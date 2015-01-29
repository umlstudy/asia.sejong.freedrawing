package asia.sejong.freedrawing.parts.FDPolygonEditPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

import asia.sejong.freedrawing.figures.FDPolygonFigure;
import asia.sejong.freedrawing.model.FDPolygon;
import asia.sejong.freedrawing.model.listener.FDRectListener;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.FDTextShapeEditPart;

public class FDPolygonEditPart extends FDTextShapeEditPart implements FDRectListener {
	
	public FDPolygonEditPart(FDPolygon element) {
		setModel(element);
	}
	
	@Override
	protected void createEditPolicies() {
		
		super.createEditPolicies();
		
		installEditPolicy("POLYGON_ROLE", new FDPolygonCreatePolicy());
	}
	
	protected void refreshVisuals() {
		FDPolygon m = (FDPolygon) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}
	
	protected FDPolygonFigure getPoligonFigure() {
		return (FDPolygonFigure) getFigure();
	}
}