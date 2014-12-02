package asia.sejong.freedrawing.parts.FDShapeEditPart;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.debug.ForEditPart;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.model.listener.FDShapeListener;
import asia.sejong.freedrawing.parts.FDRectEditPart.FDRectEditPart;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.FDShapeDeleteCommand;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;

public abstract class FDShapeEditPart extends AbstractGraphicalEditPart implements NodeEditPart, FDShapeListener {

	protected final EditPart findEditPart(Object model) {
		if (model == null) {
			return null;
		}
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		return (EditPart)registry.get(model);
	}
	
	protected final FDRoot getRootModel() {
		return (FDRoot)getViewer().getContents().getModel();
	}
	
	public FDShape getModel() {
		return (FDShape) super.getModel();
	}
	
	protected void refreshVisuals() {
		
		setBorderColor(getModel().getBorderColor());
		
		super.refreshVisuals();
	}
	
	protected abstract void setBorderColor(RGB rgbColor);

	protected String getEditPolicyName(String name) {
		return String.format("%s.%s", this.getClass().getSimpleName(), name);
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(getEditPolicyName(EditPolicy.GRAPHICAL_NODE_ROLE), new FDShapeEditPolicy());
		
		installEditPolicy(getEditPolicyName(EditPolicy.LAYOUT_ROLE), new FDShapeOrderedLayoutEditPolicy());
		
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
		
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			protected Command createDeleteCommand(GroupRequest request) {
				return new FDShapeDeleteCommand(getRootModel(), getModel());
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
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
		// create 와 recreate 에 따른 화살표 모양 생성 가능
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
	
	/**
	 * 노드위로 노드가 지나갈 때 무브 액션이 활성화 되도록 함
	 */
	@Override
	public EditPart getTargetEditPart(Request request) {
		EditPart targetEditPart = super.getTargetEditPart(request);
		if ( request instanceof ChangeBoundsRequest && targetEditPart instanceof FDRectEditPart ) {
			return targetEditPart.getParent();
		}
		return targetEditPart;
	}

	//============================================================
	// protected method adapter
	
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
	
	//============================================================
	// For Debug
	
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
	
	public Command getCommand(Request request) {
		Command command = super.getCommand(request);
		ForEditPart.traceRequest(this, request, command);
		return command;
	}
	
	//============================================================
	// AbstractGraphicalEditPart
	
	public void addNotify() {
		super.addNotify();
		((FDShape)getModel()).addListener(this);
	}
	
	public void removeNotify() {
		((FDShape)getModel()).removeListener(this);
		super.removeNotify();
	}
	
	// ==========================================================================
	// FDShapeListener
	
	@Override
	public final void locationChanged(int x, int y) {
		figure.setLocation(new Point(x, y));
		figure.getParent().getLayoutManager().setConstraint(figure, figure.getBounds());
	}

	@Override
	public final void sizeChanged(int width, int height) {
		getFigure().setSize(width, height);
		figure.getParent().getLayoutManager().setConstraint(figure, figure.getBounds());
	}

	@Override
	public final void borderColorChanged(RGB rgbColor) {
		setBorderColor(rgbColor);
		getFigure().repaint();
	}
}
