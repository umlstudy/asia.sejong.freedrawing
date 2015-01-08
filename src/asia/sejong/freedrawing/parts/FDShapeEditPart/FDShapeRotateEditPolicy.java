package asia.sejong.freedrawing.parts.FDShapeEditPart;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import asia.sejong.freedrawing.parts.FDShapeEditPart.request.RotateRequest;
import asia.sejong.freedrawing.parts.common.FDShapeRotateTracker;

public class FDShapeRotateEditPolicy extends ResizableEditPolicy {

	@Override
	public Command getCommand(Request request) {
		if (FDShapeRotateTracker.REQ_ROTATE.equals(request.getType())) {
			return getRotateCommand((ChangeBoundsRequest) request);
		}
		return super.getCommand(request);
	}

	@Override
	public boolean understandsRequest(Request request) {
		if (FDShapeRotateTracker.REQ_ROTATE.equals(request.getType())) {
			return true;
		}
		return super.understandsRequest(request);
	}
	
	protected Command getRotateCommand(ChangeBoundsRequest request) {
		RotateRequest req = new RotateRequest(FDShapeRotateTracker.REQ_ROTATE_CHILD);
		req.setEditParts(getHost());
		req.setCenteredResize(request.isCenteredResize());
		req.setConstrainedMove(request.isConstrainedMove());
		req.setConstrainedResize(request.isConstrainedResize());
		req.setSnapToEnabled(request.isSnapToEnabled());
		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setExtendedData(request.getExtendedData());
		req.setResizeDirection(request.getResizeDirection());
		return getHost().getParent().getCommand(req);
	}
}
