package asia.sejong.freedrawing.parts.FDLabelEditPart;

import org.eclipse.gef.GraphicalEditPart;

import asia.sejong.freedrawing.draw2d.figures.FDLabelFigure;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.listener.TextShapeListener;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.FDTextShapeEditPart;

public class FDLabelEditPart extends FDTextShapeEditPart implements TextShapeListener {
	
	public FDLabelEditPart(FDLabel element) {
		setModel(element);
	}
	
	protected void refreshVisuals() {
		FDLabel m = (FDLabel) getModel();
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), m.getBounds());
		super.refreshVisuals();
	}
	
	protected FDLabelFigure getRectangleFigure() {
		return (FDLabelFigure) getFigure();
	}
}