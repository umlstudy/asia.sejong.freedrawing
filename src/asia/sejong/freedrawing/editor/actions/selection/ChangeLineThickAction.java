package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.LineWidthChangeCommand;


public class ChangeLineThickAction extends ElementSelectionAction<FDElement> {

	private Float lineWidth;
	
	public ChangeLineThickAction(FreedrawingEditor part) {
		super(part);
	}
	
	public void run() {
		
		if ( lineWidth == null ) return;
		
			
		List<FDElement> lists = getElements(FDElement.class);
		
		if (lists.size()>0) {
//				Float lineWidth = dialog.getResult();
			execute(new LineWidthChangeCommand(lists, lineWidth));
		} else {
			// TODO change color
		}
	}

	public void setLineWidth(Float lineWidth) {
		this.lineWidth = lineWidth;
		getEditorContext().setLineWidth(lineWidth);
	}
	
//	@Override
//	protected FDElement filter(Object model) {
//		if ( model instanceof FDElement ) {
//			return (FDElement)model;
//		}
//		return null;
//	}

}
