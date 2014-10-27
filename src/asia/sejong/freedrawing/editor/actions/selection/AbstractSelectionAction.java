package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public abstract class AbstractSelectionAction extends Action {
	
	private EditDomain editDomain;
	private AbstractTool tool;
	private PaletteSelectionActionGroup actionGroup;
	
	protected AbstractSelectionAction() {
		super("", AS_CHECK_BOX);
		this.tool = createTool();
	}
	
	static final AbstractSelectionAction build(Class<? extends AbstractSelectionAction> actionClass, String title, ImageDescriptor desc, EditDomain editDomain, PaletteSelectionActionGroup actionGroup) {
		AbstractSelectionAction newInstance;
		try {
			newInstance = actionClass.newInstance();
			newInstance.build(title, desc, editDomain, actionGroup);
			return newInstance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void build(String title, ImageDescriptor desc, EditDomain editDomain, PaletteSelectionActionGroup actionGroup) {
		this.setText(title);
		this.setImageDescriptor(desc);
		this.editDomain = editDomain;
		this.actionGroup = actionGroup;	
	}
	
	protected abstract AbstractTool createTool();

	public void run() {
		for ( SubSelectionActionGroup sgAction : actionGroup.getSubGroupActions() ) {
			for ( AbstractSelectionAction action : sgAction.getActions() ) {
				if ( action == this ) {
					action.setChecked(true);
				} else {
					action.setChecked(false);
				}
			}
		}
		editDomain.setActiveTool(tool);
	}
}