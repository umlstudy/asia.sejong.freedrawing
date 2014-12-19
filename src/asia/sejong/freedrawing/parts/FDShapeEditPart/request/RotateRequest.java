package asia.sejong.freedrawing.parts.FDShapeEditPart.request;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.GroupRequest;

public class RotateRequest extends GroupRequest implements DropRequest {

	public RotateRequest(String type) {
		super(type);
	}

	@Override
	public Point getLocation() {
		return null;
	}
}
