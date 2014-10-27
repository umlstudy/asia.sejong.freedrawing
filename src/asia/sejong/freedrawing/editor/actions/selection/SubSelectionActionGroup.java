package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

public class SubSelectionActionGroup {
	
	private List<AbstractSelectionAction> actions;

	public static SubSelectionActionGroup newInstance(List<AbstractSelectionAction> actions) {
		SubSelectionActionGroup  sgAction = new SubSelectionActionGroup();
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
