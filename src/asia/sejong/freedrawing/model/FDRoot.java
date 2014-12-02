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

	private final List<FDRect> childElements = new ArrayList<FDRect>();
	private final List<FDWire> wires = new ArrayList<FDWire>();
	
	private final Collection<FDRootListener> listeners = new HashSet<FDRootListener>();
	
	public FDRoot() {}
	
	public Point getNextLocation(int x, int y) {
		while ( true ) {
			boolean alreadyExistInLocation = false;
			for ( FDRect item : childElements ) {
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
	
	@Override
	public void addNode(FDRect target) {
		Assert.isTrue(!childElements.contains(target));

		childElements.add(target);
		target.setParent(this);
		
		for (FDRootListener l : listeners) {
			l.childNodeAdded(target);
		}
	}
	
	@Override
	public void removeNode(FDRect target) {
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
			l.childNodeRemoved(target);
		}
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
	
	public int changePosition(int newPosition, FDRect target) {
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
	
//	public int changeToFront(FDNode target) {
//		if ( !childElements.contains(target) ) {
//			throw new RuntimeException();
//		}
//		
//		int position = childElements.indexOf(target);
//		
//		childElements.remove(target);
//		childElements.add(target);
//		
//		for (FDContainerListener l : listeners) {
//			l.changeToFront(target);
//		}
//		
//		return position;
//	}
//	
//	public int changeToBack(FDNode target) {
//		if ( !childElements.contains(target) ) {
//			throw new RuntimeException();
//		}
//		int position = childElements.indexOf(target);
//		
//		childElements.remove(target);
//		childElements.add(0, target);
//		
//		for (FDContainerListener l : listeners) {
//			l.changeToBack(target);
//		}
//		
//		return position;
//	}
	
//	public void addConnection(FDConnection child) {
//		childConnections.add(child);
//	}
//	
//	public FDConnection createOrFindConnection(FDNode source, FDNode target) {
//		if ( source == null || source == target ) {
//			return null;
//		}
//		FDConnection fdConnection = new FDConnection();
//		fdConnection.setSource(source);
//		fdConnection.setTarget(target);
//		
//		for ( FDConnection item : childConnections ) {
//			if ( item.equals(fdConnection) ) {
//				return item;
//			}
//		}
//		
//		childConnections.add(fdConnection);
//		
//		return fdConnection;
//	}
//	
//	public FDConnection findConnection(FDNode source, FDNode target) {
//		if ( source == null || source == target ) {
//			return null;
//		}
//		FDConnection fdConnection = new FDConnection();
//		fdConnection.setSource(source);
//		fdConnection.setTarget(target);
//		
//		for ( FDConnection item : childConnections ) {
//			if ( item.equals(fdConnection) ) {
//				return item;
//			}
//		}
//		
//		return null;
//	}
//	
//	public void removeConnection(FDConnection conn) {
//		if ( conn != null ) {
//			childConnections.remove(conn);
//		}
//	}
	
	//============================================================
	// Listeners
	
	public void addNodeRootListener(FDRootListener l) {
		listeners.add(l);
	}
	
	public void removeNodeRootListener(FDRootListener l) {
		listeners.remove(l);
	}
}
