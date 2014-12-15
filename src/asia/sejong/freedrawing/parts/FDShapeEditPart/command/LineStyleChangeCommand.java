package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDElement;

public class LineStyleChangeCommand extends Command {

	private List<FDElement> elements;
	private int lineStyle;
	
	private Map<FDElement, Integer> oldLineStyles;
	
	public LineStyleChangeCommand(List<FDElement> elements, int lineStyle) {
		this.elements = elements;
		this.lineStyle = lineStyle;
		this.oldLineStyles = new HashMap<FDElement, Integer>();
	}
	
	public void execute() {
		oldLineStyles.clear();
		for ( FDElement item : elements ) {
			oldLineStyles.put(item, item.getLineStyle());
			if ( item.getLineStyle() != lineStyle ) {
				item.setLineStyle(lineStyle);
			}
		}
	}
	
	public void undo() {
		for ( FDElement item : elements ) {
			Integer oldLineStyle = oldLineStyles.get(item);
			if ( item.getLineStyle() == oldLineStyle ) {
				item.setLineStyle(oldLineStyle);
			}
		}
	}
}
