package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;


public class FDPolygon extends FDTextShape {

	private static final long serialVersionUID = -4826394023152452709L;

	private List<Point> points;
	
	FDPolygon() {
		setPoints(new ArrayList<Point>());
	}

	public FDPolygon setDefaults(List<Point> defaultPoints) {
		points.addAll(defaultPoints);
		return this;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
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
