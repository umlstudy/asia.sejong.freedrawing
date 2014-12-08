package asia.sejong.freedrawing.model;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Rectangle;

import asia.sejong.freedrawing.model.listener.FDElementListener;
import asia.sejong.freedrawing.model.listener.FDShapeListener;

public abstract class FDShape extends FDWireEndPoint {
	
	private int width, height;
	
	transient private FDContainer parent;
	
	public FDContainer getParent() {
		return parent;
	}

	void setParent(FDContainer parent) {
		this.parent = parent;
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

	//============================================================
	// Cloneable
	
	@Override
	public FDShape clone() {
		
		FDShape shape = (FDShape)super.clone();
		
		shape.setOutgoingWires(new ArrayList<FDWire>());
		shape.setIncommingWires(new ArrayList<FDWire>());
		
		shape.width = width;
		shape.height = height;
		
		return shape;
	}

	//============================================================
	// FDShapeListener
	
	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (FDElementListener l : listeners) {
			((FDShapeListener)l).sizeChanged(newWidth, newHeight);
		}
	}
}