package asia.sejong.freedrawing.model.listener;

import org.eclipse.draw2d.geometry.Point;

public interface FDWireListener {

	void bendpointAdded(int locationIndex, Point location);
	void bendpointRemoved(int locationIndex);
	void bendpointMoved(int locationIndex, Point newPoint);
}
