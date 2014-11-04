package asia.sejong.freedrawing.parts.FDConnectionEditPart;

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
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.SWT;

import asia.sejong.freedrawing.figures.FDConnectionFigure;
import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd.DeleteFDConnectionCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.cmd.RecreateFDConnectionCommand;

public class FDConnectionEditPart extends AbstractConnectionEditPart {

	protected static final PointList ARROWHEAD = new PointList(new int[]{
			0, 0, -2, 2, -2, 0, -2, -2, 0, 0
		});

	public FDConnectionEditPart(FDConnection connection) {
		setModel(connection);
	}

	public FDConnection getModel() {
		return (FDConnection) super.getModel();
	}
	
//	@Override
//	public void sourceChanged(FDElement sourceModel) {
//		DebugUtil.printLogStart();
//		EditPart originalSourceEditPart = getSource();
//		Map<?, ?> registry = getViewer().getEditPartRegistry();
//		if ( registry.get(sourceModel) != originalSourceEditPart ) {
//			if ( registry.get(sourceModel) instanceof AbstractFDNodeEditPart ) {
//				AbstractFDNodeEditPart newEditPart = (AbstractFDNodeEditPart)registry.get(sourceModel);
//				setSource(newEditPart);
//			}
//		}
//		DebugUtil.printLogEnd();
//	}
//
//	@Override
//	public void targetChanged(FDElement targetModel) {
//		DebugUtil.printLogStart();
//		EditPart originalTargetEditPart = getTarget();
//		Map<?, ?> registry = getViewer().getEditPartRegistry();
//		if ( registry.get(targetModel) != originalTargetEditPart ) {
//			if ( registry.get(targetModel) instanceof AbstractFDNodeEditPart ) {
//				AbstractFDNodeEditPart newEditPart = (AbstractFDNodeEditPart)registry.get(targetModel);
//				setTarget(newEditPart);
//			}
//		}
//		DebugUtil.printLogEnd();
//	}
	
	protected IFigure createFigure() {
		return createConnection(true);
	}

	public static PolylineConnection createConnection(boolean custom) {
		PolylineConnection connection = null;
		if ( custom ) {
			connection = new FDConnectionFigure();
			connection.setLineWidth(3);
			connection.setAntialias(1);
		} else {
			connection = new PolylineConnection();
//			connection.setLineDash(new float[] {1f,2f});
			connection.setLineStyle(SWT.LINE_DASH);
			connection.setAlpha(180);
			connection.setBackgroundColor(ColorConstants.blue);
			connection.setLineWidth(4);
			connection.setAntialias(5);
		}

		connection.setConnectionRouter(new BendpointConnectionRouter());

		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.darkGray);
		connection.setTargetDecoration(decoration);

		return connection;
	}
	
//	/**
//	 * Answer a new command to recreate the receiver
//	 */
//	public CreateConnectionCommand recreateCommand() {
//		CreateConnectionCommand cmd;
//		if (getModel().isOffspringConnection()) {
//			cmd = new CreateOffspringConnectionCommand(getModel().marriage);
//			cmd.setTarget(getModel().person);
//		}
//		else {
//			cmd = new CreateSpouseConnectionCommand(getModel().person);
//			cmd.setTarget(getModel().marriage);
//		}
//		return cmd;
//	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getSourceConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getSource() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getSource();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
		return super.getSourceConnectionAnchor();
	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getTargetConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getTarget() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getTarget();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
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
				return new DeleteFDConnectionCommand(getModel());
			}
		});
		
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new BendpointFDConnectionEditPolicy());
	}
	
	protected PolylineConnection getConnection() {
		return (PolylineConnection)getFigure();
	}

	public RecreateFDConnectionCommand recreateCommand() {
		FDNode src = (FDNode)getSource().getModel();
		List<Point> bendpoints = src.getBendpoints((FDNode)getTarget().getModel());
		
		RecreateFDConnectionCommand cmd = new RecreateFDConnectionCommand(bendpoints);
		cmd.setSource(getSource().getModel());
		cmd.setTarget(getTarget().getModel());
		return cmd;
	}
	
	public void deactivate() {
		super.deactivate();
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
	
	public void bendpointAdded(int locationIndex, Point location) {
		List<Bendpoint> bendpoints = getBendpoints();
		AbsoluteBendpoint bp = new AbsoluteBendpoint(location);
		bendpoints.add(locationIndex, bp);
		
		refresh();
	}
	
	public void bendpointRemoved(int locationIndex) {
		List<Bendpoint> bendpoints = getBendpoints();
		bendpoints.remove(locationIndex);

		refresh();
	}
	
	public void bendpointMoved(int locationIndex, Point newPoint) {
		AbsoluteBendpoint newBendpoint = new AbsoluteBendpoint(newPoint);
		getBendpoints().set(locationIndex, newBendpoint);
		
		refresh();
	}

	public void setBendpoints(List<Point> targetBendpoints) {
		PolylineConnection connection = getConnection();
		BendpointConnectionRouter connectionRouter = (BendpointConnectionRouter)connection.getConnectionRouter();
		List<Bendpoint> bendpoints = new ArrayList<Bendpoint>();
		if ( targetBendpoints != null ) {
			for ( Point point : targetBendpoints  ) {
				bendpoints.add(new AbsoluteBendpoint(point));
			}
			connectionRouter.setConstraint(connection, bendpoints);
		}
	}
}
