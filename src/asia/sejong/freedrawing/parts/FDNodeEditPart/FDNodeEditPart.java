package asia.sejong.freedrawing.parts.FDNodeEditPart;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import asia.sejong.freedrawing.figures.FDRectangleFigure;
import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.listener.FDNodeListener;
import asia.sejong.freedrawing.parts.FDConnectionEditPart.FDConnectionEditPart;
import asia.sejong.freedrawing.parts.common.AbstractNodeEditPart;
import asia.sejong.freedrawing.util.DebugUtil;

public class FDNodeEditPart extends AbstractNodeEditPart implements NodeEditPart, FDNodeListener {
	
	public FDNodeEditPart(FDNode element) {
		setModel(element);
		((Label)getFigure()).setText("A " + Integer.toHexString(System.identityHashCode(element)));
	}
	
	/**
	 * Update the figure based upon the current model state
	 */
	protected void refreshVisuals() {
		FDNode m = (FDNode) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}

	
	public FDNode getModel() {
		return (FDNode) super.getModel();
	}
	
	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection originates along
	 * the {@link PersonFigure}'s bounding box. This is called once a connection has been
	 * established.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection creation
	 * originates along the {@link PersonFigure}'s bounding box. 
	 * This is called when the user is interactively creating a connection.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request).getConnectionEditPart();
			if (!(part instanceof ConnectionEditPart)) {
				return null;
			}
			
			FDConnectionEditPart connPart = (FDConnectionEditPart) part;
			CreateFDConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidSource(getModel())) {
				return null;
			}
			return new ChopboxAnchor(getFigure());
		} else {
			// 货肺款 目池记
			return new ChopboxAnchor(getFigure());
		}
	}

	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection terminates along
	 * the {@link PersonFigure}'s bounding box. This is called once a connection has been
	 * established.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	/**
	 * If this request is creating a connection from a {@link MarriageEditPart} to the
	 * receiver, then return an instance of {@link ChopboxAnchor} so that the connection
	 * creation snaps to the figure and terminates along the {@link PersonFigure}'s
	 * bounding box. If the connection source is NOT from a {@link MarriageEditPart} then
	 * return <code>null</code> so that the connection does not appear to connect to the receiver. 
	 * This is called when the user is interactively creating a connection.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof CreateConnectionRequest) {
			Command cmd = ((CreateConnectionRequest) request).getStartCommand();
			if (!(cmd instanceof CreateFDConnectionCommand))
				return null;
			if (!((CreateFDConnectionCommand) cmd).isValidTarget(getModel()))
				return null;
			return new ChopboxAnchor(getFigure());
		}
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request).getConnectionEditPart();
			if (!(part instanceof FDConnectionEditPart))
				return null;
			FDConnectionEditPart connPart = (FDConnectionEditPart) part;
			CreateFDConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidTarget(getModel()))
				return null;
			return new ChopboxAnchor(getFigure());
		}
		return null;
	}
	
	public Command getCommand(Request request) {
		DebugUtil.printLogStart();
		return super.getCommand(request);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can add itself as a listener to the underlying model object
	 */
	public void addNotify() {
		super.addNotify();
		getModel().addFDNodeListener(this);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can stop listening to events from the underlying model object
	 */
	public void removeNotify() {
		getModel().removeFDNodeListener(this);
		super.removeNotify();
	}
	
	/**
	 * Create and return the figure representing this model object
	 */
	protected IFigure createFigure() {
		return new FDRectangleFigure();
	}

	/**
	 * Answer the rectangle figure associated with the receiver
	 */
	protected FDRectangleFigure getRectangleFigure() {
		return (FDRectangleFigure) getFigure();
	}
	

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new FDNodeEditPolicy());
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new OrderedLayoutFDNodeEditPolicy());
	}

	@Override
	public void sourceChanged(FDNode oldSource, FDNode newSource) {
		if ( newSource == null ) {
			// remove connection
			FDConnection conn = getNodeRoot().findConnection(oldSource, getModel());
			ConnectionEditPart part = findConnection(conn);
			if ( conn != null ) {
				removeTargetConnection(part);
			}
			return;
		} else if ( oldSource !=null && oldSource != newSource ) {
			// ReTarget
		} else {
			// New Target
			FDConnection conn = getNodeRoot().createOrFindConnection(newSource, getModel());
			if ( conn != null ) {
				ConnectionEditPart part = findConnection(conn);
				
				if (part == null) {
					part = createOrFindConnection(conn);
				}
				
				if ( !part.getTargetConnections().contains(part) ) {
					addTargetConnection(part, 0);
				}
			}
		}
	}

	@Override
	public void targetChanged(FDNode oldTarget, FDNode newTarget) {
		if ( newTarget == null ) {
			// remove connection
			FDConnection conn = getNodeRoot().findConnection(getModel(), oldTarget);
			getNodeRoot().removeConnection(conn);
			ConnectionEditPart part = findConnection(conn);
			if ( conn != null ) {
				removeSourceConnection(part);
			}
			return;
		} else if ( oldTarget !=null && oldTarget != newTarget ) {
			// ReTarget
		} else {
			// New Target
			FDConnection conn = getNodeRoot().createOrFindConnection(getModel(), newTarget);
			if ( conn != null ) {
				ConnectionEditPart part = findConnection(conn);
				
				if (part == null) {
					part = createOrFindConnection(conn);
				}
					
				if ( !part.getSourceConnections().contains(part) ) {
					addSourceConnection(part, 0);
				}
			}
		}
	}

//	@Override
//	public void connectionSourceSetted(Link connection) {
//		LinkEditPart part = (LinkEditPart)getViewer().getEditPartRegistry().get(connection);
//		addSourceConnection(part, 0);
//	}
//
//	@Override
//	public void connectionTargetSetted(Link connection) {
//		LinkEditPart part = (LinkEditPart)getViewer().getEditPartRegistry().get(connection);
//		addTargetConnection(part, 0);
//	}
//
//	@Override
//	public void childNodeAdded(Node child) {
//		// TODO Auto-generated method stub
//		
//	}
}