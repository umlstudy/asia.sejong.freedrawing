package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.PanningSelectionTool;

public class SelectPanningAction extends GroupMemberAction {

	@Override
	protected AbstractTool createTool() {
		return new PanningSelectionTool();
	}
}
