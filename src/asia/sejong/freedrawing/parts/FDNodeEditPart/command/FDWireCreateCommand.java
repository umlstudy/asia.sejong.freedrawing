package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDWire;

public class FDWireCreateCommand extends Command {

	protected FDRect source;
	protected FDRect target;
	
	private FDWire createdWire;
	
	public FDWireCreateCommand() {
		setLabel("Create " + getWireName());
	}

	public void execute() {
		if ( createdWire == null ) {
			createdWire = FDWire.newInstance__(source, target);
		}
		source.addTarget(target, createdWire);
	}
	
	public void undo() {
		source.removeTarget(target);
	}

	public String getWireName() {
		return "BendpointWire";
	}

	public boolean isValidSource(Object source) {
		if ( source instanceof FDRect && source != target ) {
			return true;
		}
		return false;
	}

	public boolean isValidTarget(Object target) {
		if ( source != null && target instanceof FDRect && source != target ) {
			if ( !source.containsTarget((FDRect)target) ) {
				return true;
			}
		}
		return false;
	}

	public void setSource(Object source) {
		if ( isValidSource(source) ) {
			this.source = (FDRect)source;
		}
	}

	public void setTarget(Object target) {
		if ( isValidTarget(target) ) {
			this.target = (FDRect)target;
		}
	}
	
	public FDRect getSource() {
		return source;
	}

	public FDRect getTarget() {
		return target;
	}
}
