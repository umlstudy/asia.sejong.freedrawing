package asia.sejong.freedrawing.parts.common;

import java.util.Map;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.parts.FDNodeRootEditPart.FDNodeRootEditPart;

public abstract class AbstractNodeEditPart extends AbstractGraphicalEditPart {

	protected ConnectionEditPart findConnection(FDConnection model) {
		if (model == null) {
			return null;
		}
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		return (ConnectionEditPart) registry.get(model);
	}

	protected final FDNodeRoot getNodeRoot() {
		EditPart editPart = this;
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
