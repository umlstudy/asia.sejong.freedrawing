package asia.sejong.freedrawing.parts.FDNodeRootEditPart;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.model.listener.FDNodeRootListener;

/**
 * The {@link EditPart} for the {@link GenealogyGraph} model object. This EditPart is
 * responsible for creating the layer in which all other figures are placed and for
 * returning the collection of top level model objects to be displayed in that layer.
 */
public class FDNodeRootEditPart extends AbstractGraphicalEditPart implements FDNodeRootListener {
	
	public FDNodeRootEditPart(FDNodeRoot freedrawingData) {
		setModel(freedrawingData);
	}

	public FDNodeRoot getModel() {
		return (FDNodeRoot) super.getModel();
	}

	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}

//	/**
//	 * Return a collection of top level genealogy model objects to be displayed
//	 */
//	protected List<GenealogyElement> getModelChildren() {
//		List<GenealogyElement> allObjects = new ArrayList<GenealogyElement>();
//		allObjects.addAll(getModel().getMarriages());
//		allObjects.addAll(getModel().getPeople());
//		allObjects.addAll(getModel().getNotes());
//		return allObjects;
//	}

	protected void createEditPolicies() {
		
		// Disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		
		// Handles constraint changes (e.g. moving and/or resizing) of model elements
		// and creation of new model elements
		installEditPolicy("EditPolicy.LAYOUT_ROLE", new XYLayoutEditPolicy() {
			
			protected Command getCreateCommand(CreateRequest request) {
				Object type = request.getNewObjectType();
				Rectangle box = (Rectangle) getConstraintFor(request);
				FDNodeRoot nodeRoot = getModel();
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
				//return new MoveAndResizeGenealogyElementCommand(elem, box);
				return null;
			}
			
			/**
			 * Exclude MarriageEditParts from being resized
			 */
			protected EditPolicy createChildEditPolicy(EditPart child) {
//				if (child instanceof MarriageEditPart)
//					return new NonResizableMarriageEditPolicy();
				return super.createChildEditPolicy(child);
			}
			
//			/**
//			 * Called when a Note is dragged from a Person on the canvas.
//			 * Return a command for reparenting the note
//			 */
//			protected Command createAddCommand(EditPart child, Object constraint) {
//				NoteContainer oldContainer = (NoteContainer) child.getParent().getModel();
//				if (getModel() == oldContainer)
//					return null;
//				Note note = (Note) child.getModel();
//				ReparentNoteCommand cmd = new ReparentNoteCommand(getModel(), note);
//				cmd.setBounds((Rectangle) constraint);
//				cmd.setOldContainer(oldContainer);
//				return cmd;
//			}
		});
	}
	
	// ===============================================================
	// FDRootNodeListener

	@Override
	public void childNodeAdded(FDNode child) {
		addChild(createChild(child), 0);
	}

	/**
	 * Override the superclass implementation so that the receiver
	 * can add itself as a listener to the underlying model object
	 */
	public void addNotify() {
		super.addNotify();
		getModel().addNodeRootListener(this);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can stop listening to events from the underlying model object
	 */
	public void removeNotify() {
		getModel().removeNodeRootListener(this);
		super.removeNotify();
	}
	

//	// ===============================================================
//	// GenealogyGraphListener
//
//	/**
//	 * When a person is added to the model,
//	 * add a new EditPart to manage the corresponding figure.
//	 */
//	public void personAdded(Person p) {
//		addChild(createChild(p), 0);
//	}
//
//	/**
//	 * When a person is removed from the model,
//	 * find and remove the corresponding edit part
//	 */
//	public void personRemoved(Person p) {
//		genealogyElementRemoved(p);
//	}
//
//	/**
//	 * When a marriage is added to the model,
//	 * add a new EditPart to manage the corresponding figure.
//	 */
//	public void marriageAdded(Marriage m) {
//		addChild(createChild(m), 0);
//	}
//
//	/**
//	 * When a marriage is removed from the model,
//	 * find and remove the corresponding edit part
//	 */
//	public void marriageRemoved(Marriage m) {
//		genealogyElementRemoved(m);
//	}
//
//	/**
//	 * When a note is added to the model,
//	 * add a new EditPart to manage the corresponding figure.
//	 */
//	public void noteAdded(int index, Note n) {
//		addChild(createChild(n), index);
//	}
//
//	/**
//	 * When a note is removed from the model,
//	 * find and remove the corresponding edit part
//	 */
//	public void noteRemoved(Note n) {
//		genealogyElementRemoved(n);
//	}
//
//	/**
//	 * When an element is removed from the model,
//	 * find and remove the corresponding edit part
//	 */
//	private void genealogyElementRemoved(GenealogyElement elem) {
//		Object part = getViewer().getEditPartRegistry().get(elem);
//		if (part instanceof EditPart)
//			removeChild((EditPart) part);
//	}
//
//	public void graphCleared() {
//	}
}