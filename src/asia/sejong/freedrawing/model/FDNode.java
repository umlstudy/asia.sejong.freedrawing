package asia.sejong.freedrawing.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import asia.sejong.freedrawing.model.listener.FDNodeListener;

public class FDNode {
	
	private HashSet<FDNode> sources;
	private HashSet<FDNode> targets;
	private HashMap<FDNode, List<Point>> bandpoints;
	
	private int x, y, width, height;

	private Set<FDNodeListener> listeners = new HashSet<FDNodeListener>();
	
	public FDNode() {
		sources = new HashSet<FDNode>();
		targets = new HashSet<FDNode>();
		bandpoints = new HashMap<FDNode, List<Point>>();
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean setLocation(int newX, int newY) {
		if (x == newX && y == newY)
			return false;
		x = newX;
		y = newY;
		fireLocationChanged(x, y);
		return true;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean setSize(int newWidth, int newHeight) {
		if (width == newWidth && height == newHeight)
			return false;
		width = newWidth;
		height = newHeight;
		fireSizeChanged(width, height);
		return true;
	}
	
	public void setRectangle(Rectangle rect) {
		setLocation(rect.x, rect.y);
		setSize(rect.width, rect.height);
	}
	
	public Set<FDNode> getSources() {
		return new HashSet<FDNode>(sources);
	}
	
	private void addSource(FDNode source) {
		if ( source == null || sources.contains(source) ) {
			// already exist
			throw new RuntimeException();
		}

		sources.add(source);

		// notify event
		for ( FDNodeListener l : listeners ) {
			l.sourceAdded(source);
		}
	}
	
	private void removeSource(FDNode source) {
		if ( source == null || !sources.contains(source) ) {
			// not exist
			throw new RuntimeException();
		}

		sources.remove(source);

		// notify event
		for ( FDNodeListener l : listeners ) {
			l.sourceRemoved(source);
		}
	}

	public Set<FDNode> getTargets() {
		return new HashSet<FDNode>(targets);
	}

	public void addTarget(FDNode target) {
		if ( targets.contains(target) ) {
			// already exist
			return;
		}
		
		target.addSource(this);
		
		targets.add(target);
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.targetAdded(target);
		}
	}
	
	public void removeTarget(FDNode target) {
		if ( !targets.contains(target) ) {
			// not exist
			return;
		}
		
		target.removeSource(this);
		
		targets.remove(target);
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.targetRemoved(target);
		}
	}
	
	public void addFDNodeListener(FDNodeListener listener ) {
		if ( listener != null ) {
			listeners.add(listener);
		}
	}

	public void removeFDNodeListener(FDNodeListener listener ) {
		if ( listener != null ) {
			listeners.remove(listener);
		}
	}
	
	//============================================================
	// FDNode

	protected void fireLocationChanged(int newX, int newY) {
		for (FDNodeListener l : listeners)
			l.locationChanged(newX, newY);
	}

	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (FDNodeListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
