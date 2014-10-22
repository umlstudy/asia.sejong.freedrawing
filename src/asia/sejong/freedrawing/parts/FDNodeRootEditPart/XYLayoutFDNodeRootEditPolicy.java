package asia.sejong.freedrawing.parts.FDNodeRootEditPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;

/**
 * Handles constraint changes (e.g. moving and/or resizing) of model elements
 * and creation of new model elements
 * @author SeJong
 *
 */
public class XYLayoutFDNodeRootEditPolicy extends XYLayoutEditPolicy {
	
	protected Command getCreateCommand(CreateRequest request) {
		Object type = request.getNewObjectType();
		Rectangle box = (Rectangle) getConstraintFor(request);
		FDNodeRoot nodeRoot = (FDNodeRoot)getHost().getModel();
		if (type == FDNode.class) {
			FDNode element = (FDNode) request.getNewObject();
			element.setRectangle(box);
			return new CreateFDNodeCommand(nodeRoot, element);
		}
		return null;
	}

	/**
	 * IGNORE: This method is a holdover from earlier GEF frameworks
	 * and will be removed in future versions of GEF.
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		return null;
	}
	
	/**
	 * Return a command for moving elements around the canvas
	 */
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		FDNode node = (FDNode) child.getModel();
		Rectangle rect = (Rectangle) constraint;
		return new MoveAndResizeFDNodeCommand(node, rect);
	}
	
	/**
	 * Exclude MarriageEditParts from being resized
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
//		if (child instanceof MarriageEditPart)
//			return new NonResizableMarriageEditPolicy();
		return super.createChildEditPolicy(child);
	}
	
//	/**
//	 * Called when a Note is dragged from a Person on the canvas.
//	 * Return a command for reparenting the note
//	 */
//	protected Command createAddCommand(EditPart child, Object constraint) {
//		NoteContainer oldContainer = (NoteContainer) child.getParent().getModel();
//		if (getModel() == oldContainer)
//			return null;
//		Note note = (Note) child.getModel();
//		ReparentNoteCommand cmd = new ReparentNoteCommand(getModel(), note);
//		cmd.setBounds((Rectangle) constraint);
//		cmd.setOldContainer(oldContainer);
//		return cmd;
//	}
}
