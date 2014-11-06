package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.listener.FDContainerListener;
import asia.sejong.freedrawing.model.listener.FDNodeRootListener;

public class FDNodeRoot implements FDContainer {

	private final List<FDNode> childElements = new ArrayList<FDNode>();
//	private final Set<FDConnection> childConnections = new HashSet<FDConnection>();
	
	private final Collection<FDNodeRootListener> listeners = new HashSet<FDNodeRootListener>();
	
	public FDNodeRoot() {}
	
	public Point getNextLocation(int x, int y) {
		while ( true ) {
			boolean alreadyExistInLocation = false;
			for ( FDNode item : childElements ) {
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
	public void addNode(FDNode target) {
		if ( childElements.contains(target) ) {
			throw new RuntimeException();
		}
		childElements.add(target);
		target.setParent(this);
		
		for (FDContainerListener l : listeners) {
			l.childNodeAdded(target);
		}
	}
	
	@Override
	public void removeNode(FDNode target) {
		if ( !childElements.contains(target) ) {
			throw new RuntimeException();
		}
		childElements.remove(target);
		target.setParent(null);
		
		for (FDContainerListener l : listeners) {
			l.childNodeRemoved(target);
		}
	}
	
	public int changePosition(int newPosition, FDNode target) {
		if ( !childElements.contains(target) ) {
			throw new RuntimeException();
		}
		
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
	
	public void addNodeRootListener(FDNodeRootListener l) {
		listeners.add(l);
	}
	
	public void removeNodeRootListener(FDNodeRootListener l) {
		listeners.remove(l);
	}
}
