package asia.sejong.freedrawing.parts.FDWireEditPart;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.FDWireEndPoint;
import asia.sejong.freedrawing.parts.FDRootEditPart.FDRootEditPart;
import asia.sejong.freedrawing.parts.FDShapeEditPart.FDShapeEditPart;
import asia.sejong.freedrawing.parts.FDWireEditPart.command.FDWireCreateCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.command.FDWireRecreateCommand;

public class FDWireEditPolicy extends GraphicalNodeEditPolicy {
	
	private FDRoot getRoot() {
		return (FDRoot)getHost().getViewer().getContents().getModel();
	}

	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		if ( !(request.getStartCommand() instanceof FDWireCreateCommand) ) {
			return null;
		}
		FDWireCreateCommand wireCreateCommand = (FDWireCreateCommand)request.getStartCommand();
		Object target = request.getTargetEditPart().getModel();
		if ( target instanceof FDRoot ) {
			target = FDWireEndPoint.newInstance(request.getLocation());
		}
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
		if ( source instanceof FDRoot ) {
			source = FDWireEndPoint.newInstance(request.getLocation());
		}
		if (!FDWireCreateCommand.isValidSource__(source)) {
			return null;
		}
		
		FDWireCreateCommand wireCreateCommand = new FDWireCreateCommand(getRoot());
		wireCreateCommand.setSource(source);
		request.setStartCommand(wireCreateCommand);
		
		return wireCreateCommand;
	}
	
	protected Connection createDummyConnection(Request req_) {
		CreateConnectionRequest req = (CreateConnectionRequest)req_;
		FDWireEndPoint src = (FDWireEndPoint)req.getSourceEditPart().getModel();
		FDWireEndPoint tar = (FDWireEndPoint)req.getTargetEditPart().getModel();
		return FDWireEditPart.createWireFigure(FDWire.newInstance(src, tar), false);
	}
	
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Object source = request.getTarget().getModel();
		if ( source instanceof FDRoot ) {
			source = FDWireEndPoint.newInstance(request.getLocation());
		}
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
		Object target = request.getTarget().getModel();
		if ( target instanceof FDRoot ) {
			target = FDWireEndPoint.newInstance(request.getLocation());
		}
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
	
	@Override
	protected ConnectionAnchor getSourceConnectionAnchor(CreateConnectionRequest request) {
		EditPart source = request.getSourceEditPart();
		
		if ( source instanceof FDShapeEditPart ) {
			return ((FDShapeEditPart) source).getSourceConnectionAnchor(request);
		}
		if ( source instanceof FDRootEditPart ) {
			return ((FDRootEditPart) source).getSourceConnectionAnchor(request);
		}
		
		return null;
	}
	
	@Override
	protected ConnectionAnchor getTargetConnectionAnchor(CreateConnectionRequest request) {
		EditPart target = request.getTargetEditPart();
		
		if ( target instanceof FDShapeEditPart ) {
			return ((FDShapeEditPart) target).getTargetConnectionAnchor(request);
		}
		if ( target instanceof FDRootEditPart ) {
			return ((FDRootEditPart) target).getTargetConnectionAnchor(request);
		}
		
		return null;
	}
}