package asia.sejong.freedrawing.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ViewportAwareConnectionLayerClippingStrategy;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import asia.sejong.freedrawing.context.FreedrawingEditorContext;
import asia.sejong.freedrawing.draw2d.ContainerClippingStrategy;
import asia.sejong.freedrawing.draw2d.figures.FDShapeFigure;
import asia.sejong.freedrawing.draw2d.figures.GeometryUtil;
import asia.sejong.freedrawing.editor.actions.palette.factory.PaletteActionFactory;
import asia.sejong.freedrawing.parts.common.FDEditPartFactory;

public class FreedrawingEditor extends GraphicalEditor implements MouseWheelHandler {

	private FreedrawingEditorContext editorContext;
	
	private FreedrawingEditorActionManager actionManager;
	
	private boolean editorSaving = false;
	
	private ResourceTracker resourceListener = new ResourceTracker();
	
	/* 
	 * protected -> public
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getEditDomain()
	 */
	@Override
	public FreedrawingEditDomain getEditDomain() {
		return (FreedrawingEditDomain)super.getEditDomain();
	}
	
	@SuppressWarnings("unchecked")
	protected void createActions() {
		
		super.createActions();
		
		actionManager = FreedrawingEditorActionManager.newInstance(this, (List<Object>)getSelectionActions());
	}

	@Override
	public void createPartControl(Composite parent) {
		
//		parent.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
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
	
//	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
//		if (type == FreedrawingEditorContext.class) {
//			return editorContext;
//		}
//		
//		return super.getAdapter(type);
//	}
//	

	public FreedrawingEditorContext getEditorContext() {
		return editorContext;
	}
	
	@Override
	protected void createGraphicalViewer(Composite parent) {
		RulerComposite rulerComp = new RulerComposite(parent, SWT.NONE);
		super.createGraphicalViewer(rulerComp);
		rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
	}

	/**
	 * freedrawingData를 뷰에 표시하기 위한 설정
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new FDEditPartFactory());
		viewer.setSelectionManager(new FreedrawingSelectionManager(viewer));
		
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
		// SCALED_FEEDBACK_LAYER
		viewer.setRootEditPart(rootEditPart);
		
		
		IFigure feedbackLayer = rootEditPart.getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
		feedbackLayer.setClippingStrategy(ContainerClippingStrategy.INSTANCE);
		
//		feedbackLayer = rootEditPart.getLayer(LayerConstants.PRIMARY_LAYER);
////		feedbackLayer = (IFigure)feedbackLayer.getChildren().get(0);
//		//((ScalableFreeformRootEditPart)getGraphicalViewer().getRootEditPart()).getLayer(LayerConstants.PRIMARY_LAYER);
//		feedbackLayer.setClippingStrategy(new IClippingStrategy() {
//
//			@Override
//			public Rectangle[] getClip(IFigure childFigure) {
//				if ( childFigure instanceof FDShapeFigure ) {
//					FDShapeFigure sf = (FDShapeFigure)childFigure;
//					if ( sf.getDegreeEx() != 0 ) {
//						return new Rectangle[] { GeometryUtil.createSquare(sf.getBounds()) };
//					}
//				}
//				return new Rectangle[] {childFigure.getBounds(),};
//			}
//		});
////		IFigure feedbackLayer = (ConnectionLayer) rootEditPart.getLayer(LayerConstants.FEEDBACK_LAYER);
		
		
		// 마우스 CTRL + 스크롤 이벤트 캐치용
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), this);
		
		actionManager.createContextMenuManager(viewer);
	}
	

	/**
	 * 화면에 표시하기 위해 초기화 작업 수행
	 */
	@Override
	protected void initializeGraphicalViewer() {
//		super.initializeGraphicalViewer();
		
		getGraphicalViewer().setContents(editorContext.getNodeRoot());
		initProperties();
		
		actionManager.initializeKeyHandler(getGraphicalViewer());
	}

	@Override
	public void dispose() {
		if ( editorContext != null ) {
			editorContext.dispose();
		}
		
		if ( actionManager != null ) {
			actionManager.dispose();
		}
		
		((IFileEditorInput) getEditorInput()).getFile().getWorkspace().removeResourceChangeListener(resourceListener);
		
		super.dispose();
	}

	/**
	 * 지정한 파일로부터 모델을 읽음
	 */
	@Override
	protected void setInput(IEditorInput input) {
		superSetInput(input);

		IFile file = ((IFileEditorInput) input).getFile();
		try {
			InputStream is = file.getContents(false);
			ObjectInputStream ois = new ObjectInputStream(is);
			editorContext = (FreedrawingEditorContext)ois.readObject();
			ois.close();
		} catch (ClassNotFoundException e) {
			editorContext = new FreedrawingEditorContext();
		} catch (Exception e) {
			editorContext = new FreedrawingEditorContext();
//			handleException("An exception occurred while reading the file", e);
//			throw new RuntimeException(e);
		}
		
		this.editorContext.init(this);

		this.setEditDomain(new FreedrawingEditDomain(this));

		if (!editorSaving) {
			if (getGraphicalViewer() != null) {
				getGraphicalViewer().setContents(editorContext.getNodeRoot());
				initProperties();
			}
		}
	}

	protected void superSetInput(IEditorInput input) {
		// The workspace never changes for an editor. So, removing and re-adding
		// the
		// resourceListener is not necessary. But it is being done here for the
		// sake
		// of proper implementation. Plus, the resourceListener needs to be
		// added
		// to the workspace the first time around.
		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().removeResourceChangeListener(resourceListener);
		}

		super.setInput(input);

		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().addResourceChangeListener(resourceListener);
			setPartName(file.getName());
		}
	}
	
//	protected void setInput(IEditorInput input) {
//		super.setInput(input);
//		IFile file = ((IFileEditorInput) input).getFile();
//		setPartName(file.getName());
//
//		// FIXME TEST
//		this.contextManager = ContextManager.newInstance(getSite().getShell().getDisplay()); // must dispose, run before creating EditDomain
//		this.setEditDomain(new FreedrawingEditDomain(this));
//
//		try {
//			readFile(file);
//		} catch ( Exception e ) {
//			handleException("An exception occurred while reading the file", e);
//			return;
//		}
//		
//	}
//
//	private void readFile(IFile file) {
//	}

	/**
	 * 편집중에 에디터 내용을 파일로 저장한다.
	 * 저장 후 관련 상태를 갱신하고 리스너에게 통지한다.
	 * 
	 * @param monitor 진행 모니터
	 */
	@Override
	public void doSave(final IProgressMonitor progressMonitor) {
		editorSaving = true;
		SafeRunner.run(new SafeRunnable() {
			public void run() throws Exception {
				saveProperties();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				writeToOutputStream(out);
				IFile file = ((IFileEditorInput) getEditorInput()).getFile();
				file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, progressMonitor);
				getCommandStack().markSaveLocation();
			}
		});
		editorSaving = false;
	}
//	public void doSave(IProgressMonitor monitor) {
//
//		// 모델 직렬화
//		StringWriter writer = new StringWriter(5000);
//		FreedrawingModelWriter freedrawingDataWriter = FreedrawingModelWriter.newInstance(nodeRoot);
//		freedrawingDataWriter.write(new PrintWriter(writer));
//		ByteArrayInputStream stream = new ByteArrayInputStream(writer.toString().getBytes());
//
//		// 직렬화 모델을 파이로 저장함
//		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
//		try {
//			if (file.exists())
//				file.setContents(stream, false, true, monitor);
//			else
//				file.create(stream, false, monitor);
//		} catch (CoreException e) {
//			handleException("An exception occurred while saving the file", e);
//			return;
//		}
//
//		// 에디터 상태를 모델이 저장된 상태로 변경 후 리스너에 통지함
//		getCommandStack().markSaveLocation();
//		firePropertyChange(PROP_DIRTY);
//	}
	
	protected void writeToOutputStream(OutputStream os) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(os);
		saveProperties();
		out.writeObject(editorContext);
		out.close();
	}
	
	private void initProperties() {
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		
		// Snap to Geometry property
		graphicalViewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, new Boolean(editorContext.isSnapToGeometryEnabled()));
		
		// Grid properties
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED,  new Boolean(editorContext.isGridEnabled()));

		// We keep grid visibility and enablement in sync
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE,  new Boolean(editorContext.isGridEnabled()));

		// Zoom
		getZoomManager().setZoomAnimationStyle(1);
		scaleChanged(editorContext.getScaleIndex());
		
		
//		// Scroll-wheel Zoom
//		getGraphicalViewer().setProperty(
//				MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
//				MouseWheelZoomHandler.SINGLETON);
		
//		// Ruler properties
//		LogicRuler ruler = getLogicDiagram().getRuler(PositionConstants.WEST);
//		RulerProvider provider = null;
//		if (ruler != null) {
//			provider = new LogicRulerProvider(ruler);
//		}
//		getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER,
//				provider);
//		ruler = getLogicDiagram().getRuler(PositionConstants.NORTH);
//		provider = null;
//		if (ruler != null) {
//			provider = new LogicRulerProvider(ruler);
//		}
//		getGraphicalViewer().setProperty(
//				RulerProvider.PROPERTY_HORIZONTAL_RULER, provider);
//		getGraphicalViewer().setProperty(
//				RulerProvider.PROPERTY_RULER_VISIBILITY,
//				new Boolean(getLogicDiagram().getRulerVisibility()));
	}
	
	protected void saveProperties() {
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		
		editorContext.setSnapToGeometryEnabled((Boolean)graphicalViewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED));
		editorContext.setGridEnabled((Boolean)graphicalViewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED));
		
		double zoom = getZoomManager().getZoom();
		for ( int index = 0; index< getZoomManager().getZoomLevels().length; index++ ) {
			if ( getZoomManager().getZoomLevels()[index] == zoom ) {
				editorContext.setScaleIndex(index);
				break;
			}
		}
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
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * 새로운 파일로 저장
	 */
	@Override
	public void doSaveAs() {
		performSaveAs();
	}
//	public void doSaveAs() {
//
//		// 새 파일정보를 선택하기 위한 다이얼로그
//		SaveAsDialog dialog = new SaveAsDialog(getSite().getShell());
//		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
//		dialog.open();
//
//		// 캔슬 클릭시 경로는 널임
//		IPath path = dialog.getResult();
//		if (path == null)
//			return;
//
//		// 선택한 파일로 변경 및 저장 및 통지
//		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
//		super.setInput(new FileEditorInput(file));
//		doSave(null);
//		setPartName(file.getName());
//		firePropertyChange(PROP_INPUT);
//	}
	
	protected boolean performSaveAs() {
		// 새 파일정보를 선택하기 위한 다이얼로그
		SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();

		// 캔슬 클릭시 경로는 널임
		IPath path = dialog.getResult();
		if (path == null) {
			return false;
		}

		// 선택한 파일로 변경 및 저장 및 통지
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		if (!file.exists()) {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				public void execute(final IProgressMonitor monitor) {
					saveProperties();
					try {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						writeToOutputStream(out);
						file.create(new ByteArrayInputStream(out.toByteArray()), true, monitor);
						out.close();
					} catch (Exception e) {
						handleException("performSaveAs", e);
					}
				}
			};
			
			try {
				new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell()).run(false, true, op);
			} catch (Exception e) {
				handleException("performSaveAs", e);
			}
		}

		try {
			superSetInput(new FileEditorInput(file));
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			handleException("performSaveAs", e);
		}
		
		return true;
	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

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
		getActionRegistry().getAction(PaletteActionFactory.SELECT_PANNING.getId()).run();
	}
	
	public void setTargetEditPart(EditPart targetEditPart) {
		actionManager.setTargetEditPart(targetEditPart);
	}
	
	private ZoomManager getZoomManager() {
		ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart)getGraphicalViewer().getRootEditPart();
		return rootEditPart.getZoomManager();
	}

	public void scaleChanged(int scaleIndex) {
		ZoomManager zoomManager = getZoomManager();

		if ( scaleIndex >=0 && zoomManager.getZoomLevels().length > scaleIndex ) {
			zoomManager.setZoom(zoomManager.getZoomLevels()[scaleIndex]);
			editorContext.setScaleIndex(scaleIndex);
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
	
	protected void closeEditor(boolean save) {
		getSite().getPage().closeEditor(this, save);
	}
	
	// This class listens to changes to the file system in the workspace, and
	// makes changes accordingly.
	// 1) An open, saved file gets deleted -> close the editor
	// 2) An open file gets renamed or moved -> change the editor's input
	// accordingly
	class ResourceTracker implements IResourceChangeListener, IResourceDeltaVisitor {
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				if (delta != null) {
					delta.accept(this);
				}
			} catch (CoreException exception) {
				// What should be done here?
			}
		}

		public boolean visit(IResourceDelta delta) {
			if (delta == null || !delta.getResource().equals(((IFileEditorInput) getEditorInput()).getFile())) {
				return true;
			}

			System.out.println("DELTA.KIND ? "+ delta.getKind());
			
			if (delta.getKind() == IResourceDelta.REMOVED) {
				Display display = getSite().getShell().getDisplay();
				if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) { 
					// 파일이 삭제되었을 경우
					// NOTE: The case where an open, unsaved file is deleted is
					// being handled by the
					// PartListener added to the Workbench in the initialize()
					// method.
					display.asyncExec(new Runnable() {
						public void run() {
							if (!isDirty())
								closeEditor(false);
						}
					});
				} else { 
					// 파일이 옮겨졌거나 이름이 변경되었을 때
					final IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getMovedToPath());
					display.asyncExec(new Runnable() {
						public void run() {
							superSetInput(new FileEditorInput(newFile));
						}
					});
				}
			} else if (delta.getKind() == IResourceDelta.CHANGED) {
				if (!editorSaving) {
					// the file was overwritten somehow (could have been
					// replaced by another
					// version in the respository)
					final IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getFullPath());
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							setInput(new FileEditorInput(newFile));
							getCommandStack().flush();
						}
					});
				}
			}
			return false;
		}
	}
}
