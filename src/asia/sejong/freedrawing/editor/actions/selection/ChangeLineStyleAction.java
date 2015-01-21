package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDElementEditPart.command.LineStyleChangeCommand;


public class ChangeLineStyleAction extends ElementSelectionAction<FDElement> {

	private LineStyle lineStyle;
	
	public ChangeLineStyleAction(FreedrawingEditor part) {
		super(part);
	}
	
	public void run() {
		
		List<FDElement> lists = getElements(FDElement.class);
		
		if (lists.size()>0) {
			execute(new LineStyleChangeCommand(lists, getLineStyle()));
		} else {
			// TODO change color
		}
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		getEditorContext().setLineStyle(lineStyle.getStyle());
	}

//	@Override
//	protected FDElement filter(Object model) {
//		if ( model instanceof FDElement ) {
//			return (FDElement)model;
//		}
//		return null;
//	}
}
