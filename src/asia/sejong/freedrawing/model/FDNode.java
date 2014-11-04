package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDNodeListener;

public class FDNode extends TextObject {
	
	private HashSet<FDNode> sources;
	private HashSet<FDNode> targets;
	private HashMap<FDNode, List<Point>> bendpoints;
	
	private int x, y, width, height;
	
	private String text;

	private Set<FDNodeListener> listeners = new HashSet<FDNodeListener>();
	
	public FDNode() {
		sources = new HashSet<FDNode>();
		targets = new HashSet<FDNode>();
		bendpoints = new HashMap<FDNode, List<Point>>();
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
			throw new RuntimeException("already exist");
		}

		sources.add(source);

		// notify event
		for ( FDNodeListener l : listeners ) {
			l.sourceAdded(source);
		}
	}
	
	private void removeSource(FDNode source) {
		if ( source == null || !sources.contains(source) ) {
			throw new RuntimeException("not exist");
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

	public boolean containsTarget(FDNode target) {
		if ( targets.contains(target) ) {
			return true;
		}
		return false;
	}
	
	public List<Point> getBendpoints(FDNode target) {
		return bendpoints.get(target);
	}

	public void addTarget(FDNode target, List<Point> targetBendpoints) {
		if ( targets.contains(target) ) {
			// already exist
			return;
		}
		
		target.addSource(this);
		
		targets.add(target);
		bendpoints.put(target, targetBendpoints);
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.targetAdded(target, targetBendpoints);
		}
	}
	
	public List<Point> removeTarget(FDNode target) {
		if ( !targets.contains(target) ) {
			// not exist
			return null;
		}
		
		target.removeSource(this);
		
		targets.remove(target);
		
		List<Point> bendpointList = bendpoints.remove(target);
		
		// notify event
		for ( FDNodeListener l : listeners ) {
			l.targetRemoved(target);
		}
		
		return bendpointList;
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
	
	public void addBendpoint(int locationIndex, Point location, FDNode target) {
		List<Point> list = bendpoints.get(target);
		if ( list == null ) {
			list = new ArrayList<Point>();
			bendpoints.put(target, list);
		}
		
		list.add(locationIndex, location);
		
		fireBendpointAdded(locationIndex, location, target);
	}

	public Point removeBendpoint(int locationIndex, FDNode target) {
		List<Point> list = bendpoints.get(target);
		if ( list == null ) {
			throw new RuntimeException();
		}
		
		Point removed = list.remove(locationIndex);
		
		fireBendpointRemoved(locationIndex, target);
		
		return removed;
	}
	
	public Point moveBendpoint(int locationIndex, Point newPoint, FDNode target) {
		List<Point> list = bendpoints.get(target);
		if ( list == null ) {
			throw new RuntimeException();
		}
		
		Point oldPoint = list.set(locationIndex, newPoint);
		
		fireBendpointMoved(locationIndex, newPoint, target);
		
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
	
	protected void fireBendpointAdded(int locationIndex, Point location, FDNode target) {
		for (FDNodeListener l : listeners) {
			l.bendpointAdded(locationIndex, location, target);
		}
	}
	
	protected void fireBendpointRemoved(int locationIndex, FDNode target) {
		for (FDNodeListener l : listeners) {
			l.bendpointRemoved(locationIndex, target);
		}
	}
	
	protected void fireBendpointMoved(int locationIndex, Point newPoint, FDNode target) {
		for (FDNodeListener l : listeners) {
			l.bendpointMoved(locationIndex, newPoint, target);
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
}
