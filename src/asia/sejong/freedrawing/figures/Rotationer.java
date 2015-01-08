package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public abstract class Rotationer {

	public void execute(Graphics graphics, IFigure figure, double degree) {
		Rectangle originalBounds = rotate(graphics, figure, (float)degree);
		paintInRotateState();
		rotateFinished(originalBounds, figure);
	}
	
	protected abstract void paintInRotateState();
	
	private Rectangle rotate(Graphics graphics, IFigure figure, float degree) {
		
		Rectangle bounds = new Rectangle(figure.getBounds());
		
		Point targetCenterPosition = new Point(bounds.width>>1, bounds.height>>1);
		Point targetCenterPositionInGraphics = new Point(bounds.x + targetCenterPosition.x, bounds.y + targetCenterPosition.y);
		Point targetTopLeftCenterPositionInGraphics  = new Point(-targetCenterPosition.x, -targetCenterPosition.y);
		graphics.translate(targetCenterPositionInGraphics);
		figure.setLocation(targetTopLeftCenterPositionInGraphics);
		
		Rectangle translateEffectArea = calculateTranslateEffectArea(figure, degree);
		graphics.setClip(translateEffectArea);

		graphics.rotate(degree);
		
		return bounds;
	}
	
	private void rotateFinished(Rectangle originalBounds, IFigure figure) {
		// 회전원복
		figure.setLocation(originalBounds.getLocation());
	}	
	
	private static Rectangle calculateTranslateEffectArea(IFigure figure, double degree) {
		
		Rectangle bounds = figure.getBounds();
		
		double r = degree * Math.PI / 180.0;
	    float cosValue = (float)Math.cos(r);
	    float sinValue = (float)Math.sin(r);
		
	    Point points[] = new Point[4];
		points[0] = getTransformedPoint(bounds.x, bounds.y, cosValue, sinValue, -sinValue, cosValue);
		points[1] = getTransformedPoint(bounds.x + bounds.width, bounds.y, cosValue, sinValue, -sinValue, cosValue);
		points[2] = getTransformedPoint(bounds.x, bounds.y + bounds.height, cosValue, sinValue, -sinValue, cosValue);
		points[3] = getTransformedPoint(bounds.x + bounds.width, bounds.y + bounds.height, cosValue, sinValue, -sinValue, cosValue);
		
		int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
		for ( Point point : points ) {
			if ( point.x < minX ) minX = point.x;
			if ( point.y < minY ) minY = point.y;
			if ( point.x > maxX ) maxX = point.x;
			if ( point.y > maxY ) maxY = point.y;
		}
		
		int width = maxX-minX + 4;
		int height = maxY-minY + 4;
		return new Rectangle(-(width>>1), -((height)>>1), width, height);
	}
	
	private static Point getTransformedPoint(float x, float y, float m11, float m12, float m21, float m22) {
		return new Point(Math.round(m11*x+m12*y), Math.round(m21*x+m22*y));
	}
}
