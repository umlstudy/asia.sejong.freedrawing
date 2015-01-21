package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ColorChangeCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.LineColorChangeCommand;

public class ChangeLineColorAction extends ChangeColorAction<FDElement> {

	public ChangeLineColorAction(FreedrawingEditor part) {
		super(part);
	}

	@Override
	protected ColorChangeCommand createColorChangeCommand(List<FDElement> lists, RGB color) {
		return new LineColorChangeCommand(lists, color);
	}

	@Override
	protected Class<FDElement> getClassType() {
		return FDElement.class;
	}
	
	@Override
	protected void rgbChanged(RGB rgb) {
		getEditorContext().setLineColor(rgb);
	}

//	@Override
//	protected FDElement filter(Object model) {
//		if ( model instanceof FDElement ) {
//			return (FDElement)model;
//		}
//		return null;
//	}
}
