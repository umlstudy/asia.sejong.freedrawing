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
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.requests.GroupRequest;

import asia.sejong.freedrawing.figures.FDConnectionFigure;
import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.parts.FDNodeEditPart.CreateFDConnectionCommand;

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
		} else {
			connection = new PolylineConnection();
		}
		connection.setConnectionRouter(new BendpointConnectionRouter());
		connection.setLineWidth(3);
		connection.setAntialias(1);

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
		ConnectionEndpointEditPolicy selectionPolicy = new ConnectionEndpointEditPolicy();
		
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);

		// Handles deleting the receiver
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy() {
			@Override
			protected Command getDeleteCommand(GroupRequest request) {
				//return new DeleteGenealogyConnectionCommand(getModel());
				return null;
			}
		});
		
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new BendpointEditPolicy() {
			@Override
			protected Command getMoveBendpointCommand(BendpointRequest request) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected Command getDeleteBendpointCommand(BendpointRequest request) {
				// TODO Auto-generated method stub
				return null;
			}
			
			final RequestOwnedCommand<BendpointRequest> addBendpointCommand = new RequestOwnedCommand<BendpointRequest>() {
				public boolean canExecute() {
					return true;
				}
				
				public void execute() {
					addBendpoint(getRequest());
				}
			};
			
			@Override
			protected Command getCreateBendpointCommand(BendpointRequest request) {
				System.out.println("AAAAAAAAAAAAAAA" + request.getLocation());
				addBendpointCommand.setRequest(request);
				return addBendpointCommand;
			}
		});
	}

	public CreateFDConnectionCommand recreateCommand() {
		// TODO Auto-generated method stub
		return null;
	}
}
