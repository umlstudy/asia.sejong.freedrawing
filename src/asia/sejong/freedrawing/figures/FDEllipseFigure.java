package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDEllipseFigure extends RectangleFigure implements FDTextShapeFigure {

	private double degree;

	public FDEllipseFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(final Graphics graphics) {
		Rectangle bounds = new Rectangle();
		graphics.getClip(bounds);
		graphics.setAntialias(SWT.ON);
		graphics.setXORMode(true);
		graphics.setBackgroundColor(getBackgroundColor());

		if ( degree > 0 ) {
			new Rotationer() {
				@Override
				protected void paintInRotateState() {
					FDEllipseFigure.super.paintFigure(graphics);
				}
			}.execute(graphics, this, degree);
		} else {
			super.paintFigure(graphics);
		}
	}

	public LineBorder getLineBorder() {
//		return lineBorder;
		return null;
	}

	@Override
	public void setTextEx(String text) {
		
	}
	
	@Override
	public void setLineColorEx(RGB rgbColor) {
		@SuppressWarnings("unused")
		Color color = null; // TODO
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			//super.setBackgroundColor(color);
		}
	}

	@Override
	public void setFontInfoEx(FontInfo fontInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBackgroundColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setBackgroundColor(color);
		}	
	}
	
	@Override
	public void setFontColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setForegroundColor(color);
		}	
	}

	@Override
	public void setAlphaEx(int alpha) {
		
	}

	@Override
	public void setLineWidthEx(int lineWidth) {
		
	}

	@Override
	public void setLineStyleEx(int lineStyle) {
		
	}

	@Override
	public void setModelAttributes(FDElement model_) {
		FDEllipse model = (FDEllipse)model_;
		setTextEx(model.getText());
		setFontInfoEx(model.getFontInfo());
		setFontColorEx(model.getFontColor());
		//setAlpah(model.getAlpha());
		setBackgroundColorEx(model.getBackgroundColor());
		setLineWidthEx(model.getLineWidth());
		setLineStyleEx(model.getLineStyle());
		setLineColorEx(model.getLineColor());
	}

	@Override
	public void setLocationEx(Point point) {
		setLocation(point);
	}

	@Override
	public void setSizeEx(int width, int height) {
		setSize(width, height);
	}

	@Override
	public void setDegreeEx(double degree) {
		this.degree = degree;
	}
}
