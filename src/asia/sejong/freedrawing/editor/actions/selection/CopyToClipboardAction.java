package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.actions.ActionFactory;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDElement;

public class CopyToClipboardAction extends ElementSelectionAction<FDElement> {

	public CopyToClipboardAction(FreedrawingEditor editor) {
		super(editor);
		setId(ActionFactory.COPY.getId());
		setText("Copy selection to clipboard");
	}

	protected boolean calculateEnabled() {
		return !getSelectedObjects().isEmpty();
	}

	public void run() {
		Object clipboardObject = getElements(FDElement.class);
		Clipboard.getDefault().setContents(clipboardObject);
	}

//	private Object createClipboardObject() {
//		List<Object> models = new ArrayList<Object>();
//		List<?> selectedObjects = getSelectedObjects();
//		for ( Object element : selectedObjects ) {
//			if (element instanceof EditPart) {
//				Object model = ((EditPart) element).getModel();
//				models.add(model);
//			}
//		}
//		
//		return models;
//	}
}
