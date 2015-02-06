package asia.sejong.freedrawing.parts.FDEllipseEditPart;

import org.eclipse.gef.GraphicalEditPart;

import asia.sejong.freedrawing.figures.FDEllipseFigure;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.listener.FDRectListener;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.FDTextShapeEditPart;

public class FDEllipseEditPart extends FDTextShapeEditPart implements FDRectListener {
	
	public FDEllipseEditPart(FDEllipse element) {
		setModel(element);
	}
	
	protected void refreshVisuals() {
		FDEllipse m = (FDEllipse) getModel();
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), m.getBounds());
		super.refreshVisuals();
	}
	
	protected FDEllipseFigure getEllipseFigure() {
		return (FDEllipseFigure) getFigure();
	}
}