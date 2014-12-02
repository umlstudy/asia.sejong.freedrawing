package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDWireListener;

public class FDWire extends FDElement {

	private final List<FDWireBendpoint> bendpoints = new ArrayList<FDWireBendpoint>();
	
	transient private Set<FDWireListener> listeners = new HashSet<FDWireListener>();
	transient private FDRect source;
	transient private FDRect target;
	
	private FDWire() {}

	public static FDWire newInstance__(FDRect source, FDRect target) {
		return new FDWire(source, target);
	}
	
	private FDWire(FDRect source, FDRect target) {
		setSource(source);
		setTarget(target);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof FDWire))
			return false;
		FDWire wire = (FDWire) obj;
		return wire.source == source && wire.target == target;
	}

	public int hashCode() {
		int hash = 0;
		if (source != null)
			hash += "SRC".hashCode() + source.hashCode();
		if (target != null)
			hash += "TAR".hashCode() + target.hashCode();
		return hash;
	}

	public FDRect getSource() {
		return source;
	}

	public void setSource(FDRect source) {
		this.source = source;
	}

	public FDRect getTarget() {
		return target;
	}

	public void setTarget(FDRect target) {
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
	
	@Override
	protected void fireBorderColorChanged(RGB borderColor) {
		
	}
	

	//============================================================
	// Listener add and remove
	
	public void addFDWireListener(FDWireListener listener ) {
		if ( listener != null ) {
			listeners.add(listener);
		}
	}

	public void removeFDWireListener(FDWireListener listener ) {
		if ( listener != null ) {
			listeners.remove(listener);
		}
	}

	protected void fireBendpointAdded(int locationIndex, Point location) {
		for (FDWireListener l : listeners) {
			l.bendpointAdded(locationIndex, location);
		}
	}
	
	protected void fireBendpointRemoved(int locationIndex) {
		for (FDWireListener l : listeners) {
			l.bendpointRemoved(locationIndex);
		}
	}
	
	protected void fireBendpointMoved(int locationIndex, Point newPoint) {
		for (FDWireListener l : listeners) {
			l.bendpointMoved(locationIndex, newPoint);
		}
	}
}
