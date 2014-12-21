package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Transform;

public class RotationUtil {
	
	public static Point calculateTranslate(IFigure figure, double degree) {
		
		Rectangle bounds = figure.getBounds();
		int targetCenterX = Math.abs(bounds.x) + (bounds.width>>1);
		int targetCenterY = Math.abs(bounds.y) + (bounds.height>>1);
		
		double r = degree * 3.141592 / 180.0;
	    float cosValue = (float)Math.cos(r);
	    float sinValue = (float)Math.sin(r);
		
		Point transformedCenter = getTransformedCenter(targetCenterX, targetCenterY, cosValue, sinValue, -sinValue, cosValue);
		int dy = targetCenterX - transformedCenter.x;
		int dx = targetCenterY - transformedCenter.y;
		
		return new Point(dx, dy);
	}

	public static void setTransform(Figure figure, Transform transform, double angle) {
		
		Dimension size = figure.getSize();
		Point loc = figure.getLocation();
		int targetCenterX = Math.abs(loc.x) + (size.width>>1);
		int targetCenterY = Math.abs(loc.y) + (size.height>>1);
		
		double r = angle * 3.141592 / 180.0;
	    float cosValue = (float)Math.cos(r);
	    float sinValue = (float)Math.sin(r);
		
		Point transformedCenter = getTransformedCenter(targetCenterX, targetCenterY, cosValue, sinValue, -sinValue, cosValue);
		int dy = targetCenterX - transformedCenter.x;
		int dx = targetCenterY - transformedCenter.y;
		
		transform.setElements(cosValue, sinValue, -sinValue, cosValue, dx, dy);
	}
	
	private static Point getTransformedCenter(float x, float y, float m11, float m12, float m21, float m22) {
		return new Point(Math.round(m11*x+m12*y), Math.round(m21*x+m22*y));
	}
	
	public double calculateAngle(double width, double height) {
		double radian = Math.atan(height / width);
		float  degree = (float) (57.295779513082323 * radian);
		return degree;
	}
	
//	public static double getRadius(double width, double height) {
//		return Math.sqrt(width*width + height * height);
//	}
}
