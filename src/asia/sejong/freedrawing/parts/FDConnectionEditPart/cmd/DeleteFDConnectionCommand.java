package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

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
		conn.getSource().removeTarget(conn.getTarget());
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
	}
}
