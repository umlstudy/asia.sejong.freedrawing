package asia.sejong.freedrawing.parts.FDNodeRootEditPart.cmd;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;

public class CreateFDNodeCommand extends Command {

	private FDNodeRoot nodeRoot;
	private FDNode node;
	
	public CreateFDNodeCommand(FDNodeRoot nodeRoot, FDNode node) {
		this.nodeRoot = nodeRoot;
		this.node = node;
	}
	
	public void execute() {
		nodeRoot.addNode(node);
	}
	
	public void undo() {
		nodeRoot.removeNode(node);
	}
}
