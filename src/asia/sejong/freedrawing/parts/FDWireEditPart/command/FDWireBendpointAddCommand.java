package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import org.eclipse.gef.requests.BendpointRequest;


public class FDWireBendpointAddCommand extends FDWireBendpointCommand {
	
	public FDWireBendpointAddCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		addBendpint(getLocationIndex(), getLocation());
	}

	public void undo() {
		removeBendpoint(getLocationIndex());
	}
}
