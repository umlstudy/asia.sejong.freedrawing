package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.editor.dialog.SelectLineWidthDialog;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.LineWidthChangeCommand;


public class ChangeLineThickAction extends SelectionAction {

	public ChangeLineThickAction(IEditorPart part) {
		super(part);
	}
	
	public void run() {
		
		SelectLineWidthDialog dialog = new SelectLineWidthDialog(getWorkbenchPart().getSite().getShell());
		if ( dialog.open() == IDialogConstants.OK_ID ) {
			
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
				Float lineWidth = dialog.getResult();
				execute(new LineWidthChangeCommand(lists, lineWidth));
			} else {
				// TODO change color
			}
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

}
