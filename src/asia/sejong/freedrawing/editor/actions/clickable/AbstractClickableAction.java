package asia.sejong.freedrawing.editor.actions.clickable;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

public abstract class AbstractClickableAction extends SelectionAction {

	private EditDomain editDomain;
	
	protected AbstractClickableAction(IWorkbenchPart part) {
		super(part);
	}
	
	static final AbstractClickableAction build(Class<? extends AbstractClickableAction> actionClass, String title, ImageDescriptor desc, EditDomain editDomain, IWorkbenchPart part) {
		AbstractClickableAction newInstance;
		try {
			newInstance = actionClass.getDeclaredConstructor(IWorkbenchPart.class).newInstance(part);
			newInstance.build(title, desc, editDomain);
			return newInstance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void build(String title, ImageDescriptor desc, EditDomain editDomain) {
		this.setText(title);
		this.setImageDescriptor(desc);
		this.setEditDomain(editDomain);
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}
}
