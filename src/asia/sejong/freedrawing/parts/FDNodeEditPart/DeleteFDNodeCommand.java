package asia.sejong.freedrawing.parts.FDNodeEditPart;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;

public class DeleteFDNodeCommand extends Command
{
	private final FDNodeRoot nodeRoot;
	private final FDNode node;

	public DeleteFDNodeCommand(FDNodeRoot nodeRoot, FDNode node) {
		super("Delete Node");
		this.nodeRoot = nodeRoot;
		this.node = node;
	}

	/**
	 * Delete the connection
	 */
	public void execute() {
		nodeRoot.removeNode(node);
	}
	
	/**
	 * Restore the connection
	 */
	public void undo() {
	}
}