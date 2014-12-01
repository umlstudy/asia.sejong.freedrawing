package asia.sejong.freedrawing.parts.FDNodeEditPart;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import asia.sejong.freedrawing.debug.ForEditPart;
import asia.sejong.freedrawing.figures.FDRectFigure;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.listener.FDNodeListener;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.DeleteFDNodeCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.TextChangeCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;
import asia.sejong.freedrawing.parts.common.FDShapeEditPart;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDNodeEditPart extends FDShapeEditPart implements NodeEditPart, FDNodeListener {
	
	public FDNodeEditPart(FDRect element) {
		setModel(element);
	}
	
	/**
	 * Update the figure based upon the current model state
	 */
	protected void refreshVisuals() {
		FDRect m = (FDRect) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		
		setText(m.getText());
		setFont(m.getFontInfo());
		setBorderColor(m.getBorderColor());
		
		super.refreshVisuals();
	}
	
	// TODO FOR DEBUG
	public Command getCommand(Request request) {
		Command command = super.getCommand(request);
		ForEditPart.traceRequest(this, request, command);
		return command;
	}
	
	private void setFont(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		((FDRectFigure)getFigure()).setFont(font);
	}
	
	private void setBorderColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
		}
		((FDRectFigure)getFigure()).setBorderColor(color);
	}

	private void setText(String newText) {
		((Label)getFigure()).setText(newText);
	}
	
	public FDRect getModel() {
		return (FDRect) super.getModel();
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
		return new ChopboxAnchor(getFigure());
//		if (request instanceof ReconnectRequest) {
//			FDWireEditPart wireEditPart = (FDWireEditPart)((ReconnectRequest) request).getConnectionEditPart();
//			FDWireRecreateCommand recreateCommand = wireEditPart.recreateCommand();
//			boolean validSource = recreateCommand.isValidSource(getModel());
//			boolean validTarget = recreateCommand.isValidTarget(recreateCommand.getTarget());
//			
//			if (! (validSource && validTarget) ) {
//				return null;
//			}
//			
//			return new ChopboxAnchor(getFigure());
//		} else {
//			// 새로운 커넥션
//			return new ChopboxAnchor(getFigure());
//		}
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
		return new ChopboxAnchor(getFigure());
//		if (request instanceof CreateConnectionRequest) {
//			Command command = ((CreateConnectionRequest) request).getStartCommand();
//			if (!(command instanceof FDWireCreateCommand)) {
//				return null;
//			}
//			
//			FDWireCreateCommand createConnectionCommand = (FDWireCreateCommand) command;
//			FDRect target = getModel();
//			if ( !createConnectionCommand.isValidTarget(target) ) {
//				return null;
//			}
//			if ( createConnectionCommand.getSource().containsTarget(target) ) {
//				return null;
//			}
//			
//			return new ChopboxAnchor(getFigure());
//		}
//		if (request instanceof ReconnectRequest) {
//			EditPart editPart = ((ReconnectRequest) request).getConnectionEditPart();
//			if (!(editPart instanceof FDWireEditPart)) {
//				return null;
//			}
//			
//			FDWireEditPart connectionEditPart = (FDWireEditPart) editPart;
//			FDWireRecreateCommand recreateConnectionCommand = connectionEditPart.recreateCommand();
//			FDRect target = getModel();
//			if ( !recreateConnectionCommand.isValidTarget(target) ) {
//				return null;
//			}
//			if ( recreateConnectionCommand.getSource().containsTarget(target) ) {
//				return null;
//			}
//			
//			return new ChopboxAnchor(getFigure());
//		}
//		return null;
	}
	
//	public Command getCommand(Request request) {
//		DebugUtil.printLogStart();
//		return super.getCommand(request);
//	}
	
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
		return new FDRectFigure();
	}

	/**
	 * Answer the rectangle figure associated with the receiver
	 */
	protected FDRectFigure getRectangleFigure() {
		return (FDRectFigure) getFigure();
	}
	
	@Override
	protected List<FDWire> getModelSourceConnections() {
		// this model is source
		return getModel().getIncommingWires();
	}

	@Override
	protected List<FDWire> getModelTargetConnections() {
		// this model is target
		return getModel().getOutgoingWires();
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new FDNodeEditPolicy());
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FDNodeOrderedLayoutEditPolicy());
		
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
		
		// Handles deleting the selected node
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			protected Command createDeleteCommand(GroupRequest request) {
				return new DeleteFDNodeCommand(getRootModel(), getModel());
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
		
		/*
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ResizableEditPolicy() {
			protected IFigure createDragSourceFeedbackFigure() {
				getFigure();
				RectangleFigure r = new RectangleFigure();
				FigureUtilities.makeGhostShape(r);
				r.setLineStyle(Graphics.LINE_DASHDOTDOT);
				r.setForegroundColor(ColorConstants.white);
				r.setBounds(getInitialFeedbackBounds());
				r.validate();
				return r;
			}
		});
		*/
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 * Direct Edit
	 */
	public void performRequest(Request req) {
		if ( req != null && req.getType().equals(RequestConstants.REQ_DIRECT_EDIT) ) {
			
			DirectEditManager dem = new DirectEditManager(this, null, null) {
				@Override
				protected void initCellEditor() {
					String text = ((FDRectFigure) getFigure()).getText();
					getCellEditor().setValue(text);
				}
				protected CellEditor createCellEditorOn(Composite parent) {
					TextCellEditor cellEditor = new TextCellEditor(parent, SWT.MULTI | SWT.CENTER);
					Text text = (Text)cellEditor.getControl();
					Font font = null;
					FontInfo fontInfo = getModel().getFontInfo();
					if ( fontInfo != null ) {
						font = ContextManager.getInstance().getFontManager().get(fontInfo);
						text.setFont(font);
					}
					
					return cellEditor;
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
	
//	@Override
//	public void sourceAdded(FDRect source) {
////		FDWire wire = FDWire.newInstance(source, getModel());
//		FDWire wire = source.getWire(getModel());
//		Assert.isTrue(wire == null);
//		
//		wire = FDWire.newInstance__(source, getModel());
//		FDWireEditPart part = (FDWireEditPart)findEditPart(wire);
//		Assert.isTrue(part == null);
//		
//		part = (FDWireEditPart)createConnection(wire);
//		addTargetConnection(part, 0);
//	}
//
//	@Override
//	public void sourceRemoved(FDRect source) {
//		// remove sourceConnection
//		
//		FDWire wire = source.getWire(getModel());
//		Assert.isTrue(wire != null);
//		
//		FDWireEditPart part = (FDWireEditPart)findEditPart(wire);
//		Assert.isTrue(part != null);
//		removeTargetConnection(part);
//	}

//	
//	@Override
//	public void targetAdded(FDRect target) {
//		FDRect source = getModel();
//		
//		FDWire wire = source.getWire(target);
//		FDWireEditPart wireEditPart = (FDWireEditPart)findEditPart(wire);
//		Assert.isTrue(wireEditPart == null);
//		wireEditPart = (FDWireEditPart)createConnection(wire);
//		
//		addSourceConnection(wireEditPart, 0);
//		
//		FDNodeEditPart targetEditPart = (FDNodeEditPart)findEditPart(target);
//		targetEditPart.addTargetConnection(wireEditPart, 0);
//	}
//
//	@Override
//	public void targetRemoved(FDRect target, FDWire removedWire) {
//
//		FDWireEditPart wireEditPart = (FDWireEditPart)findEditPart(removedWire);
//		Assert.isTrue(wireEditPart != null);
//		removeSourceConnection(wireEditPart);
//		
//		FDNodeEditPart targetEditPart = (FDNodeEditPart)findEditPart(target);
//		targetEditPart.removeTargetConnection(wireEditPart);
//	}
	
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
//
//	@Override
//	public void bendpointAdded(int locationIndex, Point location, FDRect target) {
//		FDWireEditPart wireEditPart = getWireEditPart(getModel(), target);
//		wireEditPart.bendpointAdded(locationIndex, location);
//	}
//
//	@Override
//	public void bendpointRemoved(int locationIndex, FDRect target) {
//		FDWireEditPart wireEditPart = getWireEditPart(getModel(), target);
//		wireEditPart.bendpointRemoved(locationIndex);
//	}
//
//	@Override
//	public void bendpointMoved(int locationIndex, Point newPoint, FDRect target) {
//		FDWireEditPart wireEditPart = getWireEditPart(getModel(), target);
//		wireEditPart.bendpointMoved(locationIndex, newPoint);
//	}
//	
	// -------------------------------------------
	// FOR DEBUG
	public DragTracker getDragTracker(Request request) {
		return new org.eclipse.gef.tools.DragEditPartsTracker(this) {
			protected Command getCommand() {
				return super.getCommand();
			}
			
			protected String getCommandName() {
				return super.getCommandName();
			}
		};
	}
	
	/**
	 * 노드위로 노드가 지나갈 때 무브 액션이 활성화 되도록 함
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		EditPart targetEditPart = super.getTargetEditPart(request);
//		System.out.println("TARGET EDITPART ? " + request + "," + targetEditPart);
		if ( request instanceof ChangeBoundsRequest && targetEditPart instanceof FDNodeEditPart ) {
			return targetEditPart.getParent();
		}
		return targetEditPart;
	}

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