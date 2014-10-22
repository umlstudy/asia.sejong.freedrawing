package asia.sejong.freedrawing.parts.connection.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;

import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.model.connection.FDBendpointConnection;
import asia.sejong.freedrawing.parts.connection.FDBendpointConnectionEditPart;

/**
 * The graphical node editing policy for {@link PersonEditPart}
 * for handling connection creation and modification.
 */
public class FDBendpointConnectionEditPolicy {
	
	private final FDBendpointConnection model;
	private final AbstractFDElement source;
	private       AbstractFDElement target;

	public FDBendpointConnectionEditPolicy(AbstractFDElement source) {
		this.model = new FDBendpointConnection();
		this.source = source;
		System.out.println("Create FDBendpointNodeEditPolicy");
	}

	/**
	 * Answer the model element associated with the receiver
	 */
	protected FDBendpointConnection getModel() {
		return model;
	}
	
	protected AbstractFDElement getSource() {
		return source;
	}
	
	protected AbstractFDElement getTarget() {
		return target;
	}

//	/**
//	 * Answer a new connection command for the receiver.
//	 */
//	public CreateConnectionCommand createConnectionCommand() {
//		CreateBendpointConnectionCommand createBendpointConnectionCommand = new CreateBendpointConnectionCommand(model);
//		if ( createBendpointConnectionCommand.isValidSource(source) ) {
//			return createBendpointConnectionCommand;
//		}
//		return null;
//	}
	
	/**
	 * Answer a figure to be used during connection creation
	 */
	protected Connection createDummyConnection(Request req) {
		return FDBendpointConnectionEditPart.createConnection(false);
	}
	
//	protected ConnectionAnchor getTargetConnectionAnchor(CreateConnectionRequest request) {
//		if ( request.getTargetEditPart() == request.getSourceEditPart() ) return null;
//		
//		EditPart targetEditPart = request.getTargetEditPart();
//		if ( targetEditPart instanceof AbstractFDNodeEditPart ) {
//			target = (AbstractFDElement)targetEditPart.getModel();
//			if ( target == null ) {
//				return null;
//			}
//			System.out.println("Target Setted ? " + target);
//			return super.getTargetConnectionAnchor(request);
//		}
//		
//		return null;
//	}
}