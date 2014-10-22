package asia.sejong.freedrawing.parts.common;

import org.eclipse.gef.EditPart;

import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.parts.FDNodeRootEditPart.FDNodeRootEditPart;

public class EditPartUtil {

	public static final FDNodeRoot getFreedrawingData(EditPart editPart) {
		while ( editPart != null ) {
			if ( editPart instanceof FDNodeRootEditPart ) {
				return ((FDNodeRootEditPart)editPart).getModel();
			} else {
				editPart = editPart.getParent();
			}
		}
		
		return null;
	}
}
