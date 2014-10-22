package asia.sejong.freedrawing.parts.FDNodeEditPart;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.parts.FDConnectionEditPart.FDConnectionEditPart;
import asia.sejong.freedrawing.parts.common.EditPartUtil;

public class FDNodeEditPolicy extends GraphicalNodeEditPolicy {
	
	/**
	 * Answer a new connection command for the receiver.
	 */
	public CreateFDConnectionCommand createConnectionCommand() {
		if ( getHost().getModel() instanceof FDNode ) {
			return new CreateFDConnectionCommand();
		}
		return null;
	}
	
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Command startCmd = request.getStartCommand();
		if (!(startCmd instanceof CreateFDConnectionCommand))
			return null;
		CreateFDConnectionCommand connCmd = (CreateFDConnectionCommand) startCmd;
		
		Object target = request.getTargetEditPart().getModel();
		if (!connCmd.isValidTarget(target)) {
			return null;
		} else {
			connCmd.setTarget(target);
		}
		
		connCmd.setRoot(EditPartUtil.getFreedrawingData(getHost()));
		
		return connCmd;
	}

	/**
	 * Return a new connection command with the receiver's model as the "source".
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		CreateFDConnectionCommand connCmd = createConnectionCommand();
		request.setStartCommand(connCmd);
		Object source = request.getTargetEditPart().getModel();
		if (!connCmd.isValidSource(source)) {
			return null;
		} else {
			connCmd.setSource(source);
		}
		return connCmd;
	}
	
	protected Connection createDummyConnection(Request req) {
		return FDConnectionEditPart.createConnection(false);
	}
	
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		EditPart part = request.getConnectionEditPart();
		if (!(part instanceof FDConnectionEditPart))
			return null;
		FDConnectionEditPart connPart = (FDConnectionEditPart) part;
		CreateFDConnectionCommand connCmd = connPart.recreateCommand();
		if (!connCmd.isValidSource(request.getConnectionEditPart().getModel()))
			return null;
		connCmd.setSource(request.getConnectionEditPart().getModel());
		Command deleteCmd = new DeleteFDConnectionCommand(connPart.getModel());
		Command modifyCmd = deleteCmd.chain(connCmd);
		modifyCmd.setLabel("Modify " + connCmd.getConnectionName());
		return modifyCmd;
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		EditPart part = request.getConnectionEditPart();
		if (!(part instanceof FDConnectionEditPart))
			return null;
		FDConnectionEditPart connPart = (FDConnectionEditPart) part;
		CreateFDConnectionCommand connCmd = connPart.recreateCommand();
		if (!connCmd.isValidTarget(request.getConnectionEditPart().getModel()))
			return null;
		connCmd.setTarget(request.getConnectionEditPart().getModel());
		Command deleteCmd = new DeleteFDConnectionCommand(connPart.getModel());
		Command modifyCmd = deleteCmd.chain(connCmd);
		modifyCmd.setLabel("Modify " + connCmd.getConnectionName());
		return modifyCmd;
	}
}