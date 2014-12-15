package asia.sejong.freedrawing.editor.actions.palette;

import org.eclipse.gef.Tool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import asia.sejong.freedrawing.editor.actions.common.DropDownAction;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public class PaletteDropDownAction extends DropDownAction<PaletteAction> implements PaletteChangeListener {
	
	private IconType type;

	@Override
	public void iconChange(Tool tool, IconType newType) {
		if ( getSelectedAction() != null && tool != null ) {
			if ( type != newType || tool != getSelectedAction().getTool() ) {
				type = newType;
				Image icon = getSelectedAction().getIcon(type);
				if ( icon != null ) {
					setImageDescriptor(ImageDescriptor.createFromImage(icon));
				} else {
					setImageDescriptor(null);
				}
			}
		}
	}

	@Override
	public Tool getTool() {
		if ( getSelectedAction() != null ) {
			return getSelectedAction().getTool();
		}
		return null;
	}
}