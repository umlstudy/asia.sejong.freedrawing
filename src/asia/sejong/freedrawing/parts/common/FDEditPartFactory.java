package asia.sejong.freedrawing.parts.common;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FDImage;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.FDPolygon;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.parts.FDEllipseEditPart.FDEllipseEditPart;
import asia.sejong.freedrawing.parts.FDImageEditPart.FDImageEditPart;
import asia.sejong.freedrawing.parts.FDLabelEditPart.FDLabelEditPart;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.FDPolygonEditPart;
import asia.sejong.freedrawing.parts.FDRectEditPart.FDRectEditPart;
import asia.sejong.freedrawing.parts.FDRootEditPart.FDRootEditPart;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;

public class FDEditPartFactory implements EditPartFactory {
	
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FDRoot) {
			return new FDRootEditPart((FDRoot) model);
		} else if (model instanceof FDRect) {
			return new FDRectEditPart((FDRect) model);
		} else if (model instanceof FDEllipse) {
			return new FDEllipseEditPart((FDEllipse) model);
		} else if (model instanceof FDWire) {
			return new FDWireEditPart((FDWire) model);
		} else if (model instanceof FDLabel ) {
			return new FDLabelEditPart((FDLabel) model);
		} else if (model instanceof FDImage ) {
			return new FDImageEditPart((FDImage) model);
		} else if (model instanceof FDPolygon ) {
			return new FDPolygonEditPart((FDPolygon) model);
		}
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}
}