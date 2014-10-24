package asia.sejong.freedrawing.editor.actions;

import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.PanningSelectionTool;

public class PanningSelectionAction extends AbstractSelectionAction {

	@Override
	protected AbstractTool createTool() {
		return new PanningSelectionTool();
	}
}
