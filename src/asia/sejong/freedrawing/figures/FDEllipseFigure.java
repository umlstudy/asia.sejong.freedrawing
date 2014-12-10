package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDEllipseFigure extends Ellipse implements FDTextShapeFigure {

	public FDEllipseFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
		graphics.setAntialias(SWT.ON);
		graphics.setXORMode(true);
		graphics.setBackgroundColor(getBackgroundColor());
//		graphics.setForegroundColor(getForegroundColor());
//		graphics.setBackgroundColor(new Color(null, 31, 31, 31));
		super.paintFigure(graphics);
	}

	// TODO
	public LineBorder getLineBorder() {
//		return lineBorder;
		return null;
	}

	@Override
	public void setText(String text) {
		
	}
	
	@Override
	public String getText() {
		return null;
	}

	@Override
	public void setBorderColor(RGB rgbColor) {
		@SuppressWarnings("unused")
		Color color = null; // TODO
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			//super.setBackgroundColor(color);
		}
	}

	@Override
	public void setFont(FontInfo fontInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBackgroundColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setBackgroundColor(color);
		}	
	}
	
	@Override
	public void setFontColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setForegroundColor(color);
		}	
	}
}
