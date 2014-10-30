package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;

public class SelectMarqueeAction extends GroupMemberAction {
	
	@Override
	protected AbstractTool createTool() {
		return new MarqueeSelectionTool();
	}
}
