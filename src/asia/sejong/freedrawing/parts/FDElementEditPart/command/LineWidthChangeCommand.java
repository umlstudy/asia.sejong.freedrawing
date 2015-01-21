package asia.sejong.freedrawing.parts.FDElementEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDElement;

public class LineWidthChangeCommand extends Command {

	private List<FDElement> elements;
	private Float newLineWidth;
	
	private Map<FDElement, Float> oldLineWidths;
	
	public LineWidthChangeCommand(List<FDElement> elements, Float newLineWidth) {
		this.elements = elements;
		this.newLineWidth = newLineWidth;
		this.oldLineWidths = new HashMap<FDElement, Float>();
	}
	
	public void execute() {
		oldLineWidths.clear();
		for ( FDElement item : elements ) {
			oldLineWidths.put(item, item.getLineWidth());
			if ( item.getLineWidth() != newLineWidth ) {
				item.setLineWidth(newLineWidth);
			}
		}
	}
	
	public void undo() {
		for ( FDElement item : elements ) {
			Float oldLineWidth = oldLineWidths.get(item);
			if ( item.getLineWidth() != oldLineWidth ) {
				item.setLineWidth(oldLineWidth);
			}
		}
	}
}
