package asia.sejong.freedrawing.parts.FDRootEditPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.parts.FDRootEditPart.command.FDShapeCreateCommand;
import asia.sejong.freedrawing.parts.FDRootEditPart.command.FDShapeMoveAndResizeCommand;

/**
 * Handles constraint changes (e.g. moving and/or resizing) of model elements
 * and creation of new model elements
 * @author SeJong
 *
 */
public class FDRootXYLayoutEditPolicy extends XYLayoutEditPolicy {
	
	protected Command getCreateCommand(CreateRequest request) {
		Object type = request.getNewObjectType();
		Rectangle box = (Rectangle) getConstraintFor(request);
		FDRoot nodeRoot = (FDRoot)getHost().getModel();
		if (type == FDRect.class) {
			FDRect element = (FDRect) request.getNewObject();
			element.setRectangle(box);
			return new FDShapeCreateCommand(nodeRoot, element);
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
		FDRect node = (FDRect) child.getModel();
		Rectangle rect = (Rectangle) constraint;
		return new FDShapeMoveAndResizeCommand(node, rect);
	}
	
	/**
	 * Exclude MarriageEditParts from being resized
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
//		if (child instanceof MarriageEditPart)
//			return new NonResizableMarriageEditPolicy();
//		return super.createChildEditPolicy(child);
		return new ResizableEditPolicy() {
			protected IFigure createDragSourceFeedbackFigure() {
				// Use a ghost rectangle for feedback
//				RectangleFigure r = new RectangleFigure();
//				FigureUtilities.makeGhostShape(r);
//				r.setLineStyle(Graphics.LINE_DASHDOT);
//				r.setLineWidth(2);
//				r.setForegroundColor(ColorConstants.white);
//				r.setBounds(getInitialFeedbackBounds());
//				r.validate();
//				addFeedback(r);
				
				Rectangle bounds = getInitialFeedbackBounds();
				Image image = new Image(Display.getCurrent(), bounds.width, bounds.height);
				GC gc = new GC(image);
				SWTGraphics graphics = new SWTGraphics(gc);
				graphics.setAlpha(150);
				graphics.translate(-bounds.x, -bounds.y);
				IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
				figure.paint(graphics);
				
//				Color background = gc.getBackground ();
//				Pattern p = new Pattern (Display.getCurrent(), 0, 0, 0, bounds.height, background, 100, background, 180);
//				gc.setBackgroundPattern (p);
//				gc.fillRectangle (0, 0, bounds.width, bounds.height);
//				p.dispose ();
				
			    gc.dispose();
			    graphics.dispose();
			    
			    ImageFigure imageFigure = new ImageFigure(image);
			    imageFigure.setOpaque(true);
			    imageFigure.validate();
			    addFeedback(imageFigure);
			    System.out.println("MOVING FIGURE!");
//			    image.dispose();
				return imageFigure;
			}
		};
	}
	
	protected Command getCloneCommand(ChangeBoundsRequest request) {
		FDContainer container = (FDContainer)getHost().getModel();
		ArrayList<?> editParts = (ArrayList<?>)request.getEditParts();
		if (editParts != null && editParts.size()>0 ) {
			List<FDRect> copiedNodes = new ArrayList<FDRect>();
			for ( Object selected : editParts ) {
				EditPart selectedEditPart = (EditPart)selected;
				if ( selectedEditPart.getModel() instanceof FDRect ) {
					FDRect copiedNode = (FDRect)((FDRect) selectedEditPart.getModel()).clone();
					Point delta = request.getMoveDelta();
					copiedNode.setLocation(copiedNode.getX() + delta.x, copiedNode.getY()+delta.y);
					copiedNodes.add(copiedNode);
				}
			}
			
			if ( copiedNodes.size() > 0 ) {
				CompoundCommand compoundCommand = new CompoundCommand();
				for ( FDRect node : copiedNodes ) {
					compoundCommand.add(new FDShapeCreateCommand(container, node));
				}
				return compoundCommand;
			}
		}
		
		return null;
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
