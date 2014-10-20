package asia.sejong.freedrawing.parts.connection;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import asia.sejong.freedrawing.model.connection.AbstractFDConnection;

public abstract class AbstractFDConnectionEditPart extends AbstractConnectionEditPart
{
	protected static final PointList ARROWHEAD = new PointList(new int[]{
		0, 0, -2, 2, -2, 0, -2, -2, 0, 0
	});

	public AbstractFDConnectionEditPart(AbstractFDConnection connection) {
		setModel(connection);
	}

	public AbstractFDConnection getModel() {
		return (AbstractFDConnection) super.getModel();
	}
//	
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
//
//	/**
//	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
//	 * so that the connection terminates along the outside of the MarriageFigure rather
//	 * than at the MarriageFigure's bounding box.
//	 */
//	protected ConnectionAnchor getSourceConnectionAnchor() {
//		/*
//		 * Rather than implementing the getSourceConnectionAnchor() in this class,
//		 * modify MarriageEditPart to implement the NodeEditPart interface
//		 */
//		//if (getSource() instanceof MarriageEditPart) {
//		//	MarriageEditPart editPart = (MarriageEditPart) getSource();
//		//	return new MarriageAnchor(editPart.getFigure());
//		//}
//		return super.getSourceConnectionAnchor();
//	}
//
//	/**
//	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
//	 * so that the connection terminates along the outside of the MarriageFigure rather
//	 * than at the MarriageFigure's bounding box.
//	 */
//	protected ConnectionAnchor getTargetConnectionAnchor() {
//		/*
//		 * Rather than implementing the getSourceConnectionAnchor() in this class,
//		 * modify MarriageEditPart to implement the NodeEditPart interface
//		 */
//		//if (getTarget() instanceof MarriageEditPart) {
//		//	MarriageEditPart editPart = (MarriageEditPart) getTarget();
//		//	return new MarriageAnchor(editPart.getFigure());
//		//}
//		return super.getTargetConnectionAnchor();
//	}
//
//	/**
//	 * Create and install {@link EditPolicy} instances used to define behavior
//	 * associated with this EditPart's figure.
//	 */
//	protected void createEditPolicies() {
//		ConnectionEndpointEditPolicy selectionPolicy = new ConnectionEndpointEditPolicy();
//		
//		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);
//
//		// Handles deleting the receiver
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy() {
//			@Override
//			protected Command getDeleteCommand(GroupRequest request) {
//				return new DeleteGenealogyConnectionCommand(getModel());
//			}
//		});
//		
//		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new BendpointEditPolicy() {
//			@Override
//			protected Command getMoveBendpointCommand(BendpointRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			protected Command getDeleteBendpointCommand(BendpointRequest request) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			final RequestOwnedCommand<BendpointRequest> addBendpointCommand = new RequestOwnedCommand<BendpointRequest>() {
//				public boolean canExecute() {
//					return true;
//				}
//				
//				public void execute() {
//					addBendpoint(getRequest());
//				}
//			};
//			
//			@Override
//			protected Command getCreateBendpointCommand(BendpointRequest request) {
//				System.out.println("AAAAAAAAAAAAAAA" + request.getLocation());
//				addBendpointCommand.setRequest(request);
//				return addBendpointCommand;
//			}
//		});
//	}
}
