package asia.sejong.freedrawing.parts;

import org.eclipse.gef.EditPart;

import asia.sejong.freedrawing.model.area.FreedrawingData;
import asia.sejong.freedrawing.parts.area.FreedrawingDataEditPart;

public class EditPartUtil {

	public static final FreedrawingData getFreedrawingData(EditPart editPart) {
		while ( editPart != null ) {
			if ( editPart instanceof FreedrawingDataEditPart ) {
				return ((FreedrawingDataEditPart)editPart).getModel();
			} else {
				editPart = editPart.getParent();
			}
		}
		
		return null;
	}
}
