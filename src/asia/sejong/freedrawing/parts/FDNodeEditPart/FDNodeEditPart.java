package asia.sejong.freedrawing.parts.FDNodeEditPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Text;

import asia.sejong.freedrawing.figures.FDRectangleFigure;
import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.listener.FDNodeListener;
import asia.sejong.freedrawing.parts.FDConnectionEditPart.FDConnectionEditPart;
import asia.sejong.freedrawing.parts.FDNodeEditPart.cmd.CreateFDConnectionCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.cmd.DeleteFDNodeCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.cmd.TextChangeCommand;
import asia.sejong.freedrawing.parts.common.AbstractNodeEditPart;
import asia.sejong.freedrawing.resources.ContextManager;
import asia.sejong.freedrawing.util.DebugUtil;

public class FDNodeEditPart extends AbstractNodeEditPart implements NodeEditPart, FDNodeListener {
	
	public FDNodeEditPart(FDNode element) {
		setModel(element);
	}
	
	/**
	 * Update the figure based upon the current model state
	 */
	protected void refreshVisuals() {
		FDNode m = (FDNode) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		
		setText(m.getText());
		setFont(m.getFontInfo());
		setBorderColor(m.getBorderColor());
		
		super.refreshVisuals();
	}
	
	private void setFont(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		((FDRectangleFigure)getFigure()).setFont(font);
	}
	
	private void setBorderColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
		}
		((FDRectangleFigure)getFigure()).setBorderColor(color);
	}

	private void setText(String newText) {
		((Label)getFigure()).setText(newText);
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
	protected List<FDConnection> getModelSourceConnections() {
		// this -> modelX ?
		List<FDConnection> list = new ArrayList<FDConnection>();
		for ( FDNode target : getModel().getTargets() ) {
			list.add(FDConnection.newInstance(getModel(), target));
		}
		return list;
	}

	@Override
	protected List<FDConnection> getModelTargetConnections() {
		// nodeX -> this ?
		List<FDConnection> list = new ArrayList<FDConnection>();
		for ( FDNode source : getModel().getSources() ) {
			list.add(FDConnection.newInstance(source, getModel()));
		}
		return list;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new FDNodeEditPolicy());
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new OrderedLayoutFDNodeEditPolicy());
		
		// Handles deleting the selected person
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			protected Command createDeleteCommand(GroupRequest request) {
				return new DeleteFDNodeCommand(getNodeRoot(), getModel());
			}
		});
		
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DirectEditPolicy() {
			
			@Override
			protected void showCurrentEditValue(DirectEditRequest request) {
			}
			
			@Override
			protected Command getDirectEditCommand(DirectEditRequest request) {
				String newValue = (String)request.getCellEditor().getValue();
				if ( !newValue.equals(getModel().getText() ) ) {
					return new TextChangeCommand(getModel(), newValue);
				}
				return null;
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 * Direct Edit
	 */
	public void performRequest(Request req) {
		if ( req != null && req.getType().equals(RequestConstants.REQ_DIRECT_EDIT) ) {
			
			DirectEditManager dem = new DirectEditManager(this, TextCellEditor.class, null) {
				@Override
				protected void initCellEditor() {
					String text = ((FDRectangleFigure) getFigure()).getText();
					getCellEditor().setValue(text);
				}
			};
			dem.setLocator(new CellEditorLocator() {
				@Override
				public void relocate(final CellEditor celleditor) {
					final IFigure figure = getFigure();
					final Text text = (Text) celleditor.getControl();
					final Rectangle rect = figure.getBounds();
//					figure.translateToAbsolute(rect);
//					final Point size = text.computeSize(SWT.DEFAULT,
//							SWT.DEFAULT, true);
//					final org.eclipse.swt.graphics.Rectangle trim = text
//							.computeTrim(0, 0, 0, 0);
//					rect.translate(trim.x, trim.y);
//					rect.width = Math.max(size.x, rect.width);
//					rect.width += trim.width;
//					rect.height += trim.height;
//					text.setBounds(rect.x, rect.y, rect.width, rect.height);
					text.setBounds(rect.x, rect.y, rect.width, rect.height);
				}
			});
			dem.show();
		}
	}

	//============================================================
	// FDNodeListener
	
	@Override
	public void sourceAdded(FDNode source) {
		FDConnection conn = FDConnection.newInstance(source, getModel());
		ConnectionEditPart part = findConnection(conn);
		
		if (part == null) {
			part = createOrFindConnection(conn);
			addTargetConnection(part, 0);
		}
	}

	@Override
	public void sourceRemoved(FDNode source) {
		// remove connection
		FDConnection conn = FDConnection.newInstance(source, getModel());
		ConnectionEditPart part = findConnection(conn);
		if ( conn != null ) {
			removeTargetConnection(part);
		}
		return;
	}

	@Override
	public void targetAdded(FDNode target, List<Point> targetBendpoints) {
		FDConnection conn = FDConnection.newInstance(getModel(), target);
		FDConnectionEditPart part = (FDConnectionEditPart)findConnection(conn);
		if ( part == null ) {
			throw new RuntimeException();
		}
		part.setBendpoints(targetBendpoints);
		
		addSourceConnection(part, 0);
	}

	@Override
	public void targetRemoved(FDNode target) {
		// remove connection
		FDConnection conn = FDConnection.newInstance(getModel(), target);
		ConnectionEditPart part = findConnection(conn);
		if ( conn != null ) {
			removeSourceConnection(part);
		}
	}
	
	@Override
	public void textChanged(String newText) {
		setText(newText);
	}

	@Override
	public void borderColorChanged(RGB rgbColor) {
		setBorderColor(rgbColor);
		getFigure().repaint();
	}

	@Override
	public void fontChanged(FontInfo fontInfo) {
		setFont(fontInfo);
		getFigure().repaint();
	}

	@Override
	public void bendpointAdded(int locationIndex, Point location, FDNode target) {
		FDConnectionEditPart connectionEditPart = (FDConnectionEditPart)findConnection(FDConnection.newInstance(getModel(), target));
		connectionEditPart.bendpointAdded(locationIndex, location);
	}

	@Override
	public void bendpointRemoved(int locationIndex, FDNode target) {
		FDConnectionEditPart connectionEditPart = (FDConnectionEditPart)findConnection(FDConnection.newInstance(getModel(), target));
		connectionEditPart.bendpointRemoved(locationIndex);
	}

	@Override
	public void bendpointMoved(int locationIndex, Point newPoint, FDNode target) {
		FDConnectionEditPart connectionEditPart = (FDConnectionEditPart)findConnection(FDConnection.newInstance(getModel(), target));
		connectionEditPart.bendpointMoved(locationIndex, newPoint);
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