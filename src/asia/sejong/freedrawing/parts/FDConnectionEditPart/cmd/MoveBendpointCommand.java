package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.gef.requests.BendpointRequest;


public class MoveBendpointCommand extends BendpointCommand {
	
	private BendpointRequest request;
	private Bendpoint oldBendpoint;
	
	public MoveBendpointCommand(BendpointRequest request) {
		this.request = request;
	}
	
	public void execute() {
		List<Bendpoint> bendpoints = getBendpoints(getConnection(request));
		oldBendpoint = bendpoints.get(request.getIndex());
		AbsoluteBendpoint newBendpoint = new AbsoluteBendpoint(request.getLocation());
		bendpoints.set(request.getIndex(), newBendpoint);	
	}
	
	public void undo() {
		List<Bendpoint> bendpoints = getBendpoints(getConnection(request));
		bendpoints.set(request.getIndex(), oldBendpoint);
	}
}
