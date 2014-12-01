package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;

/**
 * Command to delete a connection 
 */
public class FDWireDeleteCommand extends FDWireCommand
{

//	private FDRect source;
//	private FDRect target;
	
	private FDWire wire;
	
	public FDWireDeleteCommand(FDRoot root, FDWire wire) {
		super(root);
		setLabel("Delete Connection");
		
		this.wire = wire;
//		
//		this.source = wire.getSource();
//		this.target = wire.getTarget();
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		getRoot().removeWire(wire);
//		FDRect source = wire.getSource();
//		FDRect target = wire.getTarget();
//		source.removeTarget(target);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
		getRoot().addWire(wire);
//		FDRect source = wire.getSource();
//		FDRect target = wire.getTarget();
//		source.addTarget(target, wire);
	}
}
