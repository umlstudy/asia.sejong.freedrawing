package asia.sejong.freedrawing.model;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;

public class FDWireTerminalPoint extends Point implements FDWireEndPoint {

	private static final long serialVersionUID = -7431631062636432275L;

	private ArrayList<FDWire> incommingWires;
	private ArrayList<FDWire> outgoingWires;
	
	public FDWireTerminalPoint(int x, int y) {
		super(x, y);
		setIncommingWires(new ArrayList<FDWire>());
		setOutgoingWires(new ArrayList<FDWire>());
	}

	public static Object newInstance(Point location) {
		return new FDWireTerminalPoint(location.x, location.y);
	}
	
	@Override
	public ArrayList<FDWire> getIncommingWires() {
		return incommingWires;
	}

	@Override
	public boolean containsTarget(FDWireEndPoint target) {
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
	
	@Override
	public void addWire(FDWire wire) {
		Assert.isTrue(!containsTarget(wire.getTarget()));
		
		FDWireEndPoint target = wire.getTarget();
		getOutgoingWires().add(wire);
		
		if ( target.getIncommingWires() != null ) {
			target.getIncommingWires().add(wire);
		}
	}
	
	@Override
	public void removeWire(FDWire wire) {
		Assert.isTrue(containsTarget(wire.getTarget()));
		
		FDWireEndPoint target = wire.getTarget();
		getOutgoingWires().remove(wire);
		
		if ( target.getIncommingWires() != null ) {
			target.getIncommingWires().remove(wire);
		}
	}
	
	public FDWire removeTarget(FDRect target) {
		
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

	public void setIncommingWires(ArrayList<FDWire> incommingWires) {
		this.incommingWires = incommingWires;
	}

	public ArrayList<FDWire> getOutgoingWires() {
		return outgoingWires;
	}

	public void setOutgoingWires(ArrayList<FDWire> outgoingWires) {
		this.outgoingWires = outgoingWires;
	}
}
