package asia.sejong.freedrawing.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import asia.sejong.freedrawing.model.listener.FDContainerListener;
import asia.sejong.freedrawing.model.listener.FDNodeRootListener;

public class FDNodeRoot {

	private final Set<FDNode> childElements = new HashSet<FDNode>();
//	private final Set<FDConnection> childConnections = new HashSet<FDConnection>();
	
	private final Collection<FDNodeRootListener> listeners = new HashSet<FDNodeRootListener>();
	
	public FDNodeRoot() {}
	
	public void addNode(FDNode target) {
		childElements.add(target);
		
		for (FDContainerListener l : listeners) {
			l.childNodeAdded(target);
		}
	}
	
	public void removeNode(FDNode target) {
		
		childElements.remove(target);
		
		for (FDContainerListener l : listeners) {
			l.childNodeRemoved(target);
		}
	}
	
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
