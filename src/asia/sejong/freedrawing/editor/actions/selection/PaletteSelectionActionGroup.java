package asia.sejong.freedrawing.editor.actions.selection;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

import asia.sejong.freedrawing.editor.FreedrawingEditDomain;

public abstract class PaletteSelectionActionGroup {
	
	private List<SubSelectionActionGroup> actions;
	private AbstractSelectionAction defaultAction;
	
	private FreedrawingEditDomain editDomain;
	
	public PaletteSelectionActionGroup(FreedrawingEditDomain editDomain) {
		this.editDomain = editDomain;
		this.actions = createSubGroupActions();
	}

	public abstract List<SubSelectionActionGroup> createSubGroupActions();
	
	public List<SubSelectionActionGroup> getSubGroupActions() {
		return Collections.unmodifiableList(actions);
	}
	
	public void selectDefaultAction() {
		defaultAction.run();
	}

	public void setDefaultAction(AbstractSelectionAction defaultAction) {
		this.defaultAction = defaultAction;
	}
	
	protected AbstractSelectionAction buildSelectionAction(Class<? extends AbstractSelectionAction> actionClass, String title, ImageDescriptor desc) {
		return AbstractSelectionAction.build(actionClass, title, desc, editDomain, this);
	}
}
