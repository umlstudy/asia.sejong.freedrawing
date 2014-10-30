package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.tools.AbstractTool;

import asia.sejong.freedrawing.editor.tools.FDConnectionCreationTool;

public class SelectConnectionAction extends GroupMemberAction {
	
	@Override
	protected AbstractTool createTool() {
		return new FDConnectionCreationTool();
	}
}
