package asia.sejong.freedrawing.commands;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.connection.AbstractFDConnection;

/**
 * Command to delete a connection from the FreeDrawing
 */
public class DeleteConnectionCommand extends Command
{
	private final AbstractFDConnection conn;
	private int connType;

	public DeleteConnectionCommand(AbstractFDConnection conn) {
		super("Delete Connection");
		this.conn = conn;
	}

	/**
	 * Delete the connection from the FreeDrawing
	 */
	public void execute() {
//		connType = 0;
//		if (conn.person.getMarriage() == conn.marriage) {
//			connType = 1;
//			conn.person.setMarriage(null);
//		}
//		if (conn.person.getParentsMarriage() == conn.marriage) {
//			connType = 2;
//			conn.person.setParentsMarriage(null);
//		}
		System.out.println("DeleteConnectionCommand execute");
	}
	
	/**
	 * Restore the connection in the genealogy graph
	 */
	public void undo() {
//		switch (connType) {
//			case 1 :
//				conn.person.setMarriage(conn.marriage);
//				break;
//			case 2 :
//				conn.person.setParentsMarriage(conn.marriage);
//				break;
//			default :
//				break;
//		}
	}
}
