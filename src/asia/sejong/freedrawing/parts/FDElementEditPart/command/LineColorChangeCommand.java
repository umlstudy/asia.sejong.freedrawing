package asia.sejong.freedrawing.parts.FDElementEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDElement;

public class LineColorChangeCommand extends ColorChangeCommand {

	private List<FDElement> elements;
	private RGB newColor;
	
	private Map<FDElement, RGB> oldColors;
	
	public LineColorChangeCommand(List<FDElement> elements, RGB newColor ) {
		this.elements = elements;
		this.newColor = newColor;
		this.oldColors = new HashMap<FDElement, RGB>();
	}
	
	public void execute() {
		oldColors.clear();
		for ( FDElement item : elements ) {
			oldColors.put(item, item.getLineColor());
			if ( item.getLineColor() != newColor ) {
				item.setLineColor(newColor);
			}
		}
	}
	
	public void undo() {
		for ( FDElement item : elements ) {
			RGB oldColor = oldColors.get(item);
			if ( !item.getLineColor().equals(oldColor) ) {
				item.setLineColor(oldColor);
			}
		}
	}
}
