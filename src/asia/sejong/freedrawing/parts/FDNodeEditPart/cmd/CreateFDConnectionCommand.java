package asia.sejong.freedrawing.parts.FDNodeEditPart.cmd;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;

public class CreateFDConnectionCommand extends Command {

	private FDNodeRoot root;
	private FDNode source;
	private FDNode target;
	
	public CreateFDConnectionCommand() {
		setLabel("Create " + getConnectionName());
	}

	public void execute() {
		source.addTarget(target);
	}
	
	public void undo() {
		source.removeTarget(target);
	}

	public String getConnectionName() {
		return "BendpointConnection";
	}

	public boolean isValidSource(Object source) {
		if ( source instanceof FDNode ) {
			return true;
		}
		return false;
	}

	public boolean isValidTarget(Object target) {
		if ( source != null && target instanceof FDNode && source != target ) {
			return true;
		}
		return false;
	}

	public void setSource(Object source) {
		if ( isValidSource(source) ) {
			this.source = (FDNode)source;
		}
	}

	public void setTarget(Object target) {
		if ( isValidTarget(target) ) {
			this.target = (FDNode)target;
		}
	}

	public FDNodeRoot getRoot() {
		return root;
	}

	public void setRoot(FDNodeRoot root) {
		this.root = root;
	}
}
