package asia.sejong.freedrawing.editor.actions.toggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;

public class ToggleActionGroup {
	
	private List<PaletteToggleAction> actions;
	private PaletteToggleAction defaultAction;
	private EditDomain editDomain;
	
	public ToggleActionGroup(EditDomain editDomain) {
		this.setActions(new ArrayList<PaletteToggleAction>());
		this.setEditDomain(editDomain);
	}

	public void addAction(PaletteToggleAction action, boolean isDefaultAction) {
		if ( isDefaultAction ) {
			this.defaultAction = action;
		}
		actions.add(action);
	}
	
	public void selectDefaultAction() {
		defaultAction.run();
	}

	public void setDefaultAction(PaletteToggleAction defaultAction) {
		this.defaultAction = defaultAction;
	}

	public List<PaletteToggleAction> getActions() {
		return Collections.unmodifiableList(actions);
	}

	private void setActions(List<PaletteToggleAction> actions) {
		this.actions = actions;
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}

	public void switchActiveTool(Tool tool) {
		for ( PaletteToggleAction action : actions ) {
			if ( action.getTool() == tool ) {
				action.setChecked(true);
			} else {
				action.setChecked(false);
			}
		}
	}
}
