package asia.sejong.freedrawing.parts.FDNodeRootEditPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.RectangleFigure;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.parts.FDNodeRootEditPart.cmd.CreateFDNodeCommand;
import asia.sejong.freedrawing.parts.FDNodeRootEditPart.cmd.MoveAndResizeFDNodeCommand;

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
				graphics.translate(-bounds.x, -bounds.y);
				IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
				figure.paint(graphics);
				
				Color background = gc.getBackground ();
				Pattern p = new Pattern (Display.getCurrent(), 0, 0, 0, bounds.height, background, 100, background, 180);
				gc.setBackgroundPattern (p);
				gc.fillRectangle (0, 0, bounds.width, bounds.height);
				p.dispose ();
				
			    gc.dispose();
			    graphics.dispose();
			    
			    ImageFigure imageFigure = new ImageFigure(image);
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
			List<FDNode> copiedNodes = new ArrayList<FDNode>();
			for ( Object selected : editParts ) {
				EditPart selectedEditPart = (EditPart)selected;
				if ( selectedEditPart.getModel() instanceof FDNode ) {
					FDNode copiedNode = ((FDNode) selectedEditPart.getModel()).clone();
					Point delta = request.getMoveDelta();
					copiedNode.setLocation(copiedNode.getX() + delta.x, copiedNode.getY()+delta.y);
					copiedNodes.add(copiedNode);
				}
			}
			
			if ( copiedNodes.size() > 0 ) {
				CompoundCommand compoundCommand = new CompoundCommand();
				for ( FDNode node : copiedNodes ) {
					compoundCommand.add(new CreateFDNodeCommand(container, node));
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
