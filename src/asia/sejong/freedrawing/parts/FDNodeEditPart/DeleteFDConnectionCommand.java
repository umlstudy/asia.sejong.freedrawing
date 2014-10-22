package asia.sejong.freedrawing.parts.FDNodeEditPart;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;

/**
 * Command to delete a connection from the FreeDrawing
 */
public class DeleteFDConnectionCommand extends Command
{
	private final FDConnection link;
	private FDNode sourceNode;
	private FDNode targetNode;
	

	public DeleteFDConnectionCommand(FDConnection link) {
		super("Delete link");
		this.link = link;
	}

	/**
	 * Delete the connection from the FreeDrawing
	 */
	public void execute() {
		System.out.println("DeleteConnectionCommand execute");
	}
	
	/**
	 * Restore the connection in the genealogy graph
	 */
	public void undo() {
	}

	public FDNode getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(FDNode sourceNode) {
		this.sourceNode = sourceNode;
	}

	public FDNode getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(FDNode targetNode) {
		this.targetNode = targetNode;
	}
}
