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
import asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd.DeleteFDConnectionCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.cmd.CreateFDConnectionCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.cmd.RecreateFDConnectionCommand;

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
		EditPart currentEditPart = request.getConnectionEditPart();
		if (!(currentEditPart instanceof FDConnectionEditPart)) {
			return null;
		}
		
		FDConnectionEditPart currentConnectionEditPart = (FDConnectionEditPart) currentEditPart;
		RecreateFDConnectionCommand recreateConnectionCommand = currentConnectionEditPart.recreateCommand();
		FDNode source = (FDNode)request.getTarget().getModel();
		if (!recreateConnectionCommand.isValidSource(source) ) {
			return null;
		}
		
		recreateConnectionCommand.setSource(source);
		if ( source.containsTarget(recreateConnectionCommand.getTarget()) ) {
			return null;
		}

		Command deleteConnCmd = new DeleteFDConnectionCommand(currentConnectionEditPart.getModel());
		Command modifyConnCmd = deleteConnCmd.chain(recreateConnectionCommand);
		modifyConnCmd.setLabel("Modify " + recreateConnectionCommand.getConnectionName());
		
		return modifyConnCmd;
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		EditPart currentEditPart = request.getConnectionEditPart();
		if (!(currentEditPart instanceof FDConnectionEditPart)) {
			return null;
		}
		
		FDConnectionEditPart currentConnectionEditPart = (FDConnectionEditPart) currentEditPart;
		RecreateFDConnectionCommand recreateConnectionCommand = currentConnectionEditPart.recreateCommand();
		FDNode target = (FDNode)request.getTarget().getModel();
		if ( !recreateConnectionCommand.isValidTarget(target) ) {
			return null;
		}
		
		recreateConnectionCommand.setTarget(target);
		if ( recreateConnectionCommand.getSource().containsTarget(target) ) {
			return null;
		}

		Command deleteConnCmd = new DeleteFDConnectionCommand(currentConnectionEditPart.getModel());
		Command modifyConnCmd = deleteConnCmd.chain(recreateConnectionCommand);
		modifyConnCmd.setLabel("Modify " + recreateConnectionCommand.getConnectionName());
		
		return modifyConnCmd;
	}
}