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
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
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
					AbstractSelectionAction defaultAction = buildSelectionAction(PanningSelectionAction.class, "����", desc);
					actions.add(defaultAction);
					setDefaultAction(defaultAction);
					
					desc = contextManager.getImageManager().getMarqueeImageDescriptor();
					actions.add(buildSelectionAction(MarqueeSelectionAction.class, "��������", desc));
					
					subGroupActions.add(SubSelectionActionGroup.newInstance(actions));
				}
				
				{
					List<AbstractSelectionAction> actions = new ArrayList<AbstractSelectionAction>();
					ImageDescriptor desc = null;
					
					desc = contextManager.getImageManager().getRectangleImageDescriptor();
					actions.add(buildSelectionAction(RectangleSelectionAction.class, "����", desc));
					
					desc = contextManager.getImageManager().getConnectionImageDescriptor();
					actions.add(buildSelectionAction(ConnectionSelectionAction.class, "����", desc));
					
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
		ColorPickAction colorPickAction = ClickableActionFactory.createColorPickAction(contextManager, getEditDomain(), getSite().getPart());
		toolbarManager.add(colorPickAction);
		getSelectionActions().add(colorPickAction.getId());
		getActionRegistry().registerAction(colorPickAction);
		
		FontPickAction fontPickAction = ClickableActionFactory.createFontPickAction(contextManager, getEditDomain(), getSite().getPart());
		toolbarManager.add(fontPickAction);
		getSelectionActions().add(fontPickAction.getId());
		getActionRegistry().registerAction(fontPickAction);
		toolbarManager.update(true);
		
		super.createPartControl(parent);
		
		for ( Control ctrl : parent.getChildren() ) {
			if ( ctrl.getLayoutData() == null ) {
				ctrl.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).create());
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
	 * freedrawingData�� �信 ǥ���ϱ� ���� ����
	 */
	@SuppressWarnings("unchecked")
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new FreedrawingEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());

		// �׼� ����
		// �׼� ���� - directEditAction
		directEditAction = new DirectEditAction(getEditorSite().getPart());
		getSelectionActions().add(directEditAction.getId());
		getActionRegistry().registerAction(directEditAction);

		// �׼� ���� - deleteAction
		deleteAction = new DeleteAction(getEditorSite().getPart());
		getSelectionActions().add(deleteAction.getId());
		getActionRegistry().registerAction(deleteAction);
		
		// ����Ű �߰� #1
		GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer);
		viewer.setKeyHandler(graphicalViewerKeyHandler);
		
		// ����Ű �߰� #2
		graphicalViewerKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0), directEditAction);
		graphicalViewerKeyHandler.put(KeyStroke.getPressed(SWT.DEL, (int)SWT.DEL, 0), deleteAction);
	}

	/**
	 * ȭ�鿡 ǥ���ϱ� ���� �ʱ�ȭ �۾� ����
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
	 * ������ ���Ϸκ��� ���� ����
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
	 * �����߿� ������ ������ ���Ϸ� �����Ѵ�.
	 * ���� �� ���� ���¸� �����ϰ� �����ʿ��� �����Ѵ�.
	 * 
	 * @param monitor ���� �����
	 */
	public void doSave(IProgressMonitor monitor) {

		// �� ����ȭ
		StringWriter writer = new StringWriter(5000);
		FreedrawingModelWriter freedrawingDataWriter = FreedrawingModelWriter.newInstance(freedrawingData);
		freedrawingDataWriter.write(new PrintWriter(writer));
		ByteArrayInputStream stream = new ByteArrayInputStream(writer.toString().getBytes());

		// ����ȭ ���� ���̷� ������
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

		// ������ ���¸� ���� ����� ���·� ���� �� �����ʿ� ������
		getCommandStack().markSaveLocation();
		firePropertyChange(PROP_DIRTY);
	}

	/**
	 * ����ó��
	 * 
	 * @param ex ���� (not <code>null</code>)
	 */
	private void handleException(String when, Exception ex) {
		ex.printStackTrace();
		Status status = new Status(IStatus.ERROR, "asia.sejong.freedrawing", when, ex);
		ErrorDialog.openError(getSite().getShell(), "Exception", ex.getMessage(), status);
	}

	/**
	 * ���ϰ� �������� �������� ������ �ٸ� �� ȣ�� ��
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * ���ο� ���Ϸ� ����
	 */
	public void doSaveAs() {

		// �� ���������� �����ϱ� ���� ���̾�α�
		SaveAsDialog dialog = new SaveAsDialog(getSite().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();

		// ĵ�� Ŭ���� ��δ� ����
		IPath path = dialog.getResult();
		if (path == null)
			return;

		// ������ ���Ϸ� ���� �� ���� �� ����
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
	 * �Ƶ��� ���
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
	 * �ȷ�Ʈ ���丮
	 */
	protected PaletteRoot getPaletteRoot() {
		return FreedrawingEditorPaletteFactory.createPalette(contextManager.getImageManager());
	}

	/**
	 * �ȷ�Ʈ �� ��� ���� �� �巹�� ������
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
