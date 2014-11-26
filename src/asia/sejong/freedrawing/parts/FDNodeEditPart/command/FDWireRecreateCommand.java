package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import asia.sejong.freedrawing.model.FDWire;

public class FDWireRecreateCommand extends FDWireCreateCommand {

	private FDWire wire;
	
	public FDWireRecreateCommand(FDWire wire) {
		setLabel("Recreate " + getWireName());
		
		this.wire = wire;
	}

	public void execute() {
		source.addTarget(target, wire);
	}
	
	public void undo() {
		source.removeTarget(target);
	}

//	
//	public FDNode getTarget() {
//		return target;
//	}
//
//	public FDNode getSource() {
//		return source;
//	}
}
