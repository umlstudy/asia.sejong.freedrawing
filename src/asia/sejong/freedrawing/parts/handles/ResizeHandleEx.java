package asia.sejong.freedrawing.parts.handles;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.swt.graphics.Cursor;

public class ResizeHandleEx extends SquareHandle {

	private int cursorDirection = 0;

	public ResizeHandleEx(GraphicalEditPart owner, Locator loc, Cursor c) {
		super(owner, loc, c);
	}
	
	public ResizeHandleEx(GraphicalEditPart owner, int direction) {
		setOwner(owner);
		setLocator(new RelativeHandleLocatorEx(owner.getFigure(), direction));
		setCursor(Cursors.getDirectionalCursor(direction, owner.getFigure().isMirrored()));
		cursorDirection = direction;
	}

	protected DragTracker createDragTracker() {
		return new ResizeTracker(getOwner(), cursorDirection);
	}
	
	protected void init() {
		setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE+3, DEFAULT_HANDLE_SIZE+3));
	}
	
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds();
		r.shrink(1, 1);
		try {
			//g.setBackgroundColor(getFillColor());
			g.setBackgroundColor(ColorConstants.green);
			g.fillRectangle(r.x, r.y, r.width, r.height);
			g.setForegroundColor(getBorderColor());
			g.drawRectangle(r.x, r.y, r.width, r.height);
		} finally {
			// We don't really own rect 'r', so fix it.
			r.expand(1, 1);
		}
	}
}
