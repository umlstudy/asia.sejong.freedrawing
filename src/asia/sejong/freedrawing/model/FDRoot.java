package asia.sejong.freedrawing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.listener.FDBaseListener;
import asia.sejong.freedrawing.model.listener.FDContainerListener;
import asia.sejong.freedrawing.model.listener.FDRootListener;

public class FDRoot extends FDBase implements FDContainer, Serializable {
	
	private static final long serialVersionUID = -260607307684628124L;
	
	public static Integer ROUTER_MANUAL = new Integer(0);
	public static Integer ROUTER_SHORTEST_PATH = new Integer(12);
	
	private final List<FDShape> childElements = new ArrayList<FDShape>();
	private final List<FDWire> wires = new ArrayList<FDWire>();
	private Integer router = ROUTER_MANUAL;
	
	public FDRoot() {}

	public Integer getConnectionRouter() {
		return router;
	}
	
	public List<FDShape> getChildren() {
		return childElements;
	}
	
	public List<FDWire> getOutgoingWires() {
		List<FDWire> outgoingWires = new ArrayList<FDWire>();
		for ( FDWire wire : wires ) {
			if ( wire.getSource().getClass() == FDWireEndPoint.class ) {
				outgoingWires.add(wire);
			}
		}
		return outgoingWires;
	}

	public List<FDWire> getIncommingWires() {
		List<FDWire> incommingWires = new ArrayList<FDWire>();
		for ( FDWire wire : wires ) {
			if ( wire.getTarget().getClass() == FDWireEndPoint.class ) {
				incommingWires.add(wire);
			}
		}
		return incommingWires;
	}
	
	public void setConnectionRouter(Integer router) {
		if ( this.router != router ) {
			this.router = router;
			for (FDBaseListener l : listeners) {
				((FDRootListener)l).routerChanged(router);
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
		
		for (FDBaseListener l : listeners) {
			((FDRootListener)l).wireAdded(wire);
		}
	}

	public void removeWire(FDWire wire) {
		Assert.isTrue(wires.contains(wire));
		
		wires.remove(wire);
		
		wire.getSource().removeWire(wire);
		
		for (FDBaseListener l : listeners) {
			((FDRootListener)l).wireRemoved(wire);
		}
	}
	
	//============================================================
	// FDContainer
	
	@Override
	public void addShape(FDShape target) {
		Assert.isTrue(!childElements.contains(target));

		childElements.add(target);
		target.setParent(this);
		
		for (FDBaseListener l : listeners) {
			((FDRootListener)l).childShapeAdded(target);
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
		
		for (FDBaseListener l : listeners) {
			((FDRootListener)l).childShapeRemoved(target);
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
		
		for (FDBaseListener l : listeners) {
			((FDContainerListener)l).positionChanged(newPosition, target);
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
