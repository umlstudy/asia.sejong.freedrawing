package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;


public class FDPolygon extends FDTextShape {

	private static final long serialVersionUID = -4826394023152452709L;

	private List<Point> points;
	
	FDPolygon() {
		setPoints(new ArrayList<Point>());
	}

	public FDPolygon setDefaults(List<Point> defaultPoints) {
		setPoints(defaultPoints);
		return this;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
		Rectangle bounds = createBounds(points);
		setLocation(bounds.x, bounds.y);
		super.setSize(bounds.width, bounds.height);
	}
	
	public static Rectangle createBounds(List<Point> points) {
		PointList pointList = new PointList();
		for ( Point point : points ) {
			pointList.addPoint(point);
		}
		return pointList.getBounds();
	}
//	
//	@Override
//	public Point getLocation() {
//		System.out.println("1222222222222222222221");
//		return createBounds(points).getLocation();
//	}
//
//	@Override
//	public Dimension getSize() {
//		return createBounds(points).getSize();
//	}
	
	@Override
	public boolean setLocation(int newX, int newY) {
		System.out.println("7777777777777777777777777777");
		if (getX() == newX && getY() == newY) {
			return false;
		}
		int dx = getX() - newX;
		int dy = getY() - newY;
		for ( Point point : points ) {
			point.translate(-dx, -dy);
		}
		
		return super.setLocation(newX, newY);
	}
	
	@Override
	public boolean setSize(int newWidth, int newHeight) {
		Rectangle oldBounds = createBounds(points);
		if (oldBounds.width == newWidth && oldBounds.height == newHeight) {
			return false;
		}
		
		if ( oldBounds.width != 0 && oldBounds.height != 0 ) {
			float widthRate = (float)newWidth/oldBounds.width -1;
			float heightRate = (float)newHeight/oldBounds.height -1;
			for ( Point point : points ) {
				int dx = (int)((point.x - getX()) * widthRate);
				int dy = (int)((point.y - getY()) * heightRate);
				point.translate(dx, dy);
			}
		}
		
		return super.setSize(newWidth, newHeight);
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDPolygon clone() {
		FDPolygon polygon = (FDPolygon)super.clone();
		polygon.getPoints().addAll(getPoints());
		return polygon;
	}

	public static List<Point> createDefaultPoints() {
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(10,10));
		points.add(new Point(10,20));
		points.add(new Point(20,20));
		points.add(new Point(20,10));
		return points;
	}

}
