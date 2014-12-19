package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDShape;

public class RotateCommand extends Command {

	private FDShape shape;
	
	public RotateCommand(FDShape shape) {
		this.shape = shape;
	}

	public void execute() {
		shape.setAngle(100);
	}
	
	public void undo() {
	}
}
