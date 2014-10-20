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
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import asia.sejong.freedrawing.model.area.FreedrawingData;
import asia.sejong.freedrawing.model.io.FreedrawingDataWriter;
import asia.sejong.freedrawing.parts.FreedrawingEditPartFactory;

public class FreedrawingEditor extends GraphicalEditorWithFlyoutPalette {

	private final FreedrawingData freedrawingData = new FreedrawingData();
	private DirectEditAction directEditAction;

	public FreedrawingEditor() {
		setEditDomain(new DefaultEditDomain(this));
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
		directEditAction = new DirectEditAction(getEditorSite().getPart());

		// ���þ׼�
		getSelectionActions().add(directEditAction.getId());
		getActionRegistry().registerAction(directEditAction);
		
		// ����Ű �߰�
		GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer);
		viewer.setKeyHandler(graphicalViewerKeyHandler);
		
		// F3
		graphicalViewerKeyHandler.put(KeyStroke.getPressed(SWT.F3, 0), directEditAction);
	}

	/**
	 * ȭ�鿡 ǥ���ϱ� ���� �ʱ�ȭ �۾� ����
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(freedrawingData);
	}

	public void dispose() {
		super.dispose();
	}

	/**
	 * ������ ���Ϸκ��� ���� ����
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFile file = ((IFileEditorInput) input).getFile();
		setPartName(file.getName());

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
		FreedrawingDataWriter freedrawingDataWriter = FreedrawingDataWriter.newInstance(freedrawingData);
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
		return FreedrawingEditorPaletteFactory.createPalette();
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
