package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.action.Action;

public class PaletteSelectAction extends Action {
	
	private EditDomain editDomain;
	private AbstractTool tool;
	private SelectableActionGroup actionGroup;
	
	public PaletteSelectAction() {
		super("", AS_CHECK_BOX);
	}
	
	public void run() {
		for ( PaletteSelectAction action : getActionGroup().getActions() ) {
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

	public void setActionGroup(SelectableActionGroup actionGroup, boolean isDefaultAction) {
		this.actionGroup = actionGroup;
		this.actionGroup.addAction(this, isDefaultAction);
	}
}