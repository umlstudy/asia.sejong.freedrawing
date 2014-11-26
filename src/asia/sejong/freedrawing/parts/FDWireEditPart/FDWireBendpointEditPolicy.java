package asia.sejong.freedrawing.parts.FDWireEditPart;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import asia.sejong.freedrawing.parts.FDWireEditPart.command.FDWireBendpointAddCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.command.FDWireBendpointDeleteCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.command.FDWireBendpointMoveCommand;

public class FDWireBendpointEditPolicy extends BendpointEditPolicy {
	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		return new FDWireBendpointMoveCommand(request);
	}
	
	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		return new FDWireBendpointDeleteCommand(request);
	}
	
	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		return new FDWireBendpointAddCommand(request);
	}
}
