package asia.sejong.freedrawing.parts.FDNodeRootEditPart.cmd;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDNode;

public class CreateFDNodeCommand extends Command {

	private FDContainer container;
	private FDNode node;
	
	public CreateFDNodeCommand(FDContainer container, FDNode node) {
		this.container = container;
		this.node = node;
	}
	
	public void execute() {
		container.addNode(node);
	}
	
	public void undo() {
		container.removeNode(node);
	}
}
