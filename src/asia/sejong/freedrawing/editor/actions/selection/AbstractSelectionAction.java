package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

public abstract class AbstractSelectionAction extends SelectionAction {

	private EditDomain editDomain;
	
	protected AbstractSelectionAction(IWorkbenchPart part) {
		super(part);
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}
}
