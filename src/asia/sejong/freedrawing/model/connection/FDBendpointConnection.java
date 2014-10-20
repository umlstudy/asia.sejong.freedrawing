package asia.sejong.freedrawing.model.connection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

public class FDBendpointConnection extends AbstractFDConnection {

	private final List<Point> bendpoints = new ArrayList<Point>();
	
	public FDBendpointConnection() {
	}
	
	public void add(int index, Point point) {
		bendpoints.add(index, point);
	}
}
