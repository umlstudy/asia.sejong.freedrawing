package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.BackgroundColorChangeCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ColorChangeCommand;


public class ChangeBackgroundColorAction extends ChangeColorAction<FDShape> {

	public ChangeBackgroundColorAction(IEditorPart part) {
		super(part);
	}
//
//	@Override
//	protected ColorChangeCommand createColorChangeCommand( List<FDTextShape> lists, RGB color) {
//		return new BackgroundColorChangeCommand(lists, color);
//	}

	@Override
	protected ColorChangeCommand createColorChangeCommand(List<FDShape> lists, RGB color) {
		return new BackgroundColorChangeCommand(lists, color);
	}
	
	@Override
	protected FDShape filter(Object model) {
		if ( model instanceof FDShape ) {
			return (FDShape)model;
		}
		return null;
	}
}
