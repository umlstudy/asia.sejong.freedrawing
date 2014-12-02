package asia.sejong.freedrawing.parts.FDEllipseEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.figures.FDEllipseFigure;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.listener.FDRectListener;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.FDTextShapeEditPart;
import asia.sejong.freedrawing.resources.ContextManager;

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
	
	// ==========================================================================
	// FDTextShapeEditPart
	
	@Override
	protected void setFont(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		((FDEllipseFigure)getFigure()).setFont(font);
	}
	
	@Override
	protected void setBorderColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
		}
		((FDEllipseFigure)getFigure()).setBorderColor(color);
	}

	@Override
	protected void setText(String newText) {
	}
	
	protected IFigure createFigure() {
		return new FDEllipseFigure();
	}

	protected FDEllipseFigure getRectangleFigure() {
		return (FDEllipseFigure) getFigure();
	}
}