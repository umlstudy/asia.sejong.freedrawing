package asia.sejong.freedrawing.parts.FDNodeEditPart;

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
public class FDNodeOrderedLayoutEditPolicy extends OrderedLayoutEditPolicy {

	protected Command getCreateCommand(CreateRequest request) {
//		Object type = request.getNewObjectType();
//		if (type == Note.class) {
//			Note note = (Note) request.getNewObject();
//			return new CreateNoteCommand(getModel(), note, null);
//		}
		return null;
	}

	/**
	 * Return a new reparent command when the a note is dragged
	 * from one note container to another.
	 * Return null if the container has not changed.
	 */
	protected Command createAddCommand(EditPart child, EditPart after) {
//		NoteContainer oldContainer = (NoteContainer) child.getParent().getModel();
//		if (getModel() == oldContainer)
//			return null;
//		Note note = (Note) child.getModel();
//		ReparentNoteCommand cmd = new ReparentNoteCommand(getModel(), note);
//		if (after != null)
//			cmd.setAfterNote((Note) after.getModel());
//		cmd.setOldContainer(oldContainer);
//		return cmd;
		
		return null; 
	}

	/**
	 * Return a new reorder command when a note is dragged
	 * within the same note container.
	 * Return null if the order is not changing.
	 */
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
//		if (child == after || getChildren().size() == 1)
//			return null;
//		int index = getChildren().indexOf(child);
//		if (index == 0) {
//			if (after == null)
//				return null;
//		}
//		else {
//			if (after == getChildren().get(index - 1))
//				return null;
//		}
//		ReorderNoteCommand cmd = new ReorderNoteCommand(getModel(), (Note) child.getModel());
//		if (after != null)
//			cmd.setAfterNote((Note) after.getModel());
//		return cmd;
		return null;
	}

	/**
	 * Given a request, return the insertion position
	 * indicated by the y coordinate of the request.
	 * This is called both when the note is reparented and reordered.
	 */
	protected EditPart getInsertionReference(Request request) {
//		int y = ((ChangeBoundsRequest) request).getLocation().y;
//		List<?> notes = getChildren();
//		NoteEditPart afterNote = null;
//		for (Iterator<?> iter = notes.iterator(); iter.hasNext();) {
//			NoteEditPart note = (NoteEditPart) iter.next();
//			if (y < note.getFigure().getBounds().y)
//				return afterNote;
//			afterNote = note;
//		}
//		return afterNote;
		return null;
	}
}
