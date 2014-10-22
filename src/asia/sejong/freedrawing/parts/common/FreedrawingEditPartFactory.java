package asia.sejong.freedrawing.parts.common;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.parts.FDConnectionEditPart.FDConnectionEditPart;
import asia.sejong.freedrawing.parts.FDNodeEditPart.FDNodeEditPart;
import asia.sejong.freedrawing.parts.FDNodeRootEditPart.FDNodeRootEditPart;

public class FreedrawingEditPartFactory implements EditPartFactory {
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FDNodeRoot)
			return new FDNodeRootEditPart((FDNodeRoot) model);
		if (model instanceof FDNode)
			return new FDNodeEditPart((FDNode) model);
		if (model instanceof FDConnection)
			return new FDConnectionEditPart((FDConnection) model);
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}
}