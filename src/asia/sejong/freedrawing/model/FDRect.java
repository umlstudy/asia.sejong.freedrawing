package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDNodeListener;

public class FDRect extends FDTextShape {
	
	private HashSet<FDRect> sources;
//	private HashSet<FDRect> targets;
	private HashMap<FDRect, FDWire> targets;
	
	private int x, y, width, height;
	
	private String text;

	transient private Set<FDNodeListener> listeners = new HashSet<FDNodeListener>();
	transient private FDContainer parent;
	
	public FDRect() {
		sources     = new HashSet<FDRect>();
//		targets     = new HashSet<FDRect>();
		targets = new HashMap<FDRect, FDWire>();
	}
	
	public FDContainer getParent() {
		return parent;
	}

	public List<FDWire> getWires() {
		return new ArrayList<FDWire>(targets.values());
	}

	void setParent(FDContainer parent) {
		this.parent = parent;
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
	
	public Set<FDRect> getSources() {
		return new HashSet<FDRect>(sources);
	}
	
	private void addSource(FDRect source) {
		if ( source == null || sources.contains(source) ) {
			throw new RuntimeException("already exist");
		}

		sources.add(source);
//
//		// notify event
//		for ( FDNodeListener l : listeners ) {
//			l.sourceAdded(source);
//		}
	}
	
	private void removeSource(FDRect source) {
		if ( source == null || !sources.contains(source) ) {
			throw new RuntimeException("not exist");
		}

		sources.remove(source);
//
//		// notify event
//		for ( FDNodeListener l : listeners ) {
//			l.sourceRemoved(source);
//		}
	}

//	public Set<FDRect> getTargets() {
//		return new HashSet<FDRect>(targets);
//	}

	public boolean containsTarget(FDRect target) {
		if ( targets.containsKey(target) ) {
			return true;
		}
		return false;
	}
	
	public FDWire getWire(FDRect target) {
		return targets.get(target);
	}
	
	public Map<? extends FDRect, ? extends FDWire> getTargets() {
		return Collections.unmodifiableMap(targets);
	}

	public void addTarget(FDRect target, FDWire wire) {
		if ( targets.containsKey(target) ) {
			// already exist
			return;
		}
		
		target.addSource(this);
		
		targets.put(target, wire);
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.targetAdded(target);
		}
	}
	
	public FDWire removeTarget(FDRect target) {
		if ( !targets.containsKey(target) ) {
			// not exist
			return null;
		}
		
		target.removeSource(this);
		
		FDWire removedWire = targets.remove(target);
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.targetRemoved(target, removedWire);
		}
		
		return removedWire;
	}

	public String getText() {
		return text;
	}

	public boolean setText(String newText) {
		if (newText == null) {
			newText = "";
		}
		if (newText.equals(text)) {
			return false;
		}
		
		text = newText;
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.textChanged(text);
		}
		
		return true;
	}
	
	public void addBendpoint(int locationIndex, Point location, FDRect target) {
		FDWire wire = targets.get(target);
		
		wire.add(locationIndex, location);
	}

	public Point removeBendpoint(int locationIndex, FDRect target) {
		FDWire wire = targets.get(target);
		
		Point removed = wire.remove(locationIndex);
		
		return removed;
	}
	
	public Point moveBendpoint(int locationIndex, Point newPoint, FDRect target) {
		FDWire wire = targets.get(target);
		
		Point oldPoint = wire.set(locationIndex, newPoint);

		return oldPoint;
	}

	//============================================================
	// Listener add and remove
	
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
		for (FDNodeListener l : listeners) {
			l.locationChanged(newX, newY);
		}
	}

	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (FDNodeListener l : listeners) {
			l.sizeChanged(newWidth, newHeight);
		}
	}
	
	@Override
	protected void fireBorderColorChanged(RGB rgbColor) {
		for (FDNodeListener l : listeners) {
			l.borderColorChanged(rgbColor);
		}
	}

	@Override
	protected void fireFontChanged(FontInfo fontInfo) {
		for (FDNodeListener l : listeners) {
			l.fontChanged(fontInfo);
		}
	}
	
	public FDRect clone() {
		
		FDRect node = (FDRect)super.clone();
		
		node.sources = new HashSet<FDRect>();
//		node.targets = new HashSet<FDRect>();
		node.targets = new HashMap<FDRect, FDWire>();
		
		node.x = x;
		node.y = y;
		node.width = width;
		node.height = height;
		node.text = text;
		
		return node;
	}
}
