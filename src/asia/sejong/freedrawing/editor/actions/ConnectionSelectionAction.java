package asia.sejong.freedrawing.editor.actions;

import org.eclipse.gef.tools.AbstractTool;

import asia.sejong.freedrawing.editor.tools.FDConnectionCreationTool;

public class ConnectionSelectionAction extends AbstractSelectionAction {
	
	@Override
	protected AbstractTool createTool() {
		return new FDConnectionCreationTool();
	}
}
