package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.command.FontChangeCommand;

public class ChangeFontAction extends SelectionAction {
	
	private FontData fontData;

	public ChangeFontAction(IEditorPart part) {
		super(part);
	}
	
	public void run() {
		
		if ( fontData == null ) {
			FontDialog dialog = new FontDialog(getWorkbenchPart().getSite().getShell());
			fontData = dialog.open();
		}
		
		if ( fontData != null ) {
			
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
				execute(new FontChangeCommand(lists, FontInfo.create(fontData)));
			} else {
				// TODO change color
			}
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	public void setFont(FontData fontData) {
		this.fontData = fontData;
	}
}
