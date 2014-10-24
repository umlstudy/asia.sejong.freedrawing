package asia.sejong.freedrawing.editor.actions;

import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;

public class MarqueeSelectionAction extends AbstractSelectionAction {
	
	@Override
	protected AbstractTool createTool() {
		return new MarqueeSelectionTool();
	}
}
