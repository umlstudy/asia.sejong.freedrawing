package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.parts.FDElementEditPart.command.ColorChangeCommand;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.command.FontColorChangeCommand;


public class ChangeFontColorAction extends ChangeColorAction<FDTextShape> {

	public ChangeFontColorAction(FreedrawingEditor part) {
		super(part);
	}

	@Override
	protected ColorChangeCommand createColorChangeCommand(List<FDTextShape> lists_, RGB color) {
		List<FDTextShape> lists = new ArrayList<FDTextShape>();
		for ( FDShape shape : lists_ ) {
			if ( shape instanceof FDTextShape ) {
				lists.add((FDTextShape)shape);
			}
		}
		return new FontColorChangeCommand(lists, color);
	}

	@Override
	protected Class<FDTextShape> getClassType() {
		return FDTextShape.class;
	}

	@Override
	protected void rgbChanged(RGB rgb) {
		getEditorContext().setFontColor(rgb);
	}
	
//	@Override
//	protected FDTextShape filter(Object model) {
//		if ( model instanceof FDTextShape ) {
//			return (FDTextShape)model;
//		}
//		return null;
//	}
}
