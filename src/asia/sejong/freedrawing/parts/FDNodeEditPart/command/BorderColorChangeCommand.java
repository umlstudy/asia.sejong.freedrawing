package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDElement;

public class BorderColorChangeCommand extends Command {

	private List<FDElement> baseObjects;
	private RGB newColor;
	
	private Map<FDElement, RGB> oldColors;
	
	public BorderColorChangeCommand(List<FDElement> baseObjects, RGB newColor ) {
		this.baseObjects = baseObjects;
		this.newColor = newColor;
		this.oldColors = new HashMap<FDElement, RGB>();
	}
	
	public void execute() {
		oldColors.clear();
		for ( FDElement item : baseObjects ) {
			oldColors.put(item, item.getBorderColor());
			if ( item.getBorderColor() != newColor ) {
				item.setBorderColor(newColor);
			}
		}
	}
	
	public void undo() {
		for ( FDElement item : baseObjects ) {
			RGB oldColor = oldColors.get(item);
			if ( !item.getBorderColor().equals(oldColor) ) {
				item.setBorderColor(oldColor);
			}
		}
	}
}
