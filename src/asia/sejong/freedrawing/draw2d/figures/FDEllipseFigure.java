package asia.sejong.freedrawing.draw2d.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class FDEllipseFigure extends FDTextShapeFigureImpl {

	FDEllipseFigure() {
		setPreferredSize(100, 100);
	}
	
	/**
	 * Returns <code>true</code> if the given point (x,y) is contained within
	 * this ellipse.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return <code>true</code>if the given point is contained
	 */
	public boolean containsPoint(int x, int y) {
		if (!super.containsPoint(x, y)) {
			return false;
		} else {
			Rectangle r = getBounds();
			long ux = x - r.x - r.width / 2;
			long uy = y - r.y - r.height / 2;
			return ((ux * ux) << 10) / (r.width * r.width) + ((uy * uy) << 10)
					/ (r.height * r.height) <= 256;
		}
	}

	/**
	 * Fills the ellipse.
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		graphics.fillOval(getOptimizedBounds());
	}

	/**
	 * Outlines the ellipse.
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		graphics.drawOval(getOptimizedBounds());
	}

	private Rectangle getOptimizedBounds() {
		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);

		Rectangle r = Rectangle.SINGLETON.setBounds(getBoundsInZeroPoint());
		r.x += inset1;
		r.y += inset1;
		r.width -= inset1 + inset2;
		r.height -= inset1 + inset2;
		return r;
	}
}
