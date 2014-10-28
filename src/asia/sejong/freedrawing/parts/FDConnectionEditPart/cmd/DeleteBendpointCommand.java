package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.gef.requests.BendpointRequest;

public class DeleteBendpointCommand extends BendpointCommand {
	
	private Bendpoint removedBendpoint;
	
	public DeleteBendpointCommand(BendpointRequest request) {
		super(request);
	}
	
	public void execute() {
		removedBendpoint = removeBendpoint(getLocationIndex());
	}
	
	public void undo() {
		addBendpint(getLocationIndex(), removedBendpoint.getLocation());
	}
}
