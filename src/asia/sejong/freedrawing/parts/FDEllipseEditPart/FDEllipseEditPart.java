package asia.sejong.freedrawing.parts.FDEllipseEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
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
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}
	
	protected IFigure createFigure() {
		return new FDEllipseFigure();
	}

	protected FDEllipseFigure getRectangleFigure() {
		return (FDEllipseFigure) getFigure();
	}
}