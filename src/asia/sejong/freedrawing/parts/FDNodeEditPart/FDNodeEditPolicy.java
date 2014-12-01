package asia.sejong.freedrawing.parts.FDNodeEditPart;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.FDWireCreateCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.FDWireRecreateCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;

public class FDNodeEditPolicy extends GraphicalNodeEditPolicy {
	
	private FDRoot getRoot() {
		return (FDRoot)getHost().getViewer().getContents().getModel();
	}

//	/**
//	 * Answer a new connection command for the receiver.
//	 */
//	public FDWireCreateCommand createConnectionCommand() {
//		if ( getHost().getModel() instanceof FDRect ) {
//			return new FDWireCreateCommand(root);
//		}
//		return null;
//	}
//	
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		if ( !(request.getStartCommand() instanceof FDWireCreateCommand) ) {
			return null;
		}
		FDWireCreateCommand wireCreateCommand = (FDWireCreateCommand)request.getStartCommand();
		Object target = request.getTargetEditPart().getModel();
		if (!FDWireCreateCommand.isValidTarget__(target)) {
			return null;
		}
		wireCreateCommand.setTarget(target);
		
		if ( !wireCreateCommand.isValidSourceAndTarget() ) {
			return null;
		}
		
		return wireCreateCommand;
	}

	/**
	 * Return a new connection command with the receiver's model as the "source".
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		Object source = request.getTargetEditPart().getModel();
		if (!FDWireCreateCommand.isValidSource__(source)) {
			return null;
		}
		
		FDWireCreateCommand wireCreateCommand = new FDWireCreateCommand(getRoot());
		wireCreateCommand.setSource(source);
		request.setStartCommand(wireCreateCommand);
		
		return wireCreateCommand;
	}
	
	protected Connection createDummyConnection(Request req) {
		return FDWireEditPart.createWireFigure(false);
	}
	
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		FDRect source = (FDRect)request.getTarget().getModel();
		if (!FDWireRecreateCommand.isValidSource__(source) ) {
			return null;
		}
		
		FDWireEditPart currentWireEditPart = (FDWireEditPart) request.getConnectionEditPart();
		FDWire oldWire = currentWireEditPart.getModel();
		FDWireRecreateCommand wireRecreateCommand = new FDWireRecreateCommand(getRoot(), oldWire);
		wireRecreateCommand.setSource(source);
		wireRecreateCommand.setTarget(oldWire.getTarget());
		
		if ( !wireRecreateCommand.isValidSourceAndTarget() ) {
			return null;
		}
//		Command deleteConnCmd = new FDWireDeleteCommand(root, currentWireEditPart.getModel());
//		Command modifyConnCmd = deleteConnCmd.chain(wireRecreateCommand);
//		modifyConnCmd.setLabel("Modify " + wireRecreateCommand.getWireName());
		
		return wireRecreateCommand;
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		FDRect target = (FDRect)request.getTarget().getModel();
		if (!FDWireRecreateCommand.isValidTarget__(target) ) {
			return null;
		}
		
		FDWireEditPart currentWireEditPart = (FDWireEditPart) request.getConnectionEditPart();
		FDWire oldWire = currentWireEditPart.getModel();
		FDWireRecreateCommand wireRecreateCommand = new FDWireRecreateCommand(getRoot(), oldWire);
		wireRecreateCommand.setSource(oldWire.getSource());
		wireRecreateCommand.setTarget(target);
		
		if ( !wireRecreateCommand.isValidSourceAndTarget() ) {
			return null;
		}
//		Command deleteConnCmd = new FDWireDeleteCommand(root, currentWireEditPart.getModel());
//		Command modifyConnCmd = deleteConnCmd.chain(wireRecreateCommand);
//		modifyConnCmd.setLabel("Modify " + wireRecreateCommand.getWireName());
		
		return wireRecreateCommand;
	}
}