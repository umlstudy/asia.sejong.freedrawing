package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ColorChangeCommand;


public abstract class ChangeColorAction<T extends FDElement> extends ElementSelectionAction<T> {

	private RGB selectedColor;

	public ChangeColorAction(FreedrawingEditor part) {
		super(part);
	}
	
	public void run() {
		
		if ( selectedColor == null ) {
			ColorDialog dialog = new ColorDialog(getWorkbenchPart().getSite().getShell());
			selectedColor = dialog.open();
		}
		if ( selectedColor != null ) {
			
			List<T> lists = getElements(getClassType());
			if (lists.size()>0) {
				execute(createColorChangeCommand(lists, selectedColor));
			} else {
				// TODO change color
			}
		}
	}
	
	protected abstract Class<T> getClassType();

	protected abstract ColorChangeCommand createColorChangeCommand(List<T> lists, RGB color);

	public void setRgb(RGB rgb) {
		this.selectedColor = rgb;
		rgbChanged(rgb);
	}

	protected abstract void rgbChanged(RGB rgb);
}
