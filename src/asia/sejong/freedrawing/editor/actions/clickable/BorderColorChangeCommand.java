package asia.sejong.freedrawing.editor.actions.clickable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.BaseObject;

public class BorderColorChangeCommand extends Command {

	private List<BaseObject> baseObjects;
	private RGB newColor;
	
	private Map<BaseObject, RGB> oldColors;
	
	public BorderColorChangeCommand(List<BaseObject> baseObjects, RGB newColor ) {
		this.baseObjects = baseObjects;
		this.newColor = newColor;
		this.oldColors = new HashMap<BaseObject, RGB>();
	}
	
	public void execute() {
		oldColors.clear();
		for ( BaseObject item : baseObjects ) {
			oldColors.put(item, item.getBorderColor());
			if ( item.getBorderColor() != newColor ) {
				item.setBorderColor(newColor);
			}
		}
	}
	
	public void undo() {
		for ( BaseObject item : baseObjects ) {
			RGB oldColor = oldColors.get(item);
			if ( !item.getBorderColor().equals(oldColor) ) {
				item.setBorderColor(oldColor);
			}
		}
	}
}
