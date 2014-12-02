package asia.sejong.freedrawing.parts.FDRectEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.figures.FDRectFigure;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.listener.FDRectListener;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.FDTextShapeEditPart;
import asia.sejong.freedrawing.resources.ContextManager;

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
	
	// ==========================================================================
	// FDTextShapeEditPart
	
	@Override
	protected void setFont(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		((FDRectFigure)getFigure()).setFont(font);
	}
	
	@Override
	protected void setBorderColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
		}
		((FDRectFigure)getFigure()).setBorderColor(color);
	}

	@Override
	protected void setText(String newText) {
		((Label)getFigure()).setText(newText);
	}
	
	protected IFigure createFigure() {
		return new FDRectFigure();
	}

	protected FDRectFigure getRectangleFigure() {
		return (FDRectFigure) getFigure();
	}
}