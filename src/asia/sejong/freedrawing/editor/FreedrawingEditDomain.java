package asia.sejong.freedrawing.editor;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Tool;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.editor.actions.toggle.ToggleActionGroup;

public class FreedrawingEditDomain extends DefaultEditDomain {
	
	private ToggleActionGroup toggleActionGroup;

	public FreedrawingEditDomain(IEditorPart editorPart) {
		super(editorPart);
	}

	public ToggleActionGroup getToggleActionGroup() {
		return toggleActionGroup;
	}

	void setToggleActionGroup(ToggleActionGroup toggleActionGroup) {
		this.toggleActionGroup = toggleActionGroup;
	}
	
	public void focusGained(FocusEvent event, EditPartViewer viewer) {
		super.focusGained(event, viewer);
		System.out.println("FOCUS GAINED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	
	public void setActiveTool(Tool tool) {
		super.setActiveTool(tool);
		if ( toggleActionGroup != null ) {
			toggleActionGroup.switchActiveTool(tool);
		}
	}
}
