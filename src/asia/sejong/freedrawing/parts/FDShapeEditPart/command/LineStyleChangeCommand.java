package asia.sejong.freedrawing.parts.FDShapeEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.model.FDElement;

public class LineStyleChangeCommand extends Command {

	private List<FDElement> elements;
	private LineStyle lineStyle;
	
	private Map<FDElement, LineStyle> oldLineStyles;
	
	public LineStyleChangeCommand(List<FDElement> elements, LineStyle lineStyle) {
		this.elements = elements;
		this.lineStyle = lineStyle;
		this.oldLineStyles = new HashMap<FDElement, LineStyle>();
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
			LineStyle oldLineStyle = oldLineStyles.get(item);
			if ( item.getLineStyle() == oldLineStyle ) {
				item.setLineStyle(oldLineStyle);
			}
		}
	}
}
