package asia.sejong.freedrawing.parts.FDConnectionEditPart;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDConnection;

/**
 * Command to delete a connection 
 */
public class DeleteFDConnectionCommand extends Command
{
	private final FDConnection conn;

	public DeleteFDConnectionCommand(FDConnection conn) {
		super("Delete Connection");
		this.conn = conn;
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		conn.getTarget().setSource(null);
		conn.getSource().setTarget(null);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
	}
}
