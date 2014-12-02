package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDShape;

public class ToFrontZOrderCommand extends Command {
	
	private final FDShape shape;
	private int position;

	public ToFrontZOrderCommand(FDShape shape) {
		super("To Front");
		this.shape = shape;
	}
	
	@Override
	public void execute() {
		FDContainer parent = shape.getParent();
		position = parent.changePosition(-1, shape);
	}
	
	@Override
	public void undo() {
		FDContainer parent = shape.getParent();
		parent.changePosition(position, shape);
	}
}
