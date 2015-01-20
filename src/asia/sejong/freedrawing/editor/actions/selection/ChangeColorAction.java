package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ColorChangeCommand;


public abstract class ChangeColorAction<T extends FDElement> extends SelectionAction {

	private RGB selectedColor;

	public ChangeColorAction(IEditorPart part) {
		super(part);
	}
	
	public void run() {
		
		if ( selectedColor == null ) {
			ColorDialog dialog = new ColorDialog(getWorkbenchPart().getSite().getShell());
			selectedColor = dialog.open();
		}
		if ( selectedColor != null ) {
			
			List<T> lists = new ArrayList<T>();
			for ( Object item : getSelectedObjects() ) {
				if ( item instanceof EditPart ) {
					Object model = ((EditPart)item).getModel();
					T filterd = filter(model);
					if ( filterd != null ) {
						lists.add(filterd);
					}
				}
			}
			
			if (lists.size()>0) {
				execute(createColorChangeCommand(lists, selectedColor));
			} else {
				// TODO change color
			}
		}
	}
	
	protected abstract T filter(Object model);

	protected abstract ColorChangeCommand createColorChangeCommand(List<T> lists, RGB color);

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	public void setRgb(RGB rgb) {
		this.selectedColor = rgb;
	}
}
