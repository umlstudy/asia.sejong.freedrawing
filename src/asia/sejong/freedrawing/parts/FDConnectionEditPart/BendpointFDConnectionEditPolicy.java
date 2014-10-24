package asia.sejong.freedrawing.parts.FDConnectionEditPart;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd.AddBendpointCommand;
import asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd.DeleteBendpointCommand;
import asia.sejong.freedrawing.parts.FDConnectionEditPart.cmd.MoveBendpointCommand;

public class BendpointFDConnectionEditPolicy extends BendpointEditPolicy {
	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		return new MoveBendpointCommand(request);
	}
	
	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		return new DeleteBendpointCommand(request);
	}
	
	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {
		return new AddBendpointCommand(request);
	}
}
