package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

public class CopyToClipboardAction extends SelectionAction {

	public CopyToClipboardAction(IEditorPart editor) {
		super(editor);
		setId(ActionFactory.COPY.getId());
		setText("Copy selection to clipboard");
	}

	protected boolean calculateEnabled() {
		return !getSelectedObjects().isEmpty();
	}

	public void run() {
		Object clipboardObject = createClipboardObject();
		Clipboard.getDefault().setContents(clipboardObject);
	}

	private Object createClipboardObject() {
		List<Object> models = new ArrayList<Object>();
		List<?> selectedObjects = getSelectedObjects();
		for ( Object element : selectedObjects ) {
			if (element instanceof EditPart) {
				Object model = ((EditPart) element).getModel();
				models.add(model);
			}
		}
		
		return models;
	}
}
