package asia.sejong.freedrawing.model.area;

import org.eclipse.draw2d.geometry.Rectangle;

public abstract class AbstractFDElement {
	private int x, y, width, height;

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
//		fireLocationChanged(x, y);
		return true;
	}

	//protected abstract void fireLocationChanged(int newX, int newY);
	
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
//		fireSizeChanged(width, height);
		return true;
	}
	
	public void setRectangle(Rectangle rect) {
		setLocation(rect.x, rect.y);
		setSize(rect.width, rect.height);
	}
	
//	protected abstract void fireSizeChanged(int newWidth, int newHeight);
}
