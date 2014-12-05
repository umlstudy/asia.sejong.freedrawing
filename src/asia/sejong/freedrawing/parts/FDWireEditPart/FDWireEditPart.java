package asia.sejong.freedrawing.parts.FDWireEditPart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RoutingAnimator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.debug.ForEditPart;
import asia.sejong.freedrawing.figures.FDWireFigure;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.FDWireBendpoint;
import asia.sejong.freedrawing.model.listener.FDWireListener;
import asia.sejong.freedrawing.parts.FDWireEditPart.command.FDWireDeleteCommand;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDWireEditPart extends AbstractConnectionEditPart implements FDWireListener, PropertyChangeListener {

	protected static final PointList ARROWHEAD = new PointList(new int[]{
			0, 0, -2, 2, -2, 0, -2, -2, 0, 0
		});

	public FDWireEditPart(FDWire connection) {
		setModel(connection);
	}
	
	@Override
	protected IFigure createFigure() {
		return createWireFigure(true);
	}

	@Override
	public FDWire getModel() {
		return (FDWire) super.getModel();
	}
	
	@Override
	public FDWireFigure getFigure() {
		return (FDWireFigure)super.getFigure();
	}
	
	public static FDWireFigure createWireFigure(boolean custom) {
		FDWireFigure wireFigure = null;
		if ( custom ) {
			wireFigure = new FDWireFigure();
			wireFigure.setLineWidth(3);
			wireFigure.setAntialias(1);
		} else {
			wireFigure = new FDWireFigure();
			wireFigure.setLineStyle(SWT.LINE_DASH);
			wireFigure.setAlpha(180);
			wireFigure.setBackgroundColor(ColorConstants.blue);
			wireFigure.setLineWidth(4);
			wireFigure.setAntialias(5);
		}

		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.darkGray);
		wireFigure.setTargetDecoration(decoration);
		
		wireFigure.addRoutingListener(RoutingAnimator.getDefault());

		return wireFigure;
	}
	
	private final FDRoot getRootModel() {
		return (FDRoot)getViewer().getContents().getModel();
	}

	@Override
	protected ConnectionAnchor getSourceConnectionAnchor() {
		return super.getSourceConnectionAnchor();
	}

	@Override
	protected ConnectionAnchor getTargetConnectionAnchor() {
		return super.getTargetConnectionAnchor();
	}
	

	@Override
	protected void createEditPolicies() {
		ConnectionEndpointEditPolicy selectionPolicy = new ConnectionEndpointEditPolicy();
		
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);

		// Handles deleting the receiver
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy() {
			@Override
			protected Command getDeleteCommand(GroupRequest request) {
				return new FDWireDeleteCommand(getRootModel(), getModel());
			}
		});
		
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new FDWireBendpointEditPolicy());
	}
	
	protected void refreshBendpoints() {
		if (getConnectionFigure().getConnectionRouter() instanceof ManhattanConnectionRouter)
			return;
		List<FDWireBendpoint> modelConstraint = getModel().getBendpoints();
		List<AbsoluteBendpoint> figureConstraint = new ArrayList<AbsoluteBendpoint>();
		for (int i = 0; i < modelConstraint.size(); i++) {
			FDWireBendpoint wbp = (FDWireBendpoint) modelConstraint.get(i);
//			RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
//			rbp.setRelativeDimensions(wbp.getFirstRelativeDimension(), wbp.getSecondRelativeDimension());
//			rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
//			figureConstraint.add(rbp);
			
			AbsoluteBendpoint bp = new AbsoluteBendpoint(wbp);
			figureConstraint.add(bp);
		}
		
		getConnectionFigure().setRoutingConstraint(figureConstraint);
	}
	
	//============================================================
	// For Debug
	
	@Override
	public Command getCommand(Request request) {
		Command command = super.getCommand(request);
		ForEditPart.traceRequest(this, request, command);
		return command;
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
	}
	
	//============================================================
	// FDWireListener
	
	@Override
	public void bendpointAdded(int locationIndex, Point location) {
		refreshBendpoints();
	}

	@Override
	public void bendpointRemoved(int locationIndex) {
		refreshBendpoints();
	}
	
	@Override
	public void bendpointMoved(int locationIndex, Point newPoint) {
		refreshBendpoints();
	}
	
	@Override
	public void borderColorChanged(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			getFigure().setForegroundColor(color);
		}
	}

	//============================================================
	// AbstractConnectionEditPart
	
	@Override
	public void addNotify() {
		super.addNotify();
		getModel().addListener(this);
	}
	
	@Override
	public void removeNotify() {
		getModel().removeListener(this);
		super.removeNotify();
	}
	
	@Override
	public void activateFigure() {
		super.activateFigure();
		getFigure().addPropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
	}
	
	@Override
	public void deactivateFigure() {
		getFigure().removePropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
		super.deactivateFigure();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (Connection.PROPERTY_CONNECTION_ROUTER.equals(property)) {
			refreshBendpoints();
			// TODO
			// refreshBendpointEditPolicy();
		}
	}
}
