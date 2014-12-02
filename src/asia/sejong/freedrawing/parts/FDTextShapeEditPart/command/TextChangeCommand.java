package asia.sejong.freedrawing.parts.FDTextShapeEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDTextShape;

/**
 * Command to delete a note from a note container
 */
public class TextChangeCommand extends Command
{
	private final FDTextShape textShape;
	private final String oldValue;
	private final String newValue;

	public TextChangeCommand(FDTextShape textShape, String newValue) {
		super("Rename TextShape");
		this.oldValue = textShape.getText();
		this.newValue = newValue;
		this.textShape = textShape;
	}
	
	/**
	 * Delete the node from the container
	 */
	public void execute() {
		textShape.setText(newValue);
	}
	
	/**
	 * Restore the node to the container
	 */
	public void undo() {
		textShape.setText(oldValue);
	}
}
