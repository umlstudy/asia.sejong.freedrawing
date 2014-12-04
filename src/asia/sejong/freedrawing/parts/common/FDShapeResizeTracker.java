package asia.sejong.freedrawing.parts.common;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;

public final class FDShapeResizeTracker extends ResizeTracker {

	public FDShapeResizeTracker(GraphicalEditPart owner, int direction) {
		super(owner, direction);
	}

	protected Dimension getMaximumSizeFor(ChangeBoundsRequest request) {
		// TODO
		return IFigure.MAX_DIMENSION;
	}

	protected Dimension getMinimumSizeFor(ChangeBoundsRequest request) {
		return IFigure.MIN_DIMENSION;
		//return LogicPlugin.getMinimumSizeFor(getOwner().getModel().getClass());
	}
}
