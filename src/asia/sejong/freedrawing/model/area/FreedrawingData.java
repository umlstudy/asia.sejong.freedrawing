package asia.sejong.freedrawing.model.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import asia.sejong.freedrawing.model.area.listener.FDContainerListener;

public class FreedrawingData {

	private final List<AbstractFDElement> childs = new ArrayList<AbstractFDElement>();
	
	private final Collection<FDContainerListener> listeners = new HashSet<FDContainerListener>();
	
	public FreedrawingData() {}
	
	public void add(AbstractFDElement child) {
		childs.add(child);
		
		for (FDContainerListener l : listeners) {
			l.childAdded(child);
		}
	}
	
	//============================================================
	// Listeners
	
	public void addFreedrawingDataListener(FDContainerListener l) {
		listeners.add(l);
	}
	
	public void removeFreedrawingDataListener(FDContainerListener l) {
		listeners.remove(l);
	}
}
