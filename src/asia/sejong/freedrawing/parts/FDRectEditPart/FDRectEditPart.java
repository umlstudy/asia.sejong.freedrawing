package asia.sejong.freedrawing.parts.FDRectEditPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

import asia.sejong.freedrawing.figures.FDRectFigure;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.listener.FDRectListener;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.FDTextShapeEditPart;

public class FDRectEditPart extends FDTextShapeEditPart implements FDRectListener {
	
	public FDRectEditPart(FDRect element) {
		setModel(element);
	}
	
	protected void refreshVisuals() {
		FDRect m = (FDRect) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}
	
	protected FDRectFigure getRectangleFigure() {
		return (FDRectFigure) getFigure();
	}
}