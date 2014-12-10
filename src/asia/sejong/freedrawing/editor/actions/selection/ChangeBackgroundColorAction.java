package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.BackgroundColorChangeCommand;


public class ChangeBackgroundColorAction extends SelectionAction {

	public ChangeBackgroundColorAction(IEditorPart part) {
		super(part);
	}
	
	public void run() {
		
		ColorDialog dialog = new ColorDialog(getWorkbenchPart().getSite().getShell());
		RGB selectedColor = dialog.open();
		if ( selectedColor != null ) {
			
			List<FDShape> lists = new ArrayList<FDShape>();
			for ( Object item : getSelectedObjects() ) {
				if ( item instanceof EditPart ) {
					Object model = ((EditPart)item).getModel();
					if ( model instanceof FDShape ) {
						lists.add((FDShape)model);
					}
				}
			}
			
			
			if (lists.size()>0) {
				execute(new BackgroundColorChangeCommand(lists, selectedColor));
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
