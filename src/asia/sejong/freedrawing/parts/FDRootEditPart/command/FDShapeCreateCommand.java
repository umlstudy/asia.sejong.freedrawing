package asia.sejong.freedrawing.parts.FDRootEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDRect;

public class FDShapeCreateCommand extends Command {

	private FDContainer container;
	private FDRect node;
	
	public FDShapeCreateCommand(FDContainer container, FDRect node) {
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
