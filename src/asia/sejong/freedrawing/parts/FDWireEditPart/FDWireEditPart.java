package asia.sejong.freedrawing.parts.FDWireEditPart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
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

import asia.sejong.freedrawing.debug.ForEditPart;
import asia.sejong.freedrawing.figures.FDWireFigure;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.listener.FDWireListener;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.FDWireDeleteCommand;
import asia.sejong.freedrawing.parts.FDNodeEditPart.command.FDWireRecreateCommand;

public class FDWireEditPart extends AbstractConnectionEditPart implements FDWireListener {

	protected static final PointList ARROWHEAD = new PointList(new int[]{
			0, 0, -2, 2, -2, 0, -2, -2, 0, 0
		});

	public FDWireEditPart(FDWire connection) {
		setModel(connection);
	}

	public FDWire getModel() {
		return (FDWire) super.getModel();
	}
	
	// TODO FOR DEBUG
	public Command getCommand(Request request) {
		Command command = super.getCommand(request);
		ForEditPart.traceRequest(this, request, command);
		return command;
	}
	
//	@Override
//	public void sourceChanged(FDElement sourceModel) {
//		DebugUtil.printLogStart();
//		EditPart originalSourceEditPart = getSource();
//		Map<?, ?> registry = getViewer().getEditPartRegistry();
//		if ( registry.get(sourceModel) != originalSourceEditPart ) {
//			if ( registry.get(sourceModel) instanceof AbstractFDNodeEditPart ) {
//				AbstractFDNodeEditPart newEditPart = (AbstractFDNodeEditPart)registry.get(sourceModel);
//				setSource(newEditPart);
//			}
//		}
//		DebugUtil.printLogEnd();
//	}
//
//	@Override
//	public void targetChanged(FDElement targetModel) {
//		DebugUtil.printLogStart();
//		EditPart originalTargetEditPart = getTarget();
//		Map<?, ?> registry = getViewer().getEditPartRegistry();
//		if ( registry.get(targetModel) != originalTargetEditPart ) {
//			if ( registry.get(targetModel) instanceof AbstractFDNodeEditPart ) {
//				AbstractFDNodeEditPart newEditPart = (AbstractFDNodeEditPart)registry.get(targetModel);
//				setTarget(newEditPart);
//			}
//		}
//		DebugUtil.printLogEnd();
//	}
	
	protected IFigure createFigure() {
		return createWireFigure(true);
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

		wireFigure.setConnectionRouter(new BendpointConnectionRouter());

		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.darkGray);
		wireFigure.setTargetDecoration(decoration);

		return wireFigure;
	}
	
//	/**
//	 * Answer a new command to recreate the receiver
//	 */
//	public CreateConnectionCommand recreateCommand() {
//		CreateConnectionCommand cmd;
//		if (getModel().isOffspringConnection()) {
//			cmd = new CreateOffspringConnectionCommand(getModel().marriage);
//			cmd.setTarget(getModel().person);
//		}
//		else {
//			cmd = new CreateSpouseConnectionCommand(getModel().person);
//			cmd.setTarget(getModel().marriage);
//		}
//		return cmd;
//	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getSourceConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getSource() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getSource();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
		return super.getSourceConnectionAnchor();
	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getTargetConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getTarget() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getTarget();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
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
				return new FDWireDeleteCommand(getModel());
			}
		});
		
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new FDWireBendpointEditPolicy());
	}
	
	protected PolylineConnection getConnection() {
		return (PolylineConnection)getFigure();
	}

	public FDWireRecreateCommand recreateCommand() {
		FDRect src = (FDRect)getSource().getModel();
		FDWire wire = src.getWire((FDRect)getTarget().getModel());
		
		FDWireRecreateCommand cmd = new FDWireRecreateCommand(wire);
		cmd.setSource(getSource().getModel());
		cmd.setTarget(getTarget().getModel());
		return cmd;
	}
	
	public void deactivate() {
		super.deactivate();
	}
	
	private List<Bendpoint> getBendpoints() {
		PolylineConnection connection = getConnection();
		BendpointConnectionRouter connectionRouter = (BendpointConnectionRouter)connection.getConnectionRouter();
		@SuppressWarnings("unchecked")
		List<Bendpoint> bendpoints = (List<Bendpoint>)connectionRouter.getConstraint(connection);
		if ( bendpoints == null ) {
			bendpoints = new ArrayList<Bendpoint>();
			connectionRouter.setConstraint(connection, bendpoints);
		}
		
		return bendpoints;
	}
	
	public void connectTarget(FDWire targetWire) {
		PolylineConnection connection = getConnection();
		BendpointConnectionRouter connectionRouter = (BendpointConnectionRouter)connection.getConnectionRouter();
		List<Bendpoint> bendpoints = new ArrayList<Bendpoint>();
		if ( targetWire != null ) {
			for ( Point point : targetWire.getBendpoints()  ) {
				bendpoints.add(new AbsoluteBendpoint(point));
			}
			connectionRouter.setConstraint(connection, bendpoints);
		}
	}
	
	@Override
	public void bendpointAdded(int locationIndex, Point location) {
		List<Bendpoint> bendpoints = getBendpoints();
		AbsoluteBendpoint bp = new AbsoluteBendpoint(location);
		bendpoints.add(locationIndex, bp);
		
		refresh();
	}

	@Override
	public void bendpointRemoved(int locationIndex) {
		List<Bendpoint> bendpoints = getBendpoints();
		bendpoints.remove(locationIndex);

		refresh();
	}
	
	@Override
	public void bendpointMoved(int locationIndex, Point newPoint) {
		AbsoluteBendpoint newBendpoint = new AbsoluteBendpoint(newPoint);
		getBendpoints().set(locationIndex, newBendpoint);
		
		refresh();
	}

	public void addNotify() {
		super.addNotify();
		getModel().addFDWireListener(this);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can stop listening to events from the underlying model object
	 */
	public void removeNotify() {
		getModel().removeFDWireListener(this);
		super.removeNotify();
	}
}
