package asia.sejong.freedrawing.parts.common;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;

import asia.sejong.freedrawing.parts.FDShapeEditPart.request.RotateRequest;

public final class FDShapeRotateTracker extends ResizeTracker {

	public static String REQ_ROTATE = "rotate"; //$NON-NLS-1$
	public static String REQ_ROTATE_CHILD = "rotate_child"; //$NON-NLS-1$

	public FDShapeRotateTracker(GraphicalEditPart owner, int direction) {
		super(owner, direction);
	}

	protected Dimension getMaximumSizeFor(ChangeBoundsRequest request) {
		// TODO
		return IFigure.MAX_DIMENSION;
	}
	
	protected String getCommandName() {
		return REQ_ROTATE;
	}
	
	protected Request createSourceRequest() {
		ChangeBoundsRequest request;
		request = new RotateRequest(REQ_ROTATE);
		request.setResizeDirection(getResizeDirection());
		return request;
	}

	protected Dimension getMinimumSizeFor(ChangeBoundsRequest request) {
		return IFigure.MIN_DIMENSION;
		//return LogicPlugin.getMinimumSizeFor(getOwner().getModel().getClass());
	}
}
