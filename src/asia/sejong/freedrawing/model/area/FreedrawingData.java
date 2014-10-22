package asia.sejong.freedrawing.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import asia.sejong.freedrawing.model.area.listener.FDContainerListener;
import asia.sejong.freedrawing.model.area.listener.FreedrawingDataListener;
import asia.sejong.freedrawing.model.connection.AbstractFDConnection;

public class FreedrawingData {

	private final List<AbstractFDElement> childElements = new ArrayList<AbstractFDElement>();
	private final List<AbstractFDConnection> childConnections = new ArrayList<AbstractFDConnection>();
	
	private final Collection<FreedrawingDataListener> listeners = new HashSet<FreedrawingDataListener>();
	
	public FreedrawingData() {}
	
	public void addElement(AbstractFDElement child) {
		childElements.add(child);
		
		for (FDContainerListener l : listeners) {
			l.childElementAdded(child);
		}
	}
	
	public void addConnection(AbstractFDConnection child) {
		childConnections.add(child);
		
		for (FreedrawingDataListener l : listeners) {
			l.childConnectionAdded(child);
		}
	}
	
	//============================================================
	// Listeners
	
	public void addFreedrawingDataListener(FreedrawingDataListener l) {
		listeners.add(l);
	}
	
	public void removeFreedrawingDataListener(FreedrawingDataListener l) {
		listeners.remove(l);
	}
}
