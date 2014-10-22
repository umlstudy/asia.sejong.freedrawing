package asia.sejong.freedrawing.parts.area.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.commands.CreateBendpointConnectionCommand;
import asia.sejong.freedrawing.commands.CreateConnectionCommand;
import asia.sejong.freedrawing.commands.DeleteConnectionCommand;
import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.parts.EditPartUtil;
import asia.sejong.freedrawing.parts.connection.AbstractFDConnectionEditPart;
import asia.sejong.freedrawing.parts.connection.FDBendpointConnectionEditPart;
import asia.sejong.freedrawing.parts.connection.policies.FDBendpointConnectionEditPolicy;

/**
 * A superclass for {@link FDBendpointConnectionEditPolicy} and
 * {@link MarriageGraphicalNodeEditPolicy} to share behavior.
 */
public class FDElementEditPolicy extends GraphicalNodeEditPolicy {
	/**
	 * Answer a new connection command for the receiver.
	 */
	public CreateConnectionCommand createConnectionCommand() {
		if ( getHost().getModel() instanceof AbstractFDElement ) {
			return new CreateBendpointConnectionCommand();
		}
		return null;
	}

	/**
	 * Return a new connection command with the receiver's model as the "source".
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		request.setStartCommand(createConnectionCommand());
		return request.getStartCommand();
	}
	
	protected Connection createDummyConnection(Request req) {
		return FDBendpointConnectionEditPart.createConnection(false);
	}
	
//	protected abstract AbstractFDElement getSource();
//	protected abstract AbstractFDElement getTarget();

	/**
	 * If the connection is valid with the receiver's model as its target then set the
	 * recevier's model as its target and return the command otherwise return
	 * <code>null</code> indicating that the connection is invalid and should not be
	 * created.
	 */
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Command startCmd = request.getStartCommand();
		if (!(startCmd instanceof CreateConnectionCommand))
			return null;
		CreateConnectionCommand connCmd = (CreateConnectionCommand) startCmd;
		
		Object source = request.getSourceEditPart().getModel();
		if (!connCmd.isValidSource(source)) {
			return null;
		} else {
			connCmd.setSource(source);
		}
		
		Object target = request.getTargetEditPart().getModel();
		if (!connCmd.isValidTarget(target)) {
			return null;
		} else {
			connCmd.setSource(target);
		}
		
		connCmd.setRootModel(EditPartUtil.getFreedrawingData(getHost()));
		
		return connCmd;
	}

	/**
	 * If the connection modification request is valid with the recevier's model
	 * as the source, then return a command to modify the specified connection
	 * otherwise return <code>null</code> indicating that the connection modification
	 * request is invalid.
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		EditPart part = request.getConnectionEditPart();
		if (!(part instanceof AbstractFDConnectionEditPart))
			return null;
		AbstractFDConnectionEditPart connPart = (AbstractFDConnectionEditPart) part;
		CreateConnectionCommand connCmd = connPart.recreateCommand();
		if (!connCmd.isValidSource(request.getConnectionEditPart().getModel()))
			return null;
		connCmd.setSource(request.getConnectionEditPart().getModel());
		Command deleteCmd = new DeleteConnectionCommand(connPart.getModel());
		Command modifyCmd = deleteCmd.chain(connCmd);
		modifyCmd.setLabel("Modify " + connCmd.getConnectionName());
		return modifyCmd;
	}

	/**
	 * If the connection modification request is valid with the recevier's model
	 * as the target, then return a command to modify the specified connection
	 * otherwise return <code>null</code> indicating that the connection modification
	 * request is invalid.
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		EditPart part = request.getConnectionEditPart();
		if (!(part instanceof AbstractFDConnectionEditPart))
			return null;
		AbstractFDConnectionEditPart connPart = (AbstractFDConnectionEditPart) part;
		CreateConnectionCommand connCmd = connPart.recreateCommand();
		if (!connCmd.isValidTarget(request.getConnectionEditPart().getModel()))
			return null;
		connCmd.setTarget(request.getConnectionEditPart().getModel());
		Command deleteCmd = new DeleteConnectionCommand(connPart.getModel());
		Command modifyCmd = deleteCmd.chain(connCmd);
		modifyCmd.setLabel("Modify " + connCmd.getConnectionName());
		return modifyCmd;
	}
}