package asia.sejong.freedrawing.parts.FDRootEditPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import asia.sejong.freedrawing.debug.ForEditPart;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.listener.FDRootListener;
import asia.sejong.freedrawing.parts.FDRectEditPart.FDRectEditPart;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;

/**
 * The {@link EditPart} for the {@link GenealogyGraph} model object. This EditPart is
 * responsible for creating the layer in which all other figures are placed and for
 * returning the collection of top level model objects to be displayed in that layer.
 */
public class FDRootEditPart extends AbstractGraphicalEditPart implements FDRootListener {
	
	public FDRootEditPart(FDRoot nodeRoot) {
		setModel(nodeRoot);
	}

	public FDRoot getModel() {
		return (FDRoot) super.getModel();
	}

	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}

	protected void createEditPolicies() {
		
		// Disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		
		// Handles constraint changes (e.g. moving and/or resizing) of model elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FDRootXYLayoutEditPolicy());
		
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
	}
	
	// ==========================================================================
	// For Debug
	
	public Command getCommand(Request request) {
		Command command = super.getCommand(request);
		ForEditPart.traceRequest(this, request, command);
		return command;
	}
	
	// ==========================================================================
	// FDRootNodeListener
	
	@Override
	public void wireAdded(FDWire wire) {
		FDRect source = wire.getSource();
		FDRect target = wire.getTarget();
		
		FDWireEditPart wireEditPart = (FDWireEditPart)findEditPart(wire);
		Assert.isTrue(wireEditPart == null);
		wireEditPart = (FDWireEditPart)createConnection(wire);
		
		FDRectEditPart sourceEditPart = (FDRectEditPart)findEditPart(source);
		sourceEditPart.addSourceConnection(wireEditPart);
		
		FDRectEditPart targetEditPart = (FDRectEditPart)findEditPart(target);
		targetEditPart.addTargetConnection(wireEditPart);
		
		for ( int idx=0; idx<wire.getBendpoints().size(); idx++ ) {
			wireEditPart.bendpointAdded(idx, wire.getBendpoints().get(idx));
		}
	}

	@Override
	public void wireRemoved(FDWire wire) {
		FDRect source = wire.getSource();
		FDRect target = wire.getTarget();
		
		FDWireEditPart wireEditPart = (FDWireEditPart)findEditPart(wire);
		Assert.isTrue(wireEditPart != null);
		
		FDRectEditPart sourceEditPart = (FDRectEditPart)findEditPart(source);
		sourceEditPart.removeSourceConnection_(wireEditPart);
		
		FDRectEditPart targetEditPart = (FDRectEditPart)findEditPart(target);
		targetEditPart.removeTargetConnection_(wireEditPart);
		
		removeChild(wireEditPart);
	} 
	
	// ===============================================================
	// FDContainerListener

	@Override
	public void childShapeAdded(FDShape child) {
		addChild(createChild(child), -1);
	}
	
	@Override
	public void childShapeRemoved(FDShape child) {
		Object part = getViewer().getEditPartRegistry().get(child);
		if (part instanceof EditPart) {
			removeChild((EditPart) part);
		}
	}
	
	@Override
	public void positionChanged(int newPosition, FDShape child) {
		Object childPart = getViewer().getEditPartRegistry().get(child);
		if (childPart instanceof GraphicalEditPart ) {
			IFigure childFigure = ((GraphicalEditPart) childPart).getFigure();
			@SuppressWarnings("unchecked")
			List<Object> children = (List<Object> )childFigure.getParent().getChildren();
			children.remove( childFigure );
			children.add(newPosition, childFigure );
			childFigure.repaint();
		}
	}

	// ==========================================================================
	// AbstractGraphicalEditPart

	@Override
	public void addNotify() {
		super.addNotify();
		getModel().addNodeRootListener(this);
	}
	
	@Override
	public void removeNotify() {
		getModel().removeNodeRootListener(this);
		super.removeNotify();
	}

	@Override 
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
	    if (key == SnapToHelper.class) {
	        List<SnapToHelper> helpers = new ArrayList<SnapToHelper>();
	        if (Boolean.TRUE.equals(getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED))) {
	            helpers.add(new SnapToGeometry(this));
	        }
	        if (Boolean.TRUE.equals(getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED))) {
	            helpers.add(new SnapToGrid(this));
	        }
	        if(helpers.size()==0) {
	            return null;
	        } else {
	            return new CompoundSnapToHelper(helpers.toArray(new SnapToHelper[0]));
	        }
	    }
	    return super.getAdapter(key);
	}
	
	protected EditPart findEditPart(Object model) {
		if (model == null) {
			return null;
		}
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		return (EditPart)registry.get(model);
	}
}