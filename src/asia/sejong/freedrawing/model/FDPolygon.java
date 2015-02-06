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
	}
	
	public static Rectangle createBounds(List<Point> points) {
		PointList pointList = new PointList();
		for ( Point point : points ) {
			pointList.addPoint(point);
		}
		return pointList.getBounds();
	}
	
	@Override
	public Point getLocation() {
		Rectangle oldBounds = createBounds(points);
		return oldBounds.getLocation();
	}
	
	@Override
	public Dimension getSize() {
		Rectangle oldBounds = createBounds(points);
		return oldBounds.getSize();
	}
	
	@Override
	public boolean setLocation(int newX, int newY) {
		Point oldLoc = getLocation();
		if (oldLoc.x == newX && oldLoc.y == newY) {
			return false;
		}
		int dx = oldLoc.x - newX;
		int dy = oldLoc.y - newY;
		for ( Point point : points ) {
			point.translate(-dx, -dy);
		}
		
		fireLocationChanged(newX, newY);
		return true;
	}
	
	@Override
	public boolean setSize(int newWidth, int newHeight) {
		Dimension oldSize = getSize();
		if (oldSize.width == newWidth && oldSize.height == newHeight) {
			return false;
		}
		
		Point oldLoc = getLocation();
		if ( oldSize.width != 0 && oldSize.height != 0 ) {
			float widthRate = (float)newWidth/oldSize.width -1;
			float heightRate = (float)newHeight/oldSize.height -1;
			for ( Point point : points ) {
				int dx = (int)((point.x - oldLoc.x) * widthRate);
				int dy = (int)((point.y - oldLoc.y) * heightRate);
				point.translate(dx, dy);
			}
		}
		
		fireSizeChanged(newWidth, newHeight);
		return true;
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDPolygon clone() {
		FDPolygon polygon = (FDPolygon)super.clone();
		for ( Point point : getPoints() ) {
			polygon.points.add(point.getCopy());
		}
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
