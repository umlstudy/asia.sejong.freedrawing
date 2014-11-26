package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDRect;

public class ToFrontZOrderCommand extends Command {
	
	private final FDRect node;
	private int position;

	public ToFrontZOrderCommand(FDRect node) {
		super("To Front");
		this.node = node;
	}
	
	public void execute() {
		FDContainer parent = node.getParent();
		position = parent.changePosition(-1, node);
	}
	
	public void undo() {
		FDContainer parent = node.getParent();
		parent.changePosition(position, node);
	}
}
