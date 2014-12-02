package asia.sejong.freedrawing.parts.FDShapeEditPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Handles creation and moving Node
 * @author SeJong
 *
 */
public class FDShapeOrderedLayoutEditPolicy extends OrderedLayoutEditPolicy {

	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	protected Command createAddCommand(EditPart child, EditPart after) {
		return null; 
	}

	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return null;
	}

	protected EditPart getInsertionReference(Request request) {
		return null;
	}
}
