package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.listener.FDContainerListener;
import asia.sejong.freedrawing.model.listener.FDRootListener;

public class FDRoot implements FDContainer {
	
	public static Integer ROUTER_MANUAL = new Integer(0);
	public static Integer ROUTER_SHORTEST_PATH = new Integer(12);
	
	private final List<FDShape> childElements = new ArrayList<FDShape>();
	private final List<FDWire> wires = new ArrayList<FDWire>();
	private Integer router = ROUTER_MANUAL;
	
	private final Collection<FDRootListener> listeners = new HashSet<FDRootListener>();
	
	public FDRoot() {}

	public Integer getConnectionRouter() {
		return router;
	}
	
	public void setConnectionRouter(Integer router) {
		if ( this.router != router ) {
			this.router = router;
			for (FDRootListener l : listeners) {
				l.routerChanged(router);
			}
		}
	}
	
	public Point getNextLocation(int x, int y) {
		while ( true ) {
			boolean alreadyExistInLocation = false;
			for ( FDShape item : childElements ) {
				if ( item.getX() == x && item.getY() == y ) {
					alreadyExistInLocation = true;
				}
			}
			
			if ( alreadyExistInLocation ) {
				x+=10;
				y+=10;
			} else {
				break;
			}
		}
		
		return new Point(x, y);
	}

	public void addWire(FDWire wire) {
		Assert.isTrue(!wires.contains(wire));
		
		wires.add(wire);
		
		wire.getSource().addWire(wire);
		
		for (FDRootListener l : listeners) {
			l.wireAdded(wire);
		}
	}

	public void removeWire(FDWire wire) {
		Assert.isTrue(wires.contains(wire));
		
		wires.remove(wire);
		
		wire.getSource().removeWire(wire);
		
		for (FDRootListener l : listeners) {
			l.wireRemoved(wire);
		}
	}
	
	//============================================================
	// FDContainer
	
	@Override
	public void addShape(FDShape target) {
		Assert.isTrue(!childElements.contains(target));

		childElements.add(target);
		target.setParent(this);
		
		for (FDRootListener l : listeners) {
			l.childShapeAdded(target);
		}
	}
	
	@Override
	public void removeShape(FDShape target) {
		Assert.isTrue(childElements.contains(target));
		
		List<FDWire> copiedList = null;
		copiedList = new ArrayList<FDWire>(target.getIncommingWires());
		for ( FDWire wire : copiedList) {
			removeWire(wire);
		}
		
		copiedList = new ArrayList<FDWire>(target.getOutgoingWires());
		for ( FDWire wire : copiedList) {
			removeWire(wire);
		}
		
		childElements.remove(target);
		target.setParent(null);
		
		for (FDRootListener l : listeners) {
			l.childShapeRemoved(target);
		}
	}
	
	@Override
	public int changePosition(int newPosition, FDShape target) {
		Assert.isTrue(childElements.contains(target));
		
		int oldPosition = childElements.indexOf(target);
		
		childElements.remove(target);
		if ( newPosition == - 1) {
			newPosition = childElements.size();
		}
		childElements.add(newPosition, target);
		
		for (FDContainerListener l : listeners) {
			l.positionChanged(newPosition, target);
		}
		
		return oldPosition;
	}
	
	//============================================================
	// Listeners
	
	public void addNodeRootListener(FDRootListener l) {
		listeners.add(l);
	}
	
	public void removeNodeRootListener(FDRootListener l) {
		listeners.remove(l);
	}
}
