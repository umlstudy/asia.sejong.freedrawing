package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.gef.requests.BendpointRequest;


public class MoveBendpointCommand extends BendpointCommand {
	
	private Bendpoint oldBendpoint;
	
	public MoveBendpointCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		oldBendpoint = replaceBendpoint(getLocationIndex(), getLocation());
	}

	public void undo() {
		replaceBendpoint(getLocationIndex(), oldBendpoint.getLocation());
	}
}
