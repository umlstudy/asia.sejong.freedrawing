package asia.sejong.freedrawing.editor;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import asia.sejong.freedrawing.editor.actions.PaletteActionFactory;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.io.FreedrawingModelWriter;
import asia.sejong.freedrawing.parts.common.FDEditPartFactory;
import asia.sejong.freedrawing.resources.ContextManager;

public class FreedrawingEditor extends GraphicalEditor implements MouseWheelHandler {

	private final FDRoot nodeRoot = new FDRoot();
	
	private ContextManager contextManager;
	
	private FreedrawingEditorActionManager actionManager;
	
	/* 
	 * protected -> public
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getEditDomain()
	 */
	public FreedrawingEditDomain getEditDomain() {
		return (FreedrawingEditDomain)super.getEditDomain();
	}
	
	@SuppressWarnings("unchecked")
	protected void createActions() {
		
		super.createActions();
		
		actionManager = FreedrawingEditorActionManager.newInstance(this, (List<Object>)getSelectionActions());
	}

	public void createPartControl(Composite parent) {
		
		parent.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
		parent.setLayout(GridLayoutFactory.fillDefaults().create());
		
		// 에디터에 툴바 생성
		actionManager.createToolBar(parent);
		
		// 에디터에 그리기 영역 생성 ( GraphicalViewer 생성 ) 
		super.createPartControl(parent);
		
		ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart)getGraphicalViewer().getRootEditPart();
		actionManager.initializeScale(rootEditPart.getZoomManager());
		
		actionManager.createViewerRelatedActions();

		// 생성된 툴바와 그리기영역의 레이아웃을 잡는다.
		for ( Control control : parent.getChildren() ) {
			if ( control.getLayoutData() == null ) {
				control.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).create());
			}
		}
	}
	
	protected void createGraphicalViewer(Composite parent) {
		RulerComposite rulerComp = new RulerComposite(parent, SWT.NONE);
		super.createGraphicalViewer(rulerComp);
		rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
	}

	/**
	 * freedrawingData를 뷰에 표시하기 위한 설정
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new FDEditPartFactory());
		
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
//		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart() {
//			public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
//				if (adapter == MouseWheelHelper.class) {
//					return this;
//				}
//				return super.getAdapter(adapter);
//			}
//		};
//		IFigure primaryLayer = rootEditPart.getLayer(LayerConstants.PRIMARY_LAYER);
//		((ConnectionLayer)rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER)).setConnectionRouter(new ShortestPathConnectionRouter(primaryLayer));
		
		viewer.setRootEditPart(rootEditPart);
		// 그리드 표시
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		// Snap to Geometry property
		viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);
//		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
				//new Boolean(getLogicDiagram().isSnapToGeometryEnabled()));
//		viewer.setSnapToGeometry(true);
		
		// 마우스 CTRL + 스크롤 이벤트 캐치용
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), this);
		
//		rootEditPart.getZoomManager().setZoomAnimationStyle(1);
//		
		
//		viewer.setRootEditPart(new ScalableFreeformRootEditPart() {
//			protected GridLayer createGridLayer() {
//				return new GridLayer() {
//					
//				};
//			}
//		});
		
		actionManager.createContextMenuManager(viewer);
	}
	

	/**
	 * 화면에 표시하기 위해 초기화 작업 수행
	 */
	protected void initializeGraphicalViewer() {
//		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(nodeRoot);
		
		actionManager.initializeKeyHandler(getGraphicalViewer());
	}

	public void dispose() {
		if ( contextManager != null ) {
			contextManager.dispose();
		}
		
		if ( actionManager != null ) {
			actionManager.dispose();
		}
		
		super.dispose();
	}

	/**
	 * 지정한 파일로부터 모델을 읽음
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFile file = ((IFileEditorInput) input).getFile();
		setPartName(file.getName());

		// FIXME TEST
		this.contextManager = ContextManager.newInstance(getSite().getShell().getDisplay()); // must dispose, run before creating EditDomain
		this.setEditDomain(new FreedrawingEditDomain(this));

		try {
			readFile(file);
		} catch ( Exception e ) {
			handleException("An exception occurred while reading the file", e);
			return;
		}
		
	}

	private void readFile(IFile file) {
	}

	/**
	 * 편집중에 에디터 내용을 파일로 저장한다.
	 * 저장 후 관련 상태를 갱신하고 리스너에게 통지한다.
	 * 
	 * @param monitor 진행 모니터
	 */
	public void doSave(IProgressMonitor monitor) {

		// 모델 직렬화
		StringWriter writer = new StringWriter(5000);
		FreedrawingModelWriter freedrawingDataWriter = FreedrawingModelWriter.newInstance(nodeRoot);
		freedrawingDataWriter.write(new PrintWriter(writer));
		ByteArrayInputStream stream = new ByteArrayInputStream(writer.toString().getBytes());

		// 직렬화 모델을 파이로 저장함
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			if (file.exists())
				file.setContents(stream, false, true, monitor);
			else
				file.create(stream, false, monitor);
		} catch (CoreException e) {
			handleException("An exception occurred while saving the file", e);
			return;
		}

		// 에디터 상태를 모델이 저장된 상태로 변경 후 리스너에 통지함
		getCommandStack().markSaveLocation();
		firePropertyChange(PROP_DIRTY);
	}

	/**
	 * 에러처리
	 * 
	 * @param ex 예외 (not <code>null</code>)
	 */
	private void handleException(String when, Exception ex) {
		ex.printStackTrace();
		Status status = new Status(IStatus.ERROR, "asia.sejong.freedrawing", when, ex);
		ErrorDialog.openError(getSite().getShell(), "Exception", ex.getMessage(), status);
	}

	/**
	 * 파일과 에디터의 콘텐츠의 내용이 다를 때 호출 됨
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * 새로운 파일로 저장
	 */
	public void doSaveAs() {

		// 새 파일정보를 선택하기 위한 다이얼로그
		SaveAsDialog dialog = new SaveAsDialog(getSite().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();

		// 캔슬 클릭시 경로는 널임
		IPath path = dialog.getResult();
		if (path == null)
			return;

		// 선택한 파일로 변경 및 저장 및 통지
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		super.setInput(new FileEditorInput(file));
		doSave(null);
		setPartName(file.getName());
		firePropertyChange(PROP_INPUT);
	}

//	public void commandStackChanged(EventObject event) {
//		firePropertyChange(IEditorPart.PROP_DIRTY);
//		super.commandStackChanged(event);
//	}
//
//	/**
//	 * 아뎁터 취득
//	 */
//	@SuppressWarnings("rawtypes")
//	public Object getAdapter(Class type) {
//		
//		Object value = super.getAdapter(type);
//
//		System.out.println("getAdapter(" + type.getSimpleName() + ") -> " + (value != null ? value.getClass().getSimpleName() : "null"));
//
//		if (value == null) {
//			return null;
//		}
//
//		return value;
//	}

//	/**
//	 * 팔레트 팩토리
//	 */
//	@Override
//	protected PaletteRoot getPaletteRoot() {
//		return FreedrawingEditorPaletteFactory.createPalette(contextManager.getImageManager());
//	}
//
//	/**
//	 * 팔레트 툴 뷰어 제공 및 드레그 리스너
//	 */
//	@Override
//	protected PaletteViewerProvider createPaletteViewerProvider() {
//		return new PaletteViewerProvider(getEditDomain()) {
//			protected void configurePaletteViewer(PaletteViewer viewer) {
//				super.configurePaletteViewer(viewer);
//				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
//			}
//		};
//	}
	
	public void setActiveTool() {
		getActionRegistry().getAction(PaletteActionFactory.TOGGLE_PANNING.getId()).run();
	}
	
	public void setTargetEditPart(EditPart targetEditPart) {
		actionManager.setTargetEditPart(targetEditPart);
	}

	public void scaleChanged(int scale) {
		ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart)getGraphicalViewer().getRootEditPart();
		ZoomManager zoomManager = rootEditPart.getZoomManager();

		if ( scale >=0 && zoomManager.getZoomLevels().length > scale ) {
			zoomManager.setZoom(zoomManager.getZoomLevels()[scale]);
		}
	}

	@Override
	public void handleMouseWheel(Event event, EditPartViewer viewer) {
		if ( event.stateMask == SWT.CTRL ) {
			if ( event.count > 0 ) {
				actionManager.setScaleNext();
			} else if ( event.count < 0 ) {
				actionManager.setScalePrevious();
			}
		}
	}
}
