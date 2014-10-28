package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.BendpointRequest;

public class DeleteBendpointCommand extends BendpointCommand {
	
	private Point removedPoint;
	
	public DeleteBendpointCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		removedPoint = removeBendpoint(getLocationIndex());
	}
	
	public void undo() {
		addBendpint(getLocationIndex(), removedPoint);
	}
}
