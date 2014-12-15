package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.LineStyleChangeCommand;


public class ChangeLineStyleAction extends SelectionAction {

	private int lineStyle;
	
	public ChangeLineStyleAction(IEditorPart part, int lineStyle) {
		super(part);
		
		this.lineStyle = lineStyle;
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
			execute(new LineStyleChangeCommand(lists, lineStyle));
		} else {
			// TODO change color
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

}
