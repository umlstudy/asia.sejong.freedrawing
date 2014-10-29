package asia.sejong.freedrawing.editor;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import asia.sejong.freedrawing.editor.actions.clickable.ClickableActionFactory;
import asia.sejong.freedrawing.editor.actions.clickable.ColorPickAction;
import asia.sejong.freedrawing.editor.actions.clickable.FontPickAction;
import asia.sejong.freedrawing.editor.actions.selection.AbstractSelectionAction;
import asia.sejong.freedrawing.editor.actions.selection.ConnectionSelectionAction;
import asia.sejong.freedrawing.editor.actions.selection.MarqueeSelectionAction;
import asia.sejong.freedrawing.editor.actions.selection.PaletteSelectionActionGroup;
import asia.sejong.freedrawing.editor.actions.selection.PanningSelectionAction;
import asia.sejong.freedrawing.editor.actions.selection.RectangleSelectionAction;
import asia.sejong.freedrawing.editor.actions.selection.SubSelectionActionGroup;
import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.model.io.FreedrawingModelWriter;
import asia.sejong.freedrawing.parts.common.FreedrawingEditPartFactory;
import asia.sejong.freedrawing.resources.ContextManager;

public class FreedrawingEditor extends GraphicalEditorWithFlyoutPalette {

	private final FDNodeRoot freedrawingData = new FDNodeRoot();
	private DirectEditAction directEditAction;
	private DeleteAction deleteAction;
	
	private ToolBarManager toolbarManager;
	private ContextManager contextManager;
	private PaletteSelectionActionGroup actionGroup;
	private MenuManager contextMenuManger;

	public FreedrawingEditor() {
		
	}
	
	private void init() {

		// Create ToolBar Actions
		FreedrawingEditDomain freedrawingEditDomain = (FreedrawingEditDomain)getEditDomain();
		actionGroup = new PaletteSelectionActionGroup(freedrawingEditDomain) {
			
			@Override
			public List<SubSelectionActionGroup> createSubGroupActions() {
				
				List<SubSelectionActionGroup> subGroupActions = new ArrayList<SubSelectionActionGroup>();
				
				{
					List<AbstractSelectionAction> actions = new ArrayList<AbstractSelectionAction>();
					ImageDescriptor desc = null;
					
					desc = contextManager.getImageManager().getSelectImageDescriptor();
					AbstractSelectionAction defaultAction = buildSelectionAction(PanningSelectionAction.class, "선택", desc);
					actions.add(defaultAction);
					setDefaultAction(defaultAction);
					
					desc = contextManager.getImageManager().getMarqueeImageDescriptor();
					actions.add(buildSelectionAction(MarqueeSelectionAction.class, "범위선택", desc));
					
					subGroupActions.add(SubSelectionActionGroup.newInstance(actions));
				}
				
				{
					List<AbstractSelectionAction> actions = new ArrayList<AbstractSelectionAction>();
					ImageDescriptor desc = null;
					
					desc = contextManager.getImageManager().getRectangleImageDescriptor();
					actions.add(buildSelectionAction(RectangleSelectionAction.class, "상자", desc));
					
					desc = contextManager.getImageManager().getConnectionImageDescriptor();
					actions.add(buildSelectionAction(ConnectionSelectionAction.class, "연결", desc));
					
					subGroupActions.add(SubSelectionActionGroup.newInstance(actions));
				}
				
				return subGroupActions;
			}
		};
		
		freedrawingEditDomain.setPaletteActionGroup(actionGroup);
	}
	
	@SuppressWarnings("unchecked")
	public void createPartControl(Composite parent) {
		
		init();
		
		parent.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
		parent.setLayout(GridLayoutFactory.fillDefaults().create());
//		parent.setLayout(RowLayoutFactory.fillDefaults().fill(true).type(SWT.VERTICAL).create());
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
//		toolbar.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
		toolbar.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).create());
		
		// add actions to ToolBar
		toolbarManager = createToolBarManager(toolbar, actionGroup);
		toolbarManager.add(new Separator());
		final ColorPickAction colorPickAction = ClickableActionFactory.createColorPickAction(contextManager, getEditDomain(), getSite().getPart());
		toolbarManager.add(colorPickAction);
		getSelectionActions().add(colorPickAction.getId());
		getActionRegistry().registerAction(colorPickAction);
		
		final FontPickAction fontPickAction = ClickableActionFactory.createFontPickAction(contextManager, getEditDomain(), getSite().getPart());
		toolbarManager.add(fontPickAction);
		getSelectionActions().add(fontPickAction.getId());
		getActionRegistry().registerAction(fontPickAction);
		toolbarManager.update(true);
		
		super.createPartControl(parent);
		
		contextMenuManger = new MenuManager("FreedrawingEditorPopup");
//		contextMenuManger.setRemoveAllWhenShown(true);
		contextMenuManger.add(fontPickAction);
		contextMenuManger.add(colorPickAction);
			
//		contextMenuManger.addMenuListener(new IMenuListener() {
//			@Override
//			public void menuAboutToShow(IMenuManager manager) {
//				manager.removeAll();
//				ISelection selection = getGraphicalViewer().getSelection();
//				contextMenuManger.add(fontPickAction);
//				contextMenuManger.add(colorPickAction);
//			}
//		});
//		getGraphicalViewer().getControl().setMenu(contextMenuManger.createContextMenu(getGraphicalViewer().getControl()));
		getGraphicalViewer().setContextMenu(contextMenuManger);
		
		for ( Control control : parent.getChildren() ) {
			if ( control.getLayoutData() == null ) {
				control.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).create());
			}
		}
	}

	private static ToolBarManager createToolBarManager(ToolBar toolbar, PaletteSelectionActionGroup actionGroup) {
		ToolBarManager toolbarManager = new ToolBarManager(toolbar);
		boolean start = true;
		for ( SubSelectionActionGroup subGroupAction : actionGroup.getSubGroupActions() ) {
			if ( start ) {
				start = false;
			} else {
				toolbarManager.add(new Separator());
			}
			for (AbstractSelectionAction action : subGroupAction.getActions()) {
				toolbarManager.add(action);
			}
		}
		toolbarManager.update(true);
		
		return toolbarManager;
	}

	/**
	 * freedrawingData를 뷰에 표시하기 위한 설정
	 */
	@SuppressWarnings("unchecked")
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new FreedrawingEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());

		// 액션 생성
		// 액션 생성 - directEditAction
		directEditAction = new DirectEditAction(getEditorSite().getPart());
		getSelectionActions().add(directEditAction.getId());
		getActionRegistry().registerAction(directEditAction);

		// 액션 생성 - deleteAction
		deleteAction = new DeleteAction(getEditorSite().getPart());
		getSelectionActions().add(deleteAction.getId());
		getActionRegistry().registerAction(deleteAction);
		
		// 단축키 추가 #1
		GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer);
		viewer.setKeyHandler(graphicalViewerKeyHandler);
		
		// 단축키 추가 #2
		graphicalViewerKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0), directEditAction);
		graphicalViewerKeyHandler.put(KeyStroke.getPressed(SWT.DEL, (int)SWT.DEL, 0), deleteAction);
	}

	/**
	 * 화면에 표시하기 위해 초기화 작업 수행
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(freedrawingData);
	}

	public void dispose() {
		this.toolbarManager.dispose();
		this.contextManager.dispose();
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
		FreedrawingModelWriter freedrawingDataWriter = FreedrawingModelWriter.newInstance(freedrawingData);
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

	/**
	 * 아뎁터 취득
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class type) {
		
		Object value = super.getAdapter(type);

		System.out.println("getAdapter(" + type.getSimpleName() + ") -> " + (value != null ? value.getClass().getSimpleName() : "null"));

		if (value == null) {
			return null;
		}

		return value;
	}

	/**
	 * 팔레트 팩토리
	 */
	protected PaletteRoot getPaletteRoot() {
		return FreedrawingEditorPaletteFactory.createPalette(contextManager.getImageManager());
	}

	/**
	 * 팔레트 툴 뷰어 제공 및 드레그 리스너
	 */
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}
}
