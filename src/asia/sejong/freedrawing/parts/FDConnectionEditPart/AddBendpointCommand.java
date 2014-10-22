package asia.sejong.freedrawing.parts.FDConnectionEditPart;

import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.gef.requests.BendpointRequest;


public class AddBendpointCommand extends BendpointCommand {
	
	private BendpointRequest request;
	
	public AddBendpointCommand(BendpointRequest request) {
		this.request = request;
	}
	
	public void execute() {
		List<Bendpoint> bendpoints = getBendpoints(getConnection(request));
		AbsoluteBendpoint bp = new AbsoluteBendpoint(request.getLocation());
		bendpoints.add(request.getIndex(), bp);
	}
}
