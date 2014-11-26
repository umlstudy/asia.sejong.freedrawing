package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDWire;

/**
 * Command to delete a connection 
 */
public class FDWireDeleteCommand extends Command
{

//	private FDRect source;
//	private FDRect target;
	
	private FDWire wire;
	
	public FDWireDeleteCommand(FDWire wire) {
		super("Delete Connection");
		
		this.wire = wire;
//		
//		this.source = wire.getSource();
//		this.target = wire.getTarget();
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		FDRect source = wire.getSource();
		FDRect target = wire.getTarget();
		source.removeTarget(target);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
		FDRect source = wire.getSource();
		FDRect target = wire.getTarget();
		source.addTarget(target, wire);
	}
}
