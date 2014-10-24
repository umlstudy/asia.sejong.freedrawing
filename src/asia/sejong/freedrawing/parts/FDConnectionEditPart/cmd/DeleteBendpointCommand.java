package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.gef.requests.BendpointRequest;

public class DeleteBendpointCommand extends BendpointCommand {
	
	private BendpointRequest request;
	private Bendpoint removedBendpoint;
	
	public DeleteBendpointCommand(BendpointRequest request) {
		this.request = request;
	}
	
	public void execute() {
		List<Bendpoint> bendpoints = getBendpoints(getConnection(request));
		removedBendpoint = bendpoints.remove(request.getIndex());
	}
	
	public void undo() {
		List<Bendpoint> bendpoints = getBendpoints(getConnection(request));
		bendpoints.add(request.getIndex(), removedBendpoint);
	}
}
