package asia.sejong.freedrawing.parts.FDNodeEditPart;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.FDWireCreateCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.FDWireRecreateCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;
import asia.sejong.freedrawing.parts.FDWireEditPart.command.FDWireDeleteCommand;

public class FDNodeEditPolicy extends GraphicalNodeEditPolicy {
	
	/**
	 * Answer a new connection command for the receiver.
	 */
	public FDWireCreateCommand createConnectionCommand() {
		if ( getHost().getModel() instanceof FDRect ) {
			return new FDWireCreateCommand();
		}
		return null;
	}
	
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Command startCmd = request.getStartCommand();
		if (!(startCmd instanceof FDWireCreateCommand))
			return null;
		FDWireCreateCommand connCmd = (FDWireCreateCommand) startCmd;
		
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
		FDWireCreateCommand connCmd = createConnectionCommand();
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
		return FDWireEditPart.createWireFigure(false);
	}
	
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		EditPart currentEditPart = request.getConnectionEditPart();
		if (!(currentEditPart instanceof FDWireEditPart)) {
			return null;
		}
		
		FDWireEditPart currentConnectionEditPart = (FDWireEditPart) currentEditPart;
		FDWireRecreateCommand wireRecreateCommand = currentConnectionEditPart.recreateCommand();
		FDRect source = (FDRect)request.getTarget().getModel();
		if (!wireRecreateCommand.isValidSource(source) ) {
			return null;
		}
		
		wireRecreateCommand.setSource(source);
		if ( source.containsTarget(wireRecreateCommand.getTarget()) ) {
			return null;
		}

		Command deleteConnCmd = new FDWireDeleteCommand(currentConnectionEditPart.getModel());
		Command modifyConnCmd = deleteConnCmd.chain(wireRecreateCommand);
		modifyConnCmd.setLabel("Modify " + wireRecreateCommand.getWireName());
		
		return modifyConnCmd;
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		EditPart currentEditPart = request.getConnectionEditPart();
		if (!(currentEditPart instanceof FDWireEditPart)) {
			return null;
		}
		
		FDWireEditPart currentConnectionEditPart = (FDWireEditPart) currentEditPart;
		FDWireRecreateCommand wireRecreateCommand = currentConnectionEditPart.recreateCommand();
		FDRect target = (FDRect)request.getTarget().getModel();
		if ( !wireRecreateCommand.isValidTarget(target) ) {
			return null;
		}
		
		wireRecreateCommand.setTarget(target);
		if ( wireRecreateCommand.getSource().containsTarget(target) ) {
			return null;
		}

		Command deleteConnCmd = new FDWireDeleteCommand(currentConnectionEditPart.getModel());
		Command modifyConnCmd = deleteConnCmd.chain(wireRecreateCommand);
		modifyConnCmd.setLabel("Modify " + wireRecreateCommand.getWireName());
		
		return modifyConnCmd;
	}
}