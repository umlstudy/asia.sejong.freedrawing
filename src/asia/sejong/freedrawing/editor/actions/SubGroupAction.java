package asia.sejong.freedrawing.editor.actions;

import java.util.List;

public class SubGroupAction {
	
	private List<AbstractSelectionAction> actions;

	public static SubGroupAction newInstance(List<AbstractSelectionAction> actions) {
		SubGroupAction  sgAction = new SubGroupAction();
		sgAction.setActions(actions);
		return sgAction;
	}

	public List<AbstractSelectionAction> getActions() {
		return actions;
	}

	private void setActions(List<AbstractSelectionAction> actions) {
		this.actions = actions;
	}
}
