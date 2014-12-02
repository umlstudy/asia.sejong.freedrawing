package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDRect;

public class ToBackZOrderCommand extends Command {
	
	private final FDRect node;
	private int position;

	public ToBackZOrderCommand(FDRect node) {
		super("To Back");
		this.node = node;
	}
	
	public void execute() {
		FDContainer parent = node.getParent();
		position = parent.changePosition(0, node);
	}
	
	public void undo() {
		FDContainer parent = node.getParent();
		parent.changePosition(position, node);
	}
}
