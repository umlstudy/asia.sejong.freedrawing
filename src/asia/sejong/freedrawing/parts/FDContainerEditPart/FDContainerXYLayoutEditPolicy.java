package asia.sejong.freedrawing.parts.FDContainerEditPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.parts.FDContainerEditPart.command.FDShapeCloneCommand;
import asia.sejong.freedrawing.parts.FDContainerEditPart.command.FDShapeCreateCommand;
import asia.sejong.freedrawing.parts.FDContainerEditPart.command.FDShapeMoveAndResizeCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.FDShapeResizableEditPolicy;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.RotateCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.request.RotateRequest;
import asia.sejong.freedrawing.parts.common.FDShapeRotateTracker;

/**
 * Handles constraint changes (e.g. moving and/or resizing) of model elements
 * and creation of new model elements
 * @author SeJong
 *
 */
public class FDContainerXYLayoutEditPolicy extends XYLayoutEditPolicy {
	
	protected Command getCreateCommand(CreateRequest request) {
		//Object type = request.getNewObjectType();
		Rectangle box = (Rectangle) getConstraintFor(request);
		FDContainer container = (FDContainer)getHost().getModel();

		Object newObject = request.getNewObject();
		if ( FDShape.class.isInstance(newObject) ) {
			FDShape element = (FDShape) newObject;
			element.setRectangle(box);
			return new FDShapeCreateCommand(container, element);
		}
		return null;
	}
	
	@Override
	public Command getCommand(Request request) {
		if (FDShapeRotateTracker.REQ_ROTATE_CHILD.equals(request.getType())) {
			return getRotateChildrenCommand((RotateRequest) request);
		}

		return super.getCommand(request);
	}
	
	protected Command getRotateChildrenCommand(RotateRequest request) {
		CompoundCommand rotate = new CompoundCommand();
		Command c;
		GraphicalEditPart child;
		List<?> children = request.getEditParts();

		for (int i = 0; i < children.size(); i++) {
			child = (GraphicalEditPart) children.get(i);
			c = createRotateCommand(request,child);
			rotate.add(c);
		}
		return rotate.unwrap();
	}

	private Command createRotateCommand(RotateRequest request, GraphicalEditPart child) {
		return new RotateCommand((FDShape)child.getModel());
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
		FDShape shape = (FDShape) child.getModel();
		Rectangle rect = (Rectangle) constraint;
		return new FDShapeMoveAndResizeCommand(shape, rect);
	}
	
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new FDShapeResizableEditPolicy();
	}
	
	protected Command getCloneCommand(ChangeBoundsRequest request) {
		FDContainer container = (FDContainer)getHost().getModel();
		ArrayList<?> editParts = (ArrayList<?>)request.getEditParts();
		Map<FDShape, List<FDWire>> copiedShapesAndWires = FDShapeCloneCommand.cloneShapesWithWires(editParts, request.getMoveDelta());
		
		return new FDShapeCloneCommand((FDRoot)container, container, copiedShapesAndWires);
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
