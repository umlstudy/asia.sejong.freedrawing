package asia.sejong.freedrawing.editor;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import asia.sejong.freedrawing.editor.actions.FreedrawingActionFactory;
import asia.sejong.freedrawing.editor.actions.selection.SelectableActionGroup;
import asia.sejong.freedrawing.editor.tools.FDPanningSelectionTool;
import asia.sejong.freedrawing.editor.tools.FreedrawingToolFactory;
import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.model.io.FreedrawingModelWriter;
import asia.sejong.freedrawing.parts.FDNodeEditPart.FDNodeEditPart;
import asia.sejong.freedrawing.parts.common.FreedrawingEditPartFactory;
import asia.sejong.freedrawing.resources.ContextManager;

public class FreedrawingEditor extends GraphicalEditorWithFlyoutPalette {

	private final FDNodeRoot freedrawingData = new FDNodeRoot();
	
	private ToolBarManager toolbarManager;
	private ContextManager contextManager;
	private SelectableActionGroup actionGroup;
	private MenuManager contextMenuManger;
	
	private EditPart targetEditPart;
	
	@SuppressWarnings("unchecked")
	protected void createActions() {
		
		super.createActions();
		
		ActionRegistry registry = getActionRegistry();
		IAction action;
		
		// Create ToolBar Actions
		FreedrawingEditDomain editDomain = (FreedrawingEditDomain)getEditDomain();
		editDomain.setSelectableActionGroup(actionGroup);
		actionGroup = new SelectableActionGroup(editDomain);
		
		// EDIT_TEXT
		action = FreedrawingActionFactory.EDIT_TEXT.create(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		// PALETTE TOOLS
		AbstractTool tool;
		// PANNING_SELECTION
		tool = FreedrawingToolFactory.PANNING_SELECTION_TOOL.createTool(this);
		action = FreedrawingActionFactory.SELECT_PANNING.create(actionGroup, tool);
		registry.registerAction(action);
		// set default tool
		editDomain.setDefaultTool(tool);
		
		// MARQUEE_SELECTION
		tool = FreedrawingToolFactory.MARQUEE_SELECTION_TOOL.createTool(this);
		action = FreedrawingActionFactory.SELECT_MARQUEE.create(actionGroup, tool);
		registry.registerAction(action);
		
		// RECTANGLE_SELECTION
		tool = FreedrawingToolFactory.NODE_CREATION_TOOL.createTool(this);
		action = FreedrawingActionFactory.SELECT_RECTANGLE.create(actionGroup, tool);
		registry.registerAction(action);
		
		// CONNECTION_SELECTION
		tool = FreedrawingToolFactory.CONNECTION_CREATION_TOOL.createTool(this);
		action = FreedrawingActionFactory.SELECT_CONNECTION.create(actionGroup, tool);
		registry.registerAction(action);
		
		// FONT_PICK
		action = FreedrawingActionFactory.FONT_PICK.create(this, editDomain);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		// COLOR_PICK
		action = FreedrawingActionFactory.COLOR_PICK.create(this, editDomain);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
	}

	public void createPartControl(Composite parent) {
		
		parent.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
		parent.setLayout(GridLayoutFactory.fillDefaults().create());
		
		createToolBar(parent);
		
		super.createPartControl(parent);

		// 툴바와 그래픽컬 뷰 레이아웃을 잡는다.
		for ( Control control : parent.getChildren() ) {
			if ( control.getLayoutData() == null ) {
				control.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).create());
			}
		}
	}

	private void createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).create());
		
		// add actions to ToolBar
		toolbarManager = new ToolBarManager(toolbar);
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.SELECT_PANNING.getId()));
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.SELECT_MARQUEE.getId()));
		toolbarManager.add(new Separator());
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.SELECT_RECTANGLE.getId()));
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.SELECT_CONNECTION.getId()));
		toolbarManager.add(new Separator());
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.FONT_PICK.getId()));
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.COLOR_PICK.getId()));
		
		toolbarManager.update(true);
	}
	
	/**
	 * freedrawingData를 뷰에 표시하기 위한 설정
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new FreedrawingEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		
		contextMenuManger = new MenuManager("FreedrawingEditorPopup");
		contextMenuManger.setRemoveAllWhenShown(true);
		viewer.setContextMenu(contextMenuManger);
		contextMenuManger.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				resetContextMenu();
			}
		});
	}
	
	/**
	 * reset context menu
	 */
	public void resetContextMenu() {
		if ( getEditDomain().getActiveTool() instanceof FDPanningSelectionTool ) {
			if ( targetEditPart instanceof FDNodeEditPart ) {
				contextMenuManger.add(getActionRegistry().getAction(FreedrawingActionFactory.FONT_PICK.getId()));
			} else {
				contextMenuManger.add(getActionRegistry().getAction(FreedrawingActionFactory.SELECT_RECTANGLE.getId()));
			}
		}
	}

	/**
	 * 화면에 표시하기 위해 초기화 작업 수행
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(freedrawingData);
	}

	public void dispose() {
		if ( toolbarManager != null ) {
			toolbarManager.dispose();
		}
		
		if ( contextManager != null ) {
			contextManager.dispose();
		}
		
		if ( contextMenuManger != null ) {
			contextMenuManger.dispose();
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
	
	public void setActiveTool() {
		getActionRegistry().getAction(FreedrawingActionFactory.SELECT_PANNING.getId()).run();
	}
	
	public void setTargetEditPart(EditPart targetEditPart) {
		this.targetEditPart = targetEditPart;
	}
}
