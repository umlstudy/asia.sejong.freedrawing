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

	public FDEllipseFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
//		graphics.setAntialias(SWT.ON);
//		graphics.setXORMode(true);
//		graphics.setBackgroundColor(getBackgroundColor());
//		graphics.setForegroundColor(getForegroundColor());
//		graphics.setBackgroundColor(new Color(null, 31, 31, 31));
		float degree =120f;
		
////		Rectangle rect = graphics.getClip(new Rectangle());
////		getBounds();
//		
//		
//		double rad = getRadius(centerLocX, centerLocY);
//		double targetAngle = getAngle(centerLocX, centerLocY);
////		Point baseTranslate = getTranslate(rad, targetAngle);
////		Point translate = getTranslate(rad, targetAngle + angle);
////		graphics.translate(translate.x - baseTranslate.x,  translate.y - baseTranslate.y);
////		graphics.translate(40, -70);
//
// 		Point translate = getTranslate(rad, angle);
////		graphics.translate((int)rad - translate.x,  -translate.y);
//		// graphics.translate(245, -200); // 45도 287,287
//// 		graphics.translate(102, -122); // 22.5도  375,155  
//		
// 		graphics.clipRect(new Rectangle(loc.x,loc.y,500, 500));
// 		
// 		rect = graphics.getClip(new Rectangle());
//		//graphics.translate(541, -200); // 22.5도  375,155 
////		graphics.rotate(angle);
//		float cos45 = (float) Math.cos(Math.PI / 4);
//
//		// float sin45 = (float)Math.sin(45);
//		float sin45 = (float) Math.sin(Math.PI / 4);
//
////		graphics.set
////		transform.setElements(cos45, sin45, -sin45, cos45, 0, 0);
		Rectangle rectangle = new Rectangle();
		graphics.getClip(rectangle);
//		this.getBounds();
//		Point translate = RotationUtil.calculateTranslate(this, degree);
		graphics.setAntialias(SWT.ON);
		graphics.setXORMode(true);
		graphics.setBackgroundColor(getBackgroundColor());

		Point moved = new Point(rectangle.x + rectangle.width/2, rectangle.y + rectangle.height/2);
		graphics.translate(-moved.x, -moved.y);
		graphics.rotate(degree);

		super.paintFigure(graphics);
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
