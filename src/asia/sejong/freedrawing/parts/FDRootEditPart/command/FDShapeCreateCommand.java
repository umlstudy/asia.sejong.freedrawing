package asia.sejong.freedrawing.parts.FDRootEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDShape;

public class FDShapeCreateCommand extends Command {

	private FDContainer container;
	private FDShape shape;
	
	public FDShapeCreateCommand(FDContainer container, FDShape shape) {
		this.container = container;
		this.shape = shape;
	}
	
	public void execute() {
		container.addShape(shape);
	}
	
	public void undo() {
		container.removeShape(shape);
	}
}
