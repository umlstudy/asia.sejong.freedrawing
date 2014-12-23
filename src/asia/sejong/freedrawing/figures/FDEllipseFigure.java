package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
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

	public FDEllipseFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(final Graphics graphics) {
		float degree =159f;
		Rectangle bounds = new Rectangle();
		graphics.getClip(bounds);
		graphics.setAntialias(SWT.ON);
		graphics.setXORMode(true);
		graphics.setBackgroundColor(getBackgroundColor());

//		// 회전
//		Point targetCenterPosition = new Point(bounds.width>>1, bounds.height>>1);
//		Point targetCenterPositionInGraphics = new Point(bounds.x + targetCenterPosition.x, bounds.y + targetCenterPosition.y);
//		Point targetTopLeftCenterPositionInGraphics  = new Point(-targetCenterPosition.x, -targetCenterPosition.y);
//		graphics.translate(targetCenterPositionInGraphics.x, targetCenterPositionInGraphics.y);
//		setLocation(targetTopLeftCenterPositionInGraphics);
//		
//		Dimension translateEffectArea = RotationUtil.calculateTranslateEffectArea(this, degree);
//		graphics.setClip(new Rectangle(-((translateEffectArea.width+4)>>1), -((translateEffectArea.height+4)>>1), translateEffectArea.width+4, translateEffectArea.height+4));
//
//		graphics.rotate(degree);
//
//		super.paintFigure(graphics);
//		
//		// 회전원복
//		setLocation(new Point(bounds.x, bounds.y));

		new Rotationer() {
			@Override
			protected void paintInRotateState() {
				FDEllipseFigure.super.paintFigure(graphics);
			}
		}.execute(graphics, this, degree);
	}
	
	private static Point getTranslate(double r, double angle) {
		double pi = 3.1415926535;
		double fRadian = pi / 180. * angle;
		double newX = r * Math.cos(fRadian);
		double newY = r * Math.sin(fRadian);
		Point p = new Point();
		p.x = (int) Math.round(newX);
		p.y = (int) Math.round(newY);
		return p;
	}
	
	public static void main(String...args) {
		for ( int y=0; y<360; y++ ) {
			Point p = getTranslate(790.3904098608484, y);
			System.out.printf(" %3ddo, x= %4d, y= %4d\n", y, p.x, p.y);
		}
	}

	// TODO
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
	public void setAngleEx(int angle) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void setSelected(boolean selected) {
//		erase();
//	}
}
