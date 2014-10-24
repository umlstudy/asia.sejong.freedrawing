package asia.sejong.freedrawing.parts.FDNodeEditPart.cmd;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDNode;

/**
 * Command to delete a note from a note container
 */
public class TextChangeCommand extends Command
{
	private final FDNode node;
	private final String oldValue;
	private final String newValue;

	public TextChangeCommand(FDNode node, String newValue) {
		super("Rename Node");
		this.oldValue = node.getText();
		this.newValue = newValue;
		this.node = node;
	}
	
	/**
	 * Delete the node from the container
	 */
	public void execute() {
		node.setText(newValue);
	}
	
	/**
	 * Restore the node to the container
	 */
	public void undo() {
		node.setText(oldValue);
	}
}
