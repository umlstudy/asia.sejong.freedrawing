package asia.sejong.freedrawing.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.draw2d.geometry.Rectangle;

import asia.sejong.freedrawing.model.listener.FDNodeListener;

public class FDNode {
	
	private FDNode source;
	private FDNode target;
	
	private int x, y, width, height;

	private Set<FDNodeListener> listeners = new HashSet<FDNodeListener>();
	
	public FDNode() {}
	
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
	
	public FDNode getSource() {
		return source;
	}
	
	public void setSource(FDNode source) {
		FDNode oldSource = this.source;
		this.source = source;

		// notify event
		for ( FDNodeListener l : listeners ) {
			l.sourceChanged(oldSource, source);
		}
	}

	public FDNode getTarget() {
		return target;
	}

	public void setTarget(FDNode target) {
		FDNode oldTarget = this.target;
		this.target = target;
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.targetChanged(oldTarget, target);
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
