package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.LineStyleChangeCommand;


public class ChangeLineStyleAction extends SelectionAction {

	private LineStyle lineStyle;
	
	public ChangeLineStyleAction(IEditorPart part, LineStyle lineStyle) {
		super(part);
		
		this.setLineStyle(lineStyle);
	}
	
	public void run() {
		
		// TODO
		List<FDElement> lists = new ArrayList<FDElement>();
		for ( Object item : getSelectedObjects() ) {
			if ( item instanceof EditPart ) {
				Object model = ((EditPart)item).getModel();
				if ( model instanceof FDElement ) {
					lists.add((FDElement)model);
				}
			}
		}
		
		if (lists.size()>0) {
			execute(new LineStyleChangeCommand(lists, getLineStyle()));
		} else {
			// TODO change color
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

}
