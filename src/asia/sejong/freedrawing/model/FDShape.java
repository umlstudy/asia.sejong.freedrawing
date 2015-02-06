package asia.sejong.freedrawing.model;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDBaseListener;
import asia.sejong.freedrawing.model.listener.FDShapeListener;

public abstract class FDShape extends FDWireEndPoint {
	
	private static final long serialVersionUID = 8663834668559415982L;

	private int width, height;
	
	private double degree;
	
	private RGB backgroundColor;
	
	private FDContainer parent;
	
	FDShape() {}
	
	public FDContainer getParent() {
		return parent;
	}

	void setParent(FDContainer parent) {
		this.parent = parent;
	}

	public RGB getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(RGB backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		// send event
		fireBackgroundColorChanged(backgroundColor);
	}

//	private int getWidth() {
//		return width;
//	}
//	
//	public int getHeight() {
//		return height;
//	}
	
	public boolean setSize(int newWidth, int newHeight) {
		if (width == newWidth && height == newHeight)
			return false;
		width = newWidth;
		height = newHeight;
		fireSizeChanged(width, height);
		return true;
	}
	
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	public Rectangle getBounds() {
		Point loc = getLocation();
		Dimension size = getSize();
		return new Rectangle(loc.x, loc.y, size.width, size.height);
	}
	
	public void setRectangle(Rectangle rect) {
		setLocation(rect.x, rect.y);
		setSize(rect.width, rect.height);
	}

	public void setDegree(double degree) {
		this.degree = degree;
		
		fireDegreeChanged(degree);
	}
	
	public double getDegree() {
		return degree;
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
		shape.backgroundColor = backgroundColor;
		shape.degree = degree;
		
		return shape;
	}

	//============================================================
	// FDShapeListener
	
	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (FDBaseListener l : listeners) {
			((FDShapeListener)l).sizeChanged(newWidth, newHeight);
		}
	}

	protected void fireBackgroundColorChanged(RGB rgbColor) {
		for (FDBaseListener l : listeners) {
			((FDShapeListener)l).backgroundColorChanged(rgbColor);
		}
	}
	
	protected void fireDegreeChanged(double degree) {
		for (FDBaseListener l : listeners) {
			((FDShapeListener)l).degreeChanged(degree);
		}
	}
}