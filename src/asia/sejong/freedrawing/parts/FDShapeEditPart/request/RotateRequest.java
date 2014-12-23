package asia.sejong.freedrawing.parts.FDShapeEditPart.request;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.GroupRequest;

public class RotateRequest extends GroupRequest implements DropRequest {

	private Point moveDelta;
	private Point location;
	
	public RotateRequest(String type) {
		super(type);
	}

	@Override
	public Point getLocation() {
		return location;
	}

	public double getDegree() {
		Rectangle bounds = ((GraphicalEditPart)getEditParts().get(0)).getFigure().getBounds();
		int targetCenterX = bounds.x + (bounds.width>>1);
		int targetCenterY = bounds.y + (bounds.height>>1);
		
		double rslt = 180f * Math.atan2(targetCenterX-location.x, targetCenterY-location.y)/Math.PI; 
		
		return Math.abs(rslt);
	}

	public Point getMoveDelta() {
		return moveDelta;
	}

	public void setMoveDelta(Point moveDelta) {
		this.moveDelta = moveDelta;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
}
