package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;

public class FDWireRecreateCommand extends FDWireCreateCommand {

	private FDWire oldWire;
	private FDWire newWire;
	
	public FDWireRecreateCommand(FDRoot root, FDWire oldWire) {
		super(root);
		setLabel("Recreate " + getWireName());
		
		this.oldWire = oldWire;
	}

	@Override
	public void execute() {
		if ( newWire == null ) {
			newWire = FDWire.newInstance__(source, target);
			newWire.addBendpoints(oldWire.getBendpoints());
		}
		getRoot().removeWire(oldWire);
		getRoot().addWire(newWire);
	}
	
	@Override
	public void undo() {
		getRoot().removeWire(newWire);
		getRoot().addWire(oldWire);
	}
	
	public boolean isValidSourceAndTarget() {
		if ( !super.isValidSourceAndTarget() ) {
			return false;
		}
		
		if ( oldWire.getSource() == source && oldWire.getTarget() == target ) {
			return false;
		}
		
		return true;
	}
}
