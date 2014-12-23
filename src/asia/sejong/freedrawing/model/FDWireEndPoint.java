package asia.sejong.freedrawing.model;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.listener.FDBaseListener;
import asia.sejong.freedrawing.model.listener.FDWireEndPointListener;

public class FDWireEndPoint extends FDElement {

	private static final long serialVersionUID = 5198536774268019990L;
	
	private ArrayList<FDWire> incommingWires;
	private ArrayList<FDWire> outgoingWires;
	
	private int x, y;
	
	protected FDWireEndPoint() {
		setIncommingWires(new ArrayList<FDWire>());
		setOutgoingWires(new ArrayList<FDWire>());
	}

	public static FDWireEndPoint newInstance(Point location) {
		FDWireEndPoint wireEndPoint = new FDWireEndPoint();
		wireEndPoint.setLocation(location.x, location.y);
		return wireEndPoint;
	}
	
	public Point getLocation() {
		return new Point(x,y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public boolean setLocation(int newX, int newY) {
		if (x == newX && y == newY) {
			return false;
		}
		x = newX;
		y = newY;
		fireLocationChanged(x, y);
		return true;
	}

	public final ArrayList<FDWire> getIncommingWires() {
		return incommingWires;
	}

	public final boolean containsTarget(FDWireEndPoint target) {
		return getOutgoingWire(target) != null;
	}
	
	private FDWire getOutgoingWire(FDWireEndPoint target) {
		for ( FDWire wire : getOutgoingWires() ) {
			if ( wire.getTarget().equals(target ) ) {
				return wire;
			}
		}
		return null;
	}
	
	public final void addWire(FDWire wire) {
		Assert.isTrue(!containsTarget(wire.getTarget()));
		
		FDWireEndPoint target = wire.getTarget();
		getOutgoingWires().add(wire);
		
		if ( target.getIncommingWires() != null ) {
			target.getIncommingWires().add(wire);
		}
	}
	
	public final void removeWire(FDWire wire) {
		Assert.isTrue(containsTarget(wire.getTarget()));
		
		FDWireEndPoint target = wire.getTarget();
		getOutgoingWires().remove(wire);
		
		if ( target.getIncommingWires() != null ) {
			target.getIncommingWires().remove(wire);
		}
	}
	
	public final FDWire removeTarget(FDRect target) {
		
		FDWire removingWire = null;
		for ( FDWire wire : getOutgoingWires() ) {
			if ( wire.getSource().equals(this) && wire.getTarget().equals(target) ) {
				removingWire = wire;
				break;
			}
		}
		
		if ( removingWire == null ) {
			// not exist
			return null;
		}
		
		getOutgoingWires().remove(removingWire);
		target.getIncommingWires().remove(removingWire);
		
		return removingWire;
	}

	public final void setIncommingWires(ArrayList<FDWire> incommingWires) {
		this.incommingWires = incommingWires;
	}

	public final ArrayList<FDWire> getOutgoingWires() {
		return outgoingWires;
	}

	public final void setOutgoingWires(ArrayList<FDWire> outgoingWires) {
		this.outgoingWires = outgoingWires;
	}
	
	//============================================================
	// Cloneable
	
	@Override
	public FDWireEndPoint clone() {
		
		FDWireEndPoint point = (FDWireEndPoint)super.clone();
		
		point.setOutgoingWires(new ArrayList<FDWire>());
		point.setIncommingWires(new ArrayList<FDWire>());
		
		point.x = this.x;
		point.y = this.y;
		
		return point;
	}
	
	//============================================================
	// FDWireEndPointListener
	
	protected void fireLocationChanged(int newX, int newY) {
		for (FDBaseListener l : listeners) {
			((FDWireEndPointListener)l).locationChanged(newX, newY);
		}
	}
}
