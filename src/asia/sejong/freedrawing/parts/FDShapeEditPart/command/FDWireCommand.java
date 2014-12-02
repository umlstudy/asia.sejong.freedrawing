package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRoot;

public abstract class FDWireCommand extends Command {

	private FDRoot root;
	
	protected FDWireCommand(FDRoot root) {
		this.setRoot(root);
	}

	protected FDRoot getRoot() {
		return root;
	}

	private void setRoot(FDRoot root) {
		this.root = root;
	}
}
