package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;

/**
 * Command to delete a connection 
 */
public class FDWireDeleteCommand extends FDWireCommand {

	private FDWire wire;
	
	public FDWireDeleteCommand(FDRoot root, FDWire wire) {
		super(root);
		setLabel("Delete Connection");
		
		this.wire = wire;
	}

	@Override
	public void execute() {
		getRoot().removeWire(wire);
	}
	
	@Override
	public void undo() {
		getRoot().addWire(wire);
	}
}
