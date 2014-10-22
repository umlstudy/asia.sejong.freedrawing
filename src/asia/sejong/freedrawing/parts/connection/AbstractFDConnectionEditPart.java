package asia.sejong.freedrawing.parts.connection;

import java.util.Map;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import asia.sejong.freedrawing.commands.CreateConnectionCommand;
import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.model.connection.AbstractFDConnection;
import asia.sejong.freedrawing.model.connection.listener.FDConnectionListener;
import asia.sejong.freedrawing.parts.area.AbstractFDNodeEditPart;
import asia.sejong.freedrawing.util.DebugUtil;

public abstract class AbstractFDConnectionEditPart extends AbstractConnectionEditPart implements FDConnectionListener {

	protected static final PointList ARROWHEAD = new PointList(new int[]{
		0, 0, -2, 2, -2, 0, -2, -2, 0, 0
	});

	public AbstractFDConnectionEditPart(AbstractFDConnection connection) {
		setModel(connection);
	}

	public AbstractFDConnection getModel() {
		return (AbstractFDConnection) super.getModel();
	}
	

	@Override
	public void sourceChanged(AbstractFDElement sourceModel) {
		DebugUtil.printLogStart();
		EditPart originalSourceEditPart = getSource();
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		if ( registry.get(sourceModel) != originalSourceEditPart ) {
			if ( registry.get(sourceModel) instanceof AbstractFDNodeEditPart ) {
				AbstractFDNodeEditPart newEditPart = (AbstractFDNodeEditPart)registry.get(sourceModel);
				setSource(newEditPart);
			}
		}
		DebugUtil.printLogEnd();
	}

	@Override
	public void targetChanged(AbstractFDElement targetModel) {
		DebugUtil.printLogStart();
		EditPart originalTargetEditPart = getTarget();
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		if ( registry.get(targetModel) != originalTargetEditPart ) {
			if ( registry.get(targetModel) instanceof AbstractFDNodeEditPart ) {
				AbstractFDNodeEditPart newEditPart = (AbstractFDNodeEditPart)registry.get(targetModel);
				setTarget(newEditPart);
			}
		}
		DebugUtil.printLogEnd();
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

	public abstract CreateConnectionCommand recreateCommand() ;
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can add itself as a listener to the underlying model object
	 */
	public void addNotify() {
		super.addNotify();
		getModel().addFDConnectionListener(this);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can stop listening to events from the underlying model object
	 */
	public void removeNotify() {
		getModel().removeFDConnectionListener(this);
		super.removeNotify();
	}
}
