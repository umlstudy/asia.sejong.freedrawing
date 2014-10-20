package asia.sejong.freedrawing.parts.connection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.requests.BendpointRequest;

import asia.sejong.freedrawing.model.connection.FDBendpointConnection;

public class FDBendpointConnectionEditPart extends AbstractFDConnectionEditPart {

	public FDBendpointConnectionEditPart(FDBendpointConnection connection) {
		super(connection);
	}

	protected IFigure createFigure() {
		PolylineConnection connection = new PolylineConnection();
		connection.setConnectionRouter(new BendpointConnectionRouter());
		connection.setLineWidth(3);
		connection.setAntialias(1);

		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.darkGray);
		connection.setTargetDecoration(decoration);

		return connection;
	}
	

	public PolylineConnection getConnection() {
		return (PolylineConnection)getFigure();
	}
	
	public BendpointConnectionRouter getConnectionRouter() {
		return (BendpointConnectionRouter)getConnection().getConnectionRouter();
	}
	
	private void addBendpoint(BendpointRequest bendpointRequest) {
		BendpointConnectionRouter connectionRouter = getConnectionRouter();
		@SuppressWarnings("unchecked")
		List<Bendpoint> bendpoints = (List<Bendpoint>)connectionRouter.getConstraint(getConnection());
		if ( bendpoints == null ) {
			bendpoints = new ArrayList<Bendpoint>();
			connectionRouter.setConstraint(getConnection(), bendpoints);
		}
		
		AbsoluteBendpoint bp = new AbsoluteBendpoint(bendpointRequest.getLocation());
		bendpoints.add(bendpointRequest.getIndex(), bp);
//		connectionRouter.invalidate(getConnection());
	}

	@Override
	protected void createEditPolicies() {
		
	}

}
