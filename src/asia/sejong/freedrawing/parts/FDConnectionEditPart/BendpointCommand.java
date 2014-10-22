package asia.sejong.freedrawing.parts.FDConnectionEditPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;

public abstract class BendpointCommand extends Command {

	protected static List<Bendpoint> getBendpoints(PolylineConnection connection) {
		BendpointConnectionRouter connectionRouter = (BendpointConnectionRouter)connection.getConnectionRouter();
		@SuppressWarnings("unchecked")
		List<Bendpoint> bendpoints = (List<Bendpoint>)connectionRouter.getConstraint(connection);
		if ( bendpoints == null ) {
			bendpoints = new ArrayList<Bendpoint>();
			connectionRouter.setConstraint(connection, bendpoints);
		}
		
		return bendpoints;
	}
	
	protected static PolylineConnection getConnection(BendpointRequest bendpointRequest) {
		return (PolylineConnection)bendpointRequest.getSource().getFigure();
	}
}
