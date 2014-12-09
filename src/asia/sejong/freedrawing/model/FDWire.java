package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.listener.FDBaseListener;
import asia.sejong.freedrawing.model.listener.FDWireListener;

public class FDWire extends FDElement {

	private static final long serialVersionUID = 6588011474926567950L;

	private final List<FDWireBendpoint> bendpoints = new ArrayList<FDWireBendpoint>();
	
	private FDWireEndPoint source;
	private FDWireEndPoint target;
	
	private FDWire() {}

	public static FDWire newInstance__(FDWireEndPoint source, FDWireEndPoint target) {
		return new FDWire(source, target);
	}
	
	private FDWire(FDWireEndPoint source, FDWireEndPoint target) {
		setSource(source);
		setTarget(target);
	}
	
	public FDWireEndPoint getSource() {
		return source;
	}

	public void setSource(FDWireEndPoint source) {
		this.source = source;
	}

	public FDWireEndPoint getTarget() {
		return target;
	}

	public void setTarget(FDWireEndPoint target) {
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

	public void applyBendpointsDelta(Point delta) {
		for ( FDWireBendpoint bp : bendpoints ) {
			bp.applyDelta(delta);
		}
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDWire clone() {
		
		FDWire object = null;;
		try {
			object = (FDWire)this.getClass().newInstance();
			for ( FDWireBendpoint bp : bendpoints ) {
				object.bendpoints.add((FDWireBendpoint)bp.clone());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return object;
	}
	
	//============================================================
	// FDWireListener

	protected void fireBendpointAdded(int locationIndex, Point location) {
		for (FDBaseListener l : listeners) {
			((FDWireListener)l).bendpointAdded(locationIndex, location);
		}
	}
	
	protected void fireBendpointRemoved(int locationIndex) {
		for (FDBaseListener l : listeners) {
			((FDWireListener)l).bendpointRemoved(locationIndex);
		}
	}
	
	protected void fireBendpointMoved(int locationIndex, Point newPoint) {
		for (FDBaseListener l : listeners) {
			((FDWireListener)l).bendpointMoved(locationIndex, newPoint);
		}
	}
}
