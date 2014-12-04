package asia.sejong.freedrawing.parts.FDLabelEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

import asia.sejong.freedrawing.figures.FDLabelFigure;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.listener.TextShapeListener;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.FDTextShapeEditPart;

public class FDLabelEditPart extends FDTextShapeEditPart implements TextShapeListener {
	
	public FDLabelEditPart(FDLabel element) {
		setModel(element);
	}
	
	protected void refreshVisuals() {
		FDLabel m = (FDLabel) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}
	
	protected IFigure createFigure() {
		return new FDLabelFigure();
	}

	protected FDLabelFigure getRectangleFigure() {
		return (FDLabelFigure) getFigure();
	}
}