package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.requests.BendpointRequest;

public class FDWireBendpointDeleteCommand extends FDWireBendpointCommand {
	
	private Point removedPoint;
	
	public FDWireBendpointDeleteCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		removedPoint = removeBendpoint(getLocationIndex());
	}
	
	public void undo() {
		addBendpint(getLocationIndex(), removedPoint);
	}
}
