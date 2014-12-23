package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDImage;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDImageFigure extends ImageFigure implements FDShapeFigure {

	private Integer alpha = 0xff;
	
	public FDImageFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
//		graphics.setAntialias(SWT.ON);
//		graphics.setXORMode(true);
//		graphics.setBackgroundColor(getBackgroundColor());
//		graphics.setForegroundColor(getForegroundColor());
//		graphics.setBackgroundColor(new Color(null, 31, 31, 31));
		
		float angle = 0f;
		/*
		Dimension size = getSize();
		Point centerLoc = getLocation().getTranslated((size.width/2), (size.height/2));
		double targetLocX = centerLoc.x;
		double targetLocY = centerLoc.y;
		
		
		double rad = getRadius(targetLocX, targetLocY);
		double baseX = rad;
		double targetAngle = 90f*(targetLocY/(targetLocX+targetLocY));
		Point baseTranslate = getTranslate(rad, targetAngle, baseX, 0.0);
		Point translate = getTranslate(rad, targetAngle + angle, baseX, 0.0);
		graphics.translate(translate.x - baseTranslate.x, translate.y - baseTranslate.y);
		*/
		graphics.rotate(angle);
		graphics.setAlpha(255);
		graphics.setBackgroundColor(ColorConstants.gray);
		super.paintFigure(graphics);
	}
	
	private static double getRadius(double width, double height) {
		return Math.sqrt(width*width + height * height);
	}
	
	private static Point getTranslate(double r, double angle, double targetX, double targetY) {
		double pi = 3.1415926535;
		double fRadian = pi / 180. * angle;
		double newX = r * Math.cos(fRadian);
		double newY = r * Math.sin(fRadian);
		Point p = new Point();
		p.x = (int) Math.round(targetX - newX);
		p.y = (int) Math.round(targetY - newY);
		return p;
	}

	// TODO
	public LineBorder getLineBorder() {
//		return lineBorder;
		return null;
	}

	@Override
	public void setAlphaEx(int alpha) {
		this.alpha = alpha;
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
	public void setLineWidthEx(int lineWidth) {
	}

	@Override
	public void setLineStyleEx(int lineStyle) {
	}

	@Override
	public void setLineColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			setBackgroundColor(color);
		}
	}
	
	@Override
	public void setModelAttributes(FDElement model_) {
		FDImage model = (FDImage)model_;
//		setTextEx(model.getTText());
//		setFontInfoEx(model.getFontInfo());
//		setFontColorEx(model.getFontColor());
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
		
	}

//	@Override
//	public void setSelected(boolean selected) {
//		// TODO Auto-generated method stub
//		
//	}
}
