package asia.sejong.freedrawing.parts.FDWireEditPart.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;

import asia.sejong.freedrawing.model.FDWire;

public abstract class FDWireBendpointCommand extends Command {
	
//	private FDRect source;
//	private FDRect target;
//	private EditPartViewer viewer;
	private Point location;
	private int locationIndex;
	private FDWire wire;
	
	protected FDWireBendpointCommand(BendpointRequest request) {
//		this.viewer = request.getSource().getViewer();
//		this.source = (FDRect)request.getSource().getSource().getModel();
//		this.target = (FDRect)request.getSource().getTarget().getModel();
		this.wire = (FDWire)request.getSource().getModel();
		
		this.setLocation(request.getLocation());
		this.setLocationIndex(request.getIndex());
	}

//	private List<Bendpoint> getBendpoints() {
//		PolylineConnection connection = getConnection();
//		BendpointConnectionRouter connectionRouter = (BendpointConnectionRouter)connection.getConnectionRouter();
//		@SuppressWarnings("unchecked")
//		List<Bendpoint> bendpoints = (List<Bendpoint>)connectionRouter.getConstraint(connection);
//		if ( bendpoints == null ) {
//			bendpoints = new ArrayList<Bendpoint>();
//			connectionRouter.setConstraint(connection, bendpoints);
//		}
//		
//		return bendpoints;
//	}
//	
//	private PolylineConnection getConnection() {
//		FDConnection model = FDConnection.newInstance(source, target);
//		ConnectionEditPart conx = (ConnectionEditPart) viewer.getEditPartRegistry().get(model);
//		return (PolylineConnection)conx.getFigure();
//	}
	
	protected void addBendpoint(int locationIndex, Point location) {
		wire.addBendpoint(locationIndex, location);
		
		
//		List<Bendpoint> bendpoints = getBendpoints();
//		AbsoluteBendpoint bp = new AbsoluteBendpoint(location);
//		bendpoints.add(locationIndex, bp);
	}
	
	protected Point removeBendpoint(int locationIndex) {
		
		Point removedPoint = wire.removeBendpoint(locationIndex);
		return removedPoint;
//		List<Bendpoint> bendpoints = getBendpoints();
//		return bendpoints.remove(locationIndex);
	}
	
	protected Point moveBendpoint(int locationIndex, Point newPoint) {
		Point oldPoint = wire.moveBendpoint(locationIndex, newPoint);
		return oldPoint;
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
