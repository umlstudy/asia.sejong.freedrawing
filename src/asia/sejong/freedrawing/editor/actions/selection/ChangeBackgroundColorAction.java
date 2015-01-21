package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.parts.FDElementEditPart.command.ColorChangeCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.BackgroundColorChangeCommand;


public class ChangeBackgroundColorAction extends ChangeColorAction<FDShape> {

	public ChangeBackgroundColorAction(FreedrawingEditor part) {
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
	protected Class<FDShape> getClassType() {
		return FDShape.class;
	}

	@Override
	protected void rgbChanged(RGB rgb) {
		getEditorContext().setBackgroundColor(rgb);
	}
	
//	@Override
//	protected FDShape filter(Object model) {
//		if ( model instanceof FDShape ) {
//			return (FDShape)model;
//		}
//		return null;
//	}
}
