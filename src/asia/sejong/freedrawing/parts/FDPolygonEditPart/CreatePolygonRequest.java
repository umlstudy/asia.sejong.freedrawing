package asia.sejong.freedrawing.parts.FDPolygonEditPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;

import asia.sejong.freedrawing.model.FDModelFactory;
import asia.sejong.freedrawing.model.FDPolygon;

public class CreatePolygonRequest extends Request {

	private List<Point> points = new ArrayList<Point>();
	private Point currentLocation;
	
	public void addPoint(Point mouseLocation) {
		getPoints().add(mouseLocation);
	}

	public FDPolygon createModel() {
		FDPolygon polygon = (FDPolygon)FDModelFactory.createModel(FDPolygon.class);
		polygon.setPoints(getPoints());
		return polygon;
	}

	public List<Point> getPoints() {
		return points;
	}
	
	public List<Point> getMovingPoints() {
		List<Point> movingPoints = new ArrayList<Point>(); 
		movingPoints.addAll(points);
		movingPoints.add(currentLocation);
		
		return movingPoints;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	public Point getCurrentLocation() {
		return currentLocation;
	}
}
