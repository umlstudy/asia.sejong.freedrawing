package asia.sejong.freedrawing.parts.FDShapeEditPart.request;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class RotateRequest extends ChangeBoundsRequest {

	public RotateRequest(String req) {
		super(req);
	}

	public double getDegree() {
		
		Point location = getLocation();
		
		Rectangle bounds = ((GraphicalEditPart)getEditParts().get(0)).getFigure().getBounds();
		int targetCenterX = bounds.x + (bounds.width>>1);
		int targetCenterY = bounds.y + (bounds.height>>1);
		
		double rslt = 180f * Math.atan2(targetCenterX-location.x, targetCenterY-location.y)/Math.PI; 
		
		return Math.abs(rslt);
	}
	
}
