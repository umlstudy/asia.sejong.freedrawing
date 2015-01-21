package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDElementEditPart.command.AlphaChangeCommand;

public class ChangeAlphaAction extends ElementSelectionAction<FDElement> {
	
	private Integer alpha;

	public ChangeAlphaAction(FreedrawingEditor part) {
		super(part);
	}
	
	public void run() {
		
		if ( getAlpha() != null ) {
			
			List<FDElement> lists = getElements(FDElement.class);

			if (lists.size()>0) {
				execute(new AlphaChangeCommand(lists, getAlpha()));
			} else {
				// TODO change color
			}
		}
	}

	public Integer getAlpha() {
		return alpha;
	}

	public void setAlpha(Integer alpha) {
		this.alpha = alpha;
	}
	
//	@Override
//	protected FDTextShape filter(Object model) {
//		if ( model instanceof FDTextShape ) {
//			return (FDTextShape)model;
//		}
//		return null;
//	}
}
