package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.action.Action;

public abstract class GroupMemberAction extends Action {
	
	private EditDomain editDomain;
	private AbstractTool tool;
	private SelectableActionGroup actionGroup;
	
	protected GroupMemberAction() {
		super("", AS_CHECK_BOX);
		this.setTool(createTool());
	}
	
	protected abstract AbstractTool createTool();

	public void run() {
		for ( GroupMemberAction action : getActionGroup().getActions() ) {
			if ( action == this ) {
				action.setChecked(true);
			} else {
				action.setChecked(false);
			}
		}
		getEditDomain().setActiveTool(getTool());
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}

	public AbstractTool getTool() {
		return tool;
	}

	public void setTool(AbstractTool tool) {
		this.tool = tool;
	}

	public SelectableActionGroup getActionGroup() {
		return actionGroup;
	}

	public void setActionGroup(SelectableActionGroup actionGroup) {
		this.actionGroup = actionGroup;
	}
}