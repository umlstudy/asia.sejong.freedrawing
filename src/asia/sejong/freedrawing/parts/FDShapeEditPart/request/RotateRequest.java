package asia.sejong.freedrawing.parts.FDShapeEditPart.request;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import asia.sejong.freedrawing.draw2d.figures.GeometryUtil;

public class RotateRequest extends ChangeBoundsRequest {

	public RotateRequest(String req) {
		super(req);
	}

	public double getDegree() {
		
		Rectangle bounds = ((GraphicalEditPart)getEditParts().get(0)).getFigure().getBounds();
		Point centerPoint = GeometryUtil.centerPoint(bounds);
		Point mouseLocation = getLocation();
		
		return GeometryUtil.calculateDegree(centerPoint.x, centerPoint.y, mouseLocation.x, mouseLocation.y);
	}
}
