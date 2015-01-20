package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ColorChangeCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.LineColorChangeCommand;

public class ChangeLineColorAction extends ChangeColorAction<FDElement> {

	public ChangeLineColorAction(IEditorPart part) {
		super(part);
	}

	@Override
	protected ColorChangeCommand createColorChangeCommand(List<FDElement> lists, RGB color) {
		return new LineColorChangeCommand(lists, color);
	}

	@Override
	protected FDElement filter(Object model) {
		if ( model instanceof FDElement ) {
			return (FDElement)model;
		}
		return null;
	}
}
