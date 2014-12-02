package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.listener.FDElementListener;
import asia.sejong.freedrawing.model.listener.FDWireListener;

public class FDWire extends FDElement {

	private final List<FDWireBendpoint> bendpoints = new ArrayList<FDWireBendpoint>();
	
	private FDShape source;
	private FDShape target;
	
	private FDWire() {}

	public static FDWire newInstance__(FDShape source, FDShape target) {
		return new FDWire(source, target);
	}
	
	private FDWire(FDShape source, FDShape target) {
		setSource(source);
		setTarget(target);
	}
	
	public FDShape getSource() {
		return source;
	}

	public void setSource(FDShape source) {
		this.source = source;
	}

	public FDShape getTarget() {
		return target;
	}

	public void setTarget(FDShape target) {
		this.target = target;
	}
	
	public void addBendpoint(int locationIndex, Point location) {
		bendpoints.add(locationIndex, FDWireBendpoint.newInstance(location));
		fireBendpointAdded(locationIndex, location);
	}

	public Point removeBendpoint(int locationIndex) {
		Point removed = bendpoints.remove(locationIndex);
		fireBendpointRemoved(locationIndex);
		return removed;
	}
	
	public Point moveBendpoint(int locationIndex, Point newPoint) {
		Point oldLocation = bendpoints.set(locationIndex, FDWireBendpoint.newInstance(newPoint));
		fireBendpointMoved(locationIndex, newPoint);
		return oldLocation;
	}
	
	public List<FDWireBendpoint> getBendpoints() {
		return Collections.unmodifiableList(bendpoints);
	}
	
	public void addBendpoints(List<FDWireBendpoint> bendpoints) {
		if ( bendpoints.size()>0 ) {
			this.bendpoints.addAll(bendpoints);
		}
	}
	
	//============================================================
	// FDWireListener

	protected void fireBendpointAdded(int locationIndex, Point location) {
		for (FDElementListener l : listeners) {
			((FDWireListener)l).bendpointAdded(locationIndex, location);
		}
	}
	
	protected void fireBendpointRemoved(int locationIndex) {
		for (FDElementListener l : listeners) {
			((FDWireListener)l).bendpointRemoved(locationIndex);
		}
	}
	
	protected void fireBendpointMoved(int locationIndex, Point newPoint) {
		for (FDElementListener l : listeners) {
			((FDWireListener)l).bendpointMoved(locationIndex, newPoint);
		}
	}
}
