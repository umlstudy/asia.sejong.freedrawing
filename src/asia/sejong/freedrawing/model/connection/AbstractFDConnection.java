package asia.sejong.freedrawing.model.connection;

import java.util.HashSet;
import java.util.Set;

import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.model.connection.listener.FDConnectionListener;

public abstract class AbstractFDConnection {

	private AbstractFDElement source;
	private AbstractFDElement target;
	
	private Set<FDConnectionListener> listeners = new HashSet<FDConnectionListener>();

	public AbstractFDElement getSource() {
		return source;
	}

	public void setSource(AbstractFDElement source) {
		this.source = source;

		// notify event
		for ( FDConnectionListener l : listeners ) {
			l.sourceChanged(source);
		}
	}

	public AbstractFDElement getTarget() {
		return target;
	}

	public void setTarget(AbstractFDElement target) {
		this.target = target;
		
		// notify event
		for ( FDConnectionListener l : listeners ) {
			l.targetChanged(target);
		}
	}
	
	public void addFDConnectionListener(FDConnectionListener listener ) {
		if ( listener != null ) {
			listeners.add(listener);
		}
	}

	public void removeFDConnectionListener(FDConnectionListener listener ) {
		if ( listener != null ) {
			listeners.remove(listener);
		}
	}

}
