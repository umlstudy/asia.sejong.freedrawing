package asia.sejong.freedrawing.model;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Rectangle;

import asia.sejong.freedrawing.model.listener.FDElementListener;
import asia.sejong.freedrawing.model.listener.FDShapeListener;

public abstract class FDShape extends FDElement {
	
	private ArrayList<FDWire> incommingWires;
	private ArrayList<FDWire> outgoingWires;
	
	private int x, y, width, height;
	
	transient private FDContainer parent;
	
	public FDShape() {
		setOutgoingWires(new ArrayList<FDWire>());
		setIncommingWires(new ArrayList<FDWire>());
	}
	
	public FDContainer getParent() {
		return parent;
	}

	void setParent(FDContainer parent) {
		this.parent = parent;
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
	

	public boolean containsTarget(FDShape target) {
		return getOutgoingWire(target) != null;
	}
	
	private FDWire getOutgoingWire(FDShape target) {
		for ( FDWire wire : getOutgoingWires() ) {
			if ( wire.getTarget().equals(target ) ) {
				return wire;
			}
		}
		return null;
	}
	
	public void addWire(FDWire wire) {
		Assert.isTrue(!containsTarget(wire.getTarget()));
		
		FDShape target = wire.getTarget();
		getOutgoingWires().add(wire);
		target.getIncommingWires().add(wire);
	}
	
	public void removeWire(FDWire wire) {
		Assert.isTrue(containsTarget(wire.getTarget()));
		
		FDShape target = wire.getTarget();
		getOutgoingWires().remove(wire);
		target.getIncommingWires().remove(wire);
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
		
		return removingWire;
	}

	//============================================================
	// Clonable
	
	public FDShape clone() {
		
		FDShape shape = (FDShape)super.clone();
		
		shape.setOutgoingWires(new ArrayList<FDWire>());
		shape.setIncommingWires(new ArrayList<FDWire>());
		
		shape.x = x;
		shape.y = y;
		shape.width = width;
		shape.height = height;
		
		return shape;
	}

	//============================================================
	// FDShapeListener
	
	protected void fireLocationChanged(int newX, int newY) {
		for (FDElementListener l : listeners) {
			((FDShapeListener)l).locationChanged(newX, newY);
		}
	}

	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (FDElementListener l : listeners) {
			((FDShapeListener)l).sizeChanged(newWidth, newHeight);
		}
	}
}