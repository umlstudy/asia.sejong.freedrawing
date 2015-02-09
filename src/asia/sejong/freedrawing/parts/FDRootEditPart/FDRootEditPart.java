package asia.sejong.freedrawing.parts.FDRootEditPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.SWT;

import asia.sejong.freedrawing.debug.ForEditPart;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.FDWireEndPoint;
import asia.sejong.freedrawing.model.listener.FDRootListener;
import asia.sejong.freedrawing.parts.FDContainerEditPart.FDContainerXYLayoutEditPolicy;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.FDPolygonCreatePolicy;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPolicy;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireableEditPart;

/**
 * The {@link EditPart} for the {@link GenealogyGraph} model object. This EditPart is
 * responsible for creating the layer in which all other figures are placed and for
 * returning the collection of top level model objects to be displayed in that layer.
 */
public class FDRootEditPart extends AbstractGraphicalEditPart implements FDWireableEditPart, NodeEditPart, FDRootListener {
	
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
		
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new FDWireEditPolicy());
		
		// Handles constraint changes (e.g. moving and/or resizing) of model elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FDContainerXYLayoutEditPolicy());
		
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
		
		installEditPolicy("POLYGON_ROLE", new FDPolygonCreatePolicy());
	}
	
	@Override
	protected List<FDShape> getModelChildren() {
		return getModel().getChildren();
	}
	
	@Override
	protected List<FDWire> getModelSourceConnections() {
		// this model is source
		return getModel().getOutgoingWires();
	}

	@Override
	protected List<FDWire> getModelTargetConnections() {
		// this model is target
		return getModel().getIncommingWires();
	}
	
	//============================================================
	// FDWireableEditPart
	
	public void addSourceConnection(FDWireEditPart wireEditPart) {
		addSourceConnection(wireEditPart, 0);
	}

	public void addTargetConnection(FDWireEditPart wireEditPart) {
		addTargetConnection(wireEditPart, 0);
	}
	
	public void removeSourceConnection_(FDWireEditPart wireEditPart) {
		removeSourceConnection(wireEditPart);
	}

	public void removeTargetConnection_(FDWireEditPart wireEditPart) {
		removeTargetConnection(wireEditPart);
	}
	
	// ==========================================================================
	// NodeEditPart
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new XYAnchor(((DropRequest)request).getLocation());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new XYAnchor(((DropRequest)request).getLocation());
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		FDWire wire = (FDWire)connection.getModel();
		FDWireEndPoint source = (FDWireEndPoint)wire.getSource();
		return new XYAnchor(source.getLocation());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		FDWire wire = (FDWire)connection.getModel();
		FDWireEndPoint target = (FDWireEndPoint)wire.getTarget();
		return new XYAnchor(target.getLocation());
	}
	
	// ==========================================================================
	// For Debug
	
	public Command getCommand(Request request) {
		Command command = super.getCommand(request);
		ForEditPart.traceRequest(this, request, command);
		return command;
	}
	
	// ==========================================================================
	// FDRootListener
	
	@Override
	public void wireAdded(FDWire wire) {
		FDWireEndPoint source = wire.getSource();
		FDWireEndPoint target = wire.getTarget();
		
		FDWireEditPart wireEditPart = (FDWireEditPart)findEditPart(wire);
		Assert.isTrue(wireEditPart == null);
		wireEditPart = (FDWireEditPart)createConnection(wire);
		
		// source edit part
		FDWireableEditPart sourceEditPart = null;
		if ( source instanceof FDShape ) {
			sourceEditPart = (FDWireableEditPart)findEditPart(source);
		} else {
			sourceEditPart = this;
		}
		Assert.isTrue(sourceEditPart != null);
		sourceEditPart.addSourceConnection(wireEditPart);
		
		// target edit part
		FDWireableEditPart targetEditPart = null;
		if ( target instanceof FDShape ) {
			targetEditPart = (FDWireableEditPart)findEditPart(target);
		} else {
			targetEditPart = this;
		}
		Assert.isTrue(targetEditPart != null);
		targetEditPart.addTargetConnection(wireEditPart);
		
		for ( int idx=0; idx<wire.getBendpoints().size(); idx++ ) {
			wireEditPart.bendpointAdded(idx, wire.getBendpoints().get(idx));
		}
	}

	@Override
	public void wireRemoved(FDWire wire) {
		FDWireEndPoint source = wire.getSource();
		FDWireEndPoint target = wire.getTarget();
		
		FDWireEditPart wireEditPart = (FDWireEditPart)findEditPart(wire);
		Assert.isTrue(wireEditPart != null);
		
		// source edit part
		FDWireableEditPart sourceEditPart = null;
		if ( source instanceof FDShape ) {
			sourceEditPart = (FDWireableEditPart)findEditPart(source);
		} else {
			sourceEditPart = this;
		}
		Assert.isTrue(sourceEditPart != null);
		sourceEditPart.removeSourceConnection_(wireEditPart);
		
		// target edit part
		FDWireableEditPart targetEditPart = null;
		if ( target instanceof FDShape ) {
			targetEditPart = (FDWireableEditPart)findEditPart(target);
		} else {
			targetEditPart = this;
		}
		Assert.isTrue(targetEditPart != null);
		targetEditPart.removeTargetConnection_(wireEditPart);
		
		removeChild(wireEditPart);
	} 
	
	@Override
	public void routerChanged(Integer newConnectionRouter) {
		refreshVisuals();
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
	
//	// FIXME
//	// FOR DEBUG
//	@Override
//	protected void removeChild(EditPart child) {
//		Assert.isNotNull(child);
//		int index = getChildren().indexOf(child);
//		if (index < 0)
//			return;
//		fireRemovingChild(child, index);
//		if (isActive())
//			child.deactivate();
//		child.removeNotify();
//		removeChildVisual(child);
//		child.setParent(null);
//		getChildren().remove(child);
//	}
	
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
	
	@Override
	protected void refreshVisuals() {
		Animation.markBegin();
		ConnectionLayer connectionLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);

		if ( (getViewer().getControl().getStyle() & SWT.MIRRORED) == 0 ) {
			connectionLayer.setAntialias(SWT.ON);
		}

		if ( getModel().getConnectionRouter().equals(FDRoot.ROUTER_MANUAL) ){
			AutomaticRouter router = new FanRouter();
			router.setNextRouter(new BendpointConnectionRouter());
			connectionLayer.setConnectionRouter(router);
		} else {
			connectionLayer.setConnectionRouter(new ShortestPathConnectionRouter(getFigure()));
		}
		
		Animation.run(150);
	}
	
	protected EditPart findEditPart(Object model) {
		if (model == null) {
			return null;
		}
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		return (EditPart)registry.get(model);
	}
}