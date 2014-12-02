package asia.sejong.freedrawing.parts.FDWireEditPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.debug.ForEditPart;
import asia.sejong.freedrawing.figures.FDWireFigure;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.listener.FDWireListener;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.FDWireDeleteCommand;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDWireEditPart extends AbstractConnectionEditPart implements FDWireListener {

	protected static final PointList ARROWHEAD = new PointList(new int[]{
			0, 0, -2, 2, -2, 0, -2, -2, 0, 0
		});

	public FDWireEditPart(FDWire connection) {
		setModel(connection);
	}
	
	@Override
	protected IFigure createFigure() {
		return createWireFigure(true);
	}

	@Override
	public FDWire getModel() {
		return (FDWire) super.getModel();
	}
	
	@Override
	public FDWireFigure getFigure() {
		return (FDWireFigure)super.getFigure();
	}
	
	protected PolylineConnection getConnection() {
		return (PolylineConnection)getFigure();
	}
	
	public static FDWireFigure createWireFigure(boolean custom) {
		FDWireFigure wireFigure = null;
		if ( custom ) {
			wireFigure = new FDWireFigure();
			wireFigure.setLineWidth(3);
			wireFigure.setAntialias(1);
		} else {
			wireFigure = new FDWireFigure();
			wireFigure.setLineStyle(SWT.LINE_DASH);
			wireFigure.setAlpha(180);
			wireFigure.setBackgroundColor(ColorConstants.blue);
			wireFigure.setLineWidth(4);
			wireFigure.setAntialias(5);
		}

		wireFigure.setConnectionRouter(new BendpointConnectionRouter());

		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.darkGray);
		wireFigure.setTargetDecoration(decoration);

		return wireFigure;
	}
	
	private final FDRoot getRootModel() {
		return (FDRoot)getViewer().getContents().getModel();
	}

	@Override
	protected ConnectionAnchor getSourceConnectionAnchor() {
		return super.getSourceConnectionAnchor();
	}

	@Override
	protected ConnectionAnchor getTargetConnectionAnchor() {
		return super.getTargetConnectionAnchor();
	}
	

	@Override
	protected void createEditPolicies() {
		ConnectionEndpointEditPolicy selectionPolicy = new ConnectionEndpointEditPolicy();
		
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);

		// Handles deleting the receiver
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy() {
			@Override
			protected Command getDeleteCommand(GroupRequest request) {
				return new FDWireDeleteCommand(getRootModel(), getModel());
			}
		});
		
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new FDWireBendpointEditPolicy());
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
	
	public void connectTarget(FDWire targetWire) {
		PolylineConnection connection = getConnection();
		BendpointConnectionRouter connectionRouter = (BendpointConnectionRouter)connection.getConnectionRouter();
		List<Bendpoint> bendpoints = new ArrayList<Bendpoint>();
		if ( targetWire != null ) {
			for ( Point point : targetWire.getBendpoints()  ) {
				bendpoints.add(new AbsoluteBendpoint(point));
			}
			connectionRouter.setConstraint(connection, bendpoints);
		}
	}
	
	//============================================================
	// For Debug
	
	@Override
	public Command getCommand(Request request) {
		Command command = super.getCommand(request);
		ForEditPart.traceRequest(this, request, command);
		return command;
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
	}
	
	//============================================================
	// FDWireListener
	
	@Override
	public void bendpointAdded(int locationIndex, Point location) {
		List<Bendpoint> bendpoints = getBendpoints();
		AbsoluteBendpoint bp = new AbsoluteBendpoint(location);
		bendpoints.add(locationIndex, bp);
		
		refresh();
	}

	@Override
	public void bendpointRemoved(int locationIndex) {
		List<Bendpoint> bendpoints = getBendpoints();
		bendpoints.remove(locationIndex);

		refresh();
	}
	
	@Override
	public void bendpointMoved(int locationIndex, Point newPoint) {
		AbsoluteBendpoint newBendpoint = new AbsoluteBendpoint(newPoint);
		getBendpoints().set(locationIndex, newBendpoint);
		
		refresh();
	}
	
	@Override
	public void borderColorChanged(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			getFigure().setForegroundColor(color);
			refresh();
		}
	}

	//============================================================
	// AbstractConnectionEditPart
	
	@Override
	public void addNotify() {
		super.addNotify();
		getModel().addListener(this);
	}
	
	@Override
	public void removeNotify() {
		getModel().removeListener(this);
		super.removeNotify();
	}
}
