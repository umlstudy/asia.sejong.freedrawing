package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.FontColorChangeCommand;


public class ChangeFontColorAction extends SelectionAction {

	public ChangeFontColorAction(IEditorPart part) {
		super(part);
	}
	
	public void run() {
		
		ColorDialog dialog = new ColorDialog(getWorkbenchPart().getSite().getShell());
		RGB selectedColor = dialog.open();
		if ( selectedColor != null ) {
			
			List<FDTextShape> lists = new ArrayList<FDTextShape>();
			for ( Object item : getSelectedObjects() ) {
				if ( item instanceof EditPart ) {
					Object model = ((EditPart)item).getModel();
					if ( model instanceof FDTextShape ) {
						lists.add((FDTextShape)model);
					}
				}
			}
			
			if (lists.size()>0) {
				execute(new FontColorChangeCommand(lists, selectedColor));
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
