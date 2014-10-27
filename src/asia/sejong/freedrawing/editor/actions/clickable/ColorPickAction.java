package asia.sejong.freedrawing.editor.actions.clickable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.IWorkbenchPart;

import asia.sejong.freedrawing.model.BaseObject;


public class ColorPickAction extends AbstractClickableAction {

	protected ColorPickAction(IWorkbenchPart part) {
		super(part);
		setId("colorPickAction");
	}
	
	public void run() {
		
		ColorDialog dialog = new ColorDialog(getWorkbenchPart().getSite().getShell());
		RGB selectedColor = dialog.open();
		if ( selectedColor != null ) {
			
			List<BaseObject> lists = new ArrayList<BaseObject>();
			for ( Object item : getSelectedObjects() ) {
				if ( item instanceof EditPart ) {
					Object model = ((EditPart)item).getModel();
					if ( model instanceof BaseObject ) {
						lists.add((BaseObject)model);
					}
				}
			}
			
			if (lists.size()>0) {
				CommandStack stack = getEditDomain().getCommandStack();
				stack.execute(new BorderColorChangeCommand(lists, selectedColor));
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
