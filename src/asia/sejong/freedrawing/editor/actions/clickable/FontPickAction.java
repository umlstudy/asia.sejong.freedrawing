package asia.sejong.freedrawing.editor.actions.clickable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.IWorkbenchPart;

import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.TextObject;

public class FontPickAction extends AbstractClickableAction {
	
	public FontPickAction(IWorkbenchPart part) {
		super(part);
	}
	
	public void run() {
		
		FontDialog dialog = new FontDialog(getWorkbenchPart().getSite().getShell());
		FontData fontData = dialog.open();
		
		if ( fontData != null ) {
			
			List<TextObject> lists = new ArrayList<TextObject>();
			for ( Object item : getSelectedObjects() ) {
				if ( item instanceof EditPart ) {
					Object model = ((EditPart)item).getModel();
					if ( model instanceof TextObject ) {
						lists.add((TextObject)model);
					}
				}
			}
			
			if (lists.size()>0) {
				CommandStack stack = getEditDomain().getCommandStack();
				stack.execute(new FontChangeCommand(lists, FontInfo.create(fontData)));
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
