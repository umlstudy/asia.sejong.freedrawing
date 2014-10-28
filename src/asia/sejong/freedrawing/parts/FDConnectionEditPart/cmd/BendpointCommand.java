package asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;

import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;

public abstract class BendpointCommand extends Command {
	
	private FDNode source;
	private FDNode target;
	private EditPartViewer viewer;
	private Point location;
	private int locationIndex;
	
	protected BendpointCommand(BendpointRequest request) {
		this.viewer = request.getSource().getViewer();
		this.source = (FDNode)request.getSource().getSource().getModel();
		this.target = (FDNode)request.getSource().getTarget().getModel();
		this.setLocation(request.getLocation());
		this.setLocationIndex(request.getIndex());
	}

	private List<Bendpoint> getBendpoints() {
		PolylineConnection connection = getConnection();
		BendpointConnectionRouter connectionRouter = (BendpointConnectionRouter)connection.getConnectionRouter();
		@SuppressWarnings("unchecked")
		List<Bendpoint> bendpoints = (List<Bendpoint>)connectionRouter.getConstraint(connection);
		if ( bendpoints == null ) {
			bendpoints = new ArrayList<Bendpoint>();
			connectionRouter.setConstraint(connection, bendpoints);
		}
		
		return bendpoints;
	}
	
	private PolylineConnection getConnection() {
		FDConnection model = FDConnection.newInstance(source, target);
		ConnectionEditPart conx = (ConnectionEditPart) viewer.getEditPartRegistry().get(model);
		return (PolylineConnection)conx.getFigure();
	}
	
	protected void addBendpint(int locationIndex, Point location) {
		List<Bendpoint> bendpoints = getBendpoints();
		AbsoluteBendpoint bp = new AbsoluteBendpoint(location);
		bendpoints.add(locationIndex, bp);
	}
	
	protected Bendpoint removeBendpoint(int locationIndex) {
		List<Bendpoint> bendpoints = getBendpoints();
		return bendpoints.remove(locationIndex);
	}
	
	protected Bendpoint replaceBendpoint(int locationIndex, Point newPoint) {
		Bendpoint oldBendpoint = getBendpoints().get(getLocationIndex());
		AbsoluteBendpoint newBendpoint = new AbsoluteBendpoint(newPoint);
		getBendpoints().set(getLocationIndex(), newBendpoint);	
		
		return oldBendpoint;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(int locationIndex) {
		this.locationIndex = locationIndex;
	}
}
