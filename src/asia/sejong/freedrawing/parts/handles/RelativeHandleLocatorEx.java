package asia.sejong.freedrawing.parts.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

public class RelativeHandleLocatorEx implements Locator {

	public static final int ROTATION = 256;
	
	private double relativeX;
	private double relativeY;
	private IFigure reference;

	public RelativeHandleLocatorEx() {
		relativeX = 0.0;
		relativeY = 0.0;
	}

	public RelativeHandleLocatorEx(IFigure reference, int location) {
		setReferenceFigure(reference);
		switch (location & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.NORTH:
			relativeY = 0;
			break;
		case PositionConstants.SOUTH:
			relativeY = 1.0;
			break;
		default:
			relativeY = 0.5;
		}
		
		if ( ( location & ROTATION ) > 0 ) {
			relativeY = -0.2;
		}

		switch (location & PositionConstants.EAST_WEST) {
		case PositionConstants.WEST:
			relativeX = 0;
			break;
		case PositionConstants.EAST:
			relativeX = 1.0;
			break;
		default:
			relativeX = 0.5;
		}
	}

	public RelativeHandleLocatorEx(IFigure reference, double relativeX, double relativeY) {
		setReferenceFigure(reference);
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	protected Rectangle getReferenceBox() {
		IFigure f = getReferenceFigure();
		if (f instanceof HandleBounds) {
			return ((HandleBounds) f).getHandleBounds();
		}
		return getReferenceFigure().getBounds();
	}

	protected IFigure getReferenceFigure() {
		return reference;
	}

	public void relocate(IFigure target) {
		IFigure reference = getReferenceFigure();
		Rectangle targetBounds = new PrecisionRectangle(getReferenceBox().getResized(-1, -1));
		reference.translateToAbsolute(targetBounds);
		target.translateToRelative(targetBounds);
		targetBounds.resize(1, 1);

		Dimension targetSize = target.getPreferredSize();

		targetBounds.x += (int) (targetBounds.width * relativeX - ((targetSize.width + 1) / 2));
		targetBounds.y += (int) (targetBounds.height * relativeY - ((targetSize.height + 1) / 2));
		targetBounds.setSize(targetSize);
		target.setBounds(targetBounds);
	}

	public void setReferenceFigure(IFigure reference) {
		this.reference = reference;
	}

}