package asia.sejong.freedrawing.parts.FDTextShapeEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.parts.FDElementEditPart.command.ColorChangeCommand;

public class FontColorChangeCommand extends ColorChangeCommand {

	private List<FDTextShape> elements;
	private RGB newColor;
	
	private Map<FDTextShape, RGB> oldColors;
	
	public FontColorChangeCommand(List<FDTextShape> elements, RGB newColor ) {
		this.elements = elements;
		this.newColor = newColor;
		this.oldColors = new HashMap<FDTextShape, RGB>();
	}
	
	public void execute() {
		oldColors.clear();
		for ( FDTextShape item : elements ) {
			oldColors.put(item, item.getFontColor());
			if ( item.getFontColor() != newColor ) {
				item.setFontColor(newColor);
			}
		}
	}
	
	public void undo() {
		for ( FDTextShape item : elements ) {
			RGB oldColor = oldColors.get(item);
			if ( !item.getFontColor().equals(oldColor) ) {
				item.setFontColor(oldColor);
			}
		}
	}
}
