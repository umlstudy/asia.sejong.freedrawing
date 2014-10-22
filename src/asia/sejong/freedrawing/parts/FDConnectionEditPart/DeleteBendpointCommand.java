package asia.sejong.freedrawing.parts.FDConnectionEditPart;

import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.gef.requests.BendpointRequest;

public class DeleteBendpointCommand extends BendpointCommand {
	
	private BendpointRequest request;
	
	public DeleteBendpointCommand(BendpointRequest request) {
		this.request = request;
	}
	
	public void execute() {
		List<Bendpoint> bendpoints = getBendpoints(getConnection(request));
		bendpoints.remove(request.getIndex());
	}
}
