package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import org.eclipse.gef.requests.BendpointRequest;


public class AddBendpointCommand extends BendpointCommand {
	
	public AddBendpointCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		addBendpint(getLocationIndex(), getLocation());
	}

	public void undo() {
		removeBendpoint(getLocationIndex());
	}
}
