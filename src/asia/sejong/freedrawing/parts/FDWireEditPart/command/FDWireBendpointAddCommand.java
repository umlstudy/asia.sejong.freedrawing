package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import org.eclipse.gef.requests.BendpointRequest;


public class FDWireBendpointAddCommand extends FDWireBendpointCommand {
	
	public FDWireBendpointAddCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		addBendpoint(getLocationIndex(), getLocation());
	}

	public void undo() {
		removeBendpoint(getLocationIndex());
	}
}
