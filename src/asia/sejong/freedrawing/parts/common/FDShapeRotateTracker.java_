package asia.sejong.freedrawing.parts.common;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;

import asia.sejong.freedrawing.parts.FDShapeEditPart.FDShapeEditPart;
import asia.sejong.freedrawing.parts.FDShapeEditPart.request.RotateRequest;

public final class FDShapeRotateTracker extends DragEditPartsTracker {

	public static String REQ_ROTATE = "rotate"; //$NON-NLS-1$
	public static String REQ_ROTATE_CHILD = "rotate_child"; //$NON-NLS-1$
	
	public FDShapeRotateTracker(GraphicalEditPart owner) {
		super(owner);
	}

//	protected Dimension getMaximumSizeFor(ChangeBoundsRequest request) {
//		// TODO
//		return IFigure.MAX_DIMENSION;
//	}
//
//	protected Dimension getMinimumSizeFor(ChangeBoundsRequest request) {
//		return IFigure.MIN_DIMENSION;
//		//return LogicPlugin.getMinimumSizeFor(getOwner().getModel().getClass());
//	}

	@Override
	protected String getCommandName() {
		return REQ_ROTATE;
	}
	
	@Override
	protected Request createTargetRequest() {
		return new RotateRequest(FDShapeRotateTracker.REQ_ROTATE);
	}
	
	@Override
	protected Command getCommand() {
		List<?> editparts = getOperationSet();
		EditPart part;
		CompoundCommand command = new CompoundCommand();
		command.setDebugLabel("Rotate Handle Tracker");//$NON-NLS-1$
		for (int i = 0; i < editparts.size(); i++) {
			part = (EditPart) editparts.get(i);
			command.add(part.getCommand(getTargetRequest()));
		}
		return command.unwrap();
	}

	@Override
	protected void updateTargetRequest() {
		RotateRequest request = (RotateRequest) getTargetRequest();
		request.setEditParts(getOperationSet());
		request.getExtendedData().clear();
		request.setLocation(getLocation());
		request.setType(getCommandName());
	}
}
