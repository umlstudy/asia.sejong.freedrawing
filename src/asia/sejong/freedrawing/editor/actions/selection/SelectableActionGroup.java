package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditDomain;

public class SelectableActionGroup {
	
	private List<GroupMemberAction> actions;
	private GroupMemberAction defaultAction;
	private EditDomain editDomain;
	
	public SelectableActionGroup(EditDomain editDomain) {
		this.setActions(new ArrayList<GroupMemberAction>());
		this.setEditDomain(editDomain);
	}

	public void addAction(GroupMemberAction action, boolean isDefaultAction) {
		if ( isDefaultAction ) {
			this.defaultAction = action;
		}
		getActions().add(action);
	}
	
	public void selectDefaultAction() {
		defaultAction.run();
	}

	public void setDefaultAction(GroupMemberAction defaultAction) {
		this.defaultAction = defaultAction;
	}

	public List<GroupMemberAction> getActions() {
		return Collections.unmodifiableList(actions);
	}

	private void setActions(List<GroupMemberAction> actions) {
		this.actions = actions;
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}
}
