package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDShape;

public class RotateCommand extends Command {

	private FDShape shape;
	private double degree;
	
	public RotateCommand(FDShape shape, double degree) {
		this.shape = shape;
		this.degree = degree;
	}

	public void execute() {
		shape.setDegree(degree);
	}
	
	public void undo() {
	}
}
