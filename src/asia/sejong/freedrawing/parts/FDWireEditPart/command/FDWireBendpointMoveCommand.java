package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.BendpointRequest;


public class FDWireBendpointMoveCommand extends FDWireBendpointCommand {
	
	private Point oldPoint;
	
	public FDWireBendpointMoveCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		oldPoint = moveBendpoint(getLocationIndex(), getLocation());
	}

	public void undo() {
		moveBendpoint(getLocationIndex(), oldPoint);
	}
}
