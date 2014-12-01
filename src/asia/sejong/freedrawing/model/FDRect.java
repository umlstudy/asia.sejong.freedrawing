package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDNodeListener;

public class FDRect extends FDTextShape {
	
	private ArrayList<FDWire> incommingWires;
	private ArrayList<FDWire> outgoingWires;
//	private HashSet<FDRect> targets;
	
	private int x, y, width, height;
	
	private String text;

	transient private Set<FDNodeListener> listeners = new HashSet<FDNodeListener>();
	transient private FDContainer parent;
	
	public FDRect() {
		setOutgoingWires(new ArrayList<FDWire>());
		setIncommingWires(new ArrayList<FDWire>());
//		targets     = new HashSet<FDRect>();
//		targets = new HashMap<FDRect, FDWire>();
	}

	public ArrayList<FDWire> getIncommingWires() {
		return incommingWires;
	}

	public void setIncommingWires(ArrayList<FDWire> incommingWires) {
		this.incommingWires = incommingWires;
	}

	public ArrayList<FDWire> getOutgoingWires() {
		return outgoingWires;
	}

	public void setOutgoingWires(ArrayList<FDWire> outgoingWires) {
		this.outgoingWires = outgoingWires;
	}
	
	public FDContainer getParent() {
		return parent;
	}

//	public Set<FDWire> getOutgoingWires() {
//		return outgoingWires;
//	}

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
//	
//	public Set<FDRect> getSources() {
//		HashSet<FDRect> rslt = new HashSet<FDRect>();
//		for ( FDWire wire : wires ) {
//			rslt.add(wire.getSource());
//		}
//		
//		return new HashSet<FDRect>(sources);
//	}
//	
//	private void addSource(FDRect source) {
//		if ( source == null || sources.contains(source) ) {
//			throw new RuntimeException("already exist");
//		}
//
//		sources.add(source);
////
////		// notify event
////		for ( FDNodeListener l : listeners ) {
////			l.sourceAdded(source);
////		}
//	}
//	
//	private void removeSource(FDRect source) {
//		if ( source == null || !sources.contains(source) ) {
//			throw new RuntimeException("not exist");
//		}
//
//		sources.remove(source);
////
////		// notify event
////		for ( FDNodeListener l : listeners ) {
////			l.sourceRemoved(source);
////		}
//	}

//	public Set<FDRect> getTargets() {
//		return new HashSet<FDRect>(targets);
//	}

	public boolean containsTarget(FDRect target) {
		return getOutgoingWire(target) != null;
	}
	
	private FDWire getOutgoingWire(FDRect target) {
		for ( FDWire wire : getOutgoingWires() ) {
			if ( wire.getTarget().equals(target ) ) {
				return wire;
			}
		}
		return null;
	}
	
//	public Map<? extends FDRect, ? extends FDWire> getTargets() {
//		return Collections.unmodifiableMap(targets);
//	}

	public void addWire(FDWire wire) {
		Assert.isTrue(!containsTarget(wire.getTarget()));
		
		FDRect target = wire.getTarget();
		getOutgoingWires().add(wire);
		target.getIncommingWires().add(wire);
		
//		// notify event
//		for ( FDNodeListener l : listeners ) {
//			l.wireAdded(wire);
//		}
	}
	
	public void removeWire(FDWire wire) {
		Assert.isTrue(containsTarget(wire.getTarget()));
		
		FDRect target = wire.getTarget();
		getOutgoingWires().remove(wire);
		target.getIncommingWires().remove(wire);
		
//		// notify event
//		for ( FDNodeListener l : listeners ) {
//			l.wireAdded(wire);
//		}
	}
	
	public FDWire removeTarget(FDRect target) {
		
		FDWire removingWire = null;
		for ( FDWire wire : getOutgoingWires() ) {
			if ( wire.getSource().equals(this) && wire.getTarget().equals(target) ) {
				removingWire = wire;
				break;
			}
		}
		
		if ( removingWire == null ) {
			// not exist
			return null;
		}
		
		getOutgoingWires().remove(removingWire);
		target.getIncommingWires().remove(removingWire);
		
//		// notify event
//		for ( FDNodeListener l : listeners ) {
//			l.wireRemoved(removingWire);
//		}
		
		return removingWire;
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
	
//	public void addBendpoint(int locationIndex, Point location, FDRect target) {
//		FDWire wire = targets.get(target);
//		
//		wire.add(locationIndex, location);
//	}
//
//	public Point removeBendpoint(int locationIndex, FDRect target) {
//		FDWire wire = targets.get(target);
//		
//		Point removed = wire.remove(locationIndex);
//		
//		return removed;
//	}
//	
//	public Point moveBendpoint(int locationIndex, Point newPoint, FDRect target) {
//		FDWire wire = targets.get(target);
//		
//		Point oldPoint = wire.set(locationIndex, newPoint);
//
//		return oldPoint;
//	}

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
		
//		node.sources = new HashSet<FDRect>();
////		node.targets = new HashSet<FDRect>();
//		node.targets = new HashMap<FDRect, FDWire>();
		node.setOutgoingWires(new ArrayList<FDWire>());
		node.setIncommingWires(new ArrayList<FDWire>());
		
		node.x = x;
		node.y = y;
		node.width = width;
		node.height = height;
		node.text = text;
		
		return node;
	}
}
