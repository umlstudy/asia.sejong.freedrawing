package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;

/**
 * Command to delete a connection 
 */
public class DeleteFDConnectionCommand extends Command
{

	private FDNode source;
	private FDNode target;
	
	private List<Point> removedBendpoints;
	
	public DeleteFDConnectionCommand(FDConnection conn) {
		super("Delete Connection");
		
		this.source = conn.getSource();
		this.target = conn.getTarget();
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		removedBendpoints = source.removeTarget(target);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
		source.addTarget(target, removedBendpoints);
	}
}
