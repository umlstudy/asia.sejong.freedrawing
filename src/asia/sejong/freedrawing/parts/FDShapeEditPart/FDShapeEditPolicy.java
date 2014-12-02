package asia.sejong.freedrawing.parts.FDShapeEditPart;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.FDWireCreateCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.FDWireRecreateCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;

public class FDShapeEditPolicy extends GraphicalNodeEditPolicy {
	
	private FDRoot getRoot() {
		return (FDRoot)getHost().getViewer().getContents().getModel();
	}

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
		return wireRecreateCommand;
	}
}