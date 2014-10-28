package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.BendpointRequest;


public class MoveBendpointCommand extends BendpointCommand {
	
	private Point oldPoint;
	
	public MoveBendpointCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		oldPoint = moveBendpoint(getLocationIndex(), getLocation());
	}

	public void undo() {
		moveBendpoint(getLocationIndex(), oldPoint);
	}
}
