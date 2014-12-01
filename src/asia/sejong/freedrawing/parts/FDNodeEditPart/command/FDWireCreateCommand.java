package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;

public class FDWireCreateCommand extends FDWireCommand {

	protected FDRect source;
	protected FDRect target;
	
	private FDWire createdWire;
	
	public FDWireCreateCommand(FDRoot root) {
		super(root);
		setLabel("Create " + getWireName());
	}

	public void execute() {
		if ( createdWire == null ) {
			createdWire = FDWire.newInstance__(source, target);
		}
		
		getRoot().addWire(createdWire);
	}
	
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
		if ( source instanceof FDRect ) {
			return true;
		}
		return false;
	}

	public static boolean isValidTarget__(Object target) {
		if ( target instanceof FDRect ) {
			return true;
		}
		return false;
	}

	public void setSource(Object source) {
		this.source = (FDRect)source;
	}

	public void setTarget(Object target) {
		this.target = (FDRect)target;
	}

//	public boolean isValidSource(Object source) {
//		if ( source instanceof FDRect && source != target ) {
//			return true;
//		}
//		return false;
//	}
//
//	public boolean isValidTarget(Object target) {
//		if ( source != null && target instanceof FDRect && source != target ) {
//			if ( !source.containsTarget((FDRect)target) ) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public void setSource(Object source) {
//		if ( isValidSource(source) ) {
//			this.source = (FDRect)source;
//		}
//	}
//
//	public void setTarget(Object target) {
//		if ( isValidTarget(target) ) {
//			this.target = (FDRect)target;
//		}
//	}
	
	public FDRect getSource() {
		return source;
	}

	public FDRect getTarget() {
		return target;
	}
}
