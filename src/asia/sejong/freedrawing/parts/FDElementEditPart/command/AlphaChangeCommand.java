package asia.sejong.freedrawing.parts.FDElementEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDElement;

public class AlphaChangeCommand extends Command {

	private List<FDElement> elements;
	private Integer alpha;
	
	private Map<FDElement, Integer> oldAlphas;
	
	public AlphaChangeCommand(List<FDElement> elements, Integer alpha) {
		this.elements = elements;
		this.alpha = alpha;
		this.oldAlphas = new HashMap<FDElement, Integer>();
	}
	
	public void execute() {
		oldAlphas.clear();
		for ( FDElement item : elements ) {
			oldAlphas.put(item, item.getAlpha());
			if ( item.getAlpha() != alpha ) {
				item.setAlpha(alpha);
			}
		}
	}
	
	public void undo() {
		for ( FDElement item : elements ) {
			Integer oldAlpha = oldAlphas.get(item);
			if ( item.getAlpha() != oldAlpha ) {
				item.setAlpha(oldAlpha);
			}
		}
	}
}
