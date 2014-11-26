package asia.sejong.freedrawing.parts.common;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.parts.FDNodeEditPart.FDNodeEditPart;
import asia.sejong.freedrawing.parts.FDRootEditPart.FDRootEditPart;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;

public class FDEditPartFactory implements EditPartFactory {
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FDRoot)
			return new FDRootEditPart((FDRoot) model);
		if (model instanceof FDRect)
			return new FDNodeEditPart((FDRect) model);
		if (model instanceof FDWire)
			return new FDWireEditPart((FDWire) model);
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}
}