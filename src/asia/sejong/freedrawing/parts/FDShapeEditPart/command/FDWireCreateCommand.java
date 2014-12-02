package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDWire;

public class FDWireCreateCommand extends FDWireCommand {

	protected FDShape source;
	protected FDShape target;
	
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
		if ( source instanceof FDShape ) {
			return true;
		}
		return false;
	}

	public static boolean isValidTarget__(Object target) {
		if ( target instanceof FDShape ) {
			return true;
		}
		return false;
	}

	public void setSource(Object source) {
		this.source = (FDShape)source;
	}

	public void setTarget(Object target) {
		this.target = (FDShape)target;
	}

	public FDShape getSource() {
		return source;
	}

	public FDShape getTarget() {
		return target;
	}
}
