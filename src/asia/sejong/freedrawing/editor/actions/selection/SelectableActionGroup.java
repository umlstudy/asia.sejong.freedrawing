package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;

public class SelectableActionGroup {
	
	private List<PaletteSelectAction> actions;
	private PaletteSelectAction defaultAction;
	private EditDomain editDomain;
	
	public SelectableActionGroup(EditDomain editDomain) {
		this.setActions(new ArrayList<PaletteSelectAction>());
		this.setEditDomain(editDomain);
	}

	public void addAction(PaletteSelectAction action, boolean isDefaultAction) {
		if ( isDefaultAction ) {
			this.defaultAction = action;
		}
		actions.add(action);
	}
	
	public void selectDefaultAction() {
		defaultAction.run();
	}

	public void setDefaultAction(PaletteSelectAction defaultAction) {
		this.defaultAction = defaultAction;
	}

	public List<PaletteSelectAction> getActions() {
		return Collections.unmodifiableList(actions);
	}

	private void setActions(List<PaletteSelectAction> actions) {
		this.actions = actions;
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}

	public void switchActiveTool(Tool tool) {
		for ( PaletteSelectAction action : actions ) {
			if ( action.getTool() == tool ) {
				action.setChecked(true);
			} else {
				action.setChecked(false);
			}
		}
	}
}
