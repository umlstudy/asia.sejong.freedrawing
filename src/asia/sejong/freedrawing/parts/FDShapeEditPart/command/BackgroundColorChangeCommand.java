package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.parts.FDElementEditPart.command.ColorChangeCommand;

public class BackgroundColorChangeCommand extends ColorChangeCommand {

	private List<FDShape> elements;
	private RGB newColor;
	
	private Map<FDShape, RGB> oldColors;
	
	public BackgroundColorChangeCommand(List<FDShape> elements, RGB newColor ) {
		this.elements = elements;
		this.newColor = newColor;
		this.oldColors = new HashMap<FDShape, RGB>();
	}
	
	public void execute() {
		oldColors.clear();
		for ( FDShape item : elements ) {
			oldColors.put(item, item.getBackgroundColor());
			if ( item.getBackgroundColor() != newColor ) {
				item.setBackgroundColor(newColor);
			}
		}
	}
	
	public void undo() {
		for ( FDShape item : elements ) {
			RGB oldColor = oldColors.get(item);
			if ( !item.getBackgroundColor().equals(oldColor) ) {
				item.setBackgroundColor(oldColor);
			}
		}
	}
}
