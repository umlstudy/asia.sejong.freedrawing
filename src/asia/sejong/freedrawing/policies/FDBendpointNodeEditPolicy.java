package asia.sejong.freedrawing.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;

import asia.sejong.freedrawing.commands.CreateBendpointConnectionCommand;
import asia.sejong.freedrawing.commands.CreateConnectionCommand;
import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.model.connection.FDBendpointConnection;
import asia.sejong.freedrawing.parts.connection.FDBendpointConnectionEditPart;

/**
 * The graphical node editing policy for {@link PersonEditPart}
 * for handling connection creation and modification.
 */
public class FDBendpointNodeEditPolicy extends FDElementGraphicalNodeEditPolicy {
	
	private final FDBendpointConnection model;
	private final AbstractFDElement target;

	public FDBendpointNodeEditPolicy(AbstractFDElement target) {
		this.model = new FDBendpointConnection();
		this.target = target;
		System.out.println("Create FDBendpointNodeEditPolicy");
	}

	/**
	 * Answer the model element associated with the receiver
	 */
	protected FDBendpointConnection getModel() {
		return model;
	}
	
	protected AbstractFDElement getTarget() {
		return target;
	}

	/**
	 * Answer a new connection command for the receiver.
	 */
	public CreateConnectionCommand createConnectionCommand() {
		CreateBendpointConnectionCommand createBendpointConnectionCommand = new CreateBendpointConnectionCommand(model);
		if ( createBendpointConnectionCommand.isValidSource(target) ) {
			createBendpointConnectionCommand.setSource(target);
			return createBendpointConnectionCommand;
		}
		return null;
	}
	
	/**
	 * Answer a figure to be used during connection creation
	 */
	protected Connection createDummyConnection(Request req) {
		return FDBendpointConnectionEditPart.createConnection();
	}
}