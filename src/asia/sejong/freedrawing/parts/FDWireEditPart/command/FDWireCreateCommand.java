package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.FDWireEndPoint;

public class FDWireCreateCommand extends FDWireCommand {

	protected FDWireEndPoint source;
	protected FDWireEndPoint target;
	
	private FDWire createdWire;
	
	public FDWireCreateCommand(FDRoot root) {
		super(root);
		setLabel("Create " + getWireName());
	}

	@Override
	public void execute() {
		if ( createdWire == null ) {
			createdWire = FDWire.newInstance__(source, target);
		}
		
		getRoot().addWire(createdWire);
	}
	
	@Override
	public void undo() {
		getRoot().removeWire(createdWire);
	}

	public String getWireName() {
		return "BendpointWire";
	}
	
	public boolean isValidSourceAndTarget() {
		if ( source != target ) {
			if ( !source.containsTarget(target) ) {
				return true;
			}
		}

		return false;
	}
	
	public static boolean isValidSource__(Object source) {
		if ( source instanceof FDWireEndPoint ) {
			return true;
		}
		return false;
	}

	public static boolean isValidTarget__(Object target) {
		if ( target instanceof FDWireEndPoint ) {
			return true;
		}
		return false;
	}

	public void setSource(Object source) {
		this.source = (FDWireEndPoint)source;
	}

	public void setTarget(Object target) {
		this.target = (FDWireEndPoint)target;
	}

	public FDWireEndPoint getSource() {
		return source;
	}

	public FDWireEndPoint getTarget() {
		return target;
	}
}
