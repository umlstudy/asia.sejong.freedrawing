package asia.sejong.freedrawing.editor.tools;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.tools.TargetingTool;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.CreatePolygonRequest;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.FDPolygonCreatePolicy;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.FDPolygonEditPart;

public class FDPolygonCreateionTool extends TargetingTool {

	private FDPolygonEditPart polygonEditPart;
	
	private static final int STATE_POLYGON_CREATE_MID = TargetingTool.MAX_STATE << 1;
	private static final int STATE_POLYGON_CREATE_END = TargetingTool.MAX_STATE << 2;
	protected static final int MAX_STATE = STATE_POLYGON_CREATE_END;

	private static final int FLAG_SOURCE_FEEDBACK = TargetingTool.MAX_FLAG << 1;
	protected static final int MAX_FLAG = FLAG_SOURCE_FEEDBACK;

	public FDPolygonCreateionTool(FreedrawingEditor editor) {
	}

	@Override
	protected String getCommandName() {
		if ( isInState(STATE_POLYGON_CREATE_MID | STATE_ACCESSIBLE_DRAG_IN_PROGRESS) ) {
			return FDPolygonCreatePolicy.REQ_POLYGON_CREATE_MID;
		} else if ( isInState(STATE_POLYGON_CREATE_END | STATE_ACCESSIBLE_DRAG_IN_PROGRESS) ) {
			return FDPolygonCreatePolicy.REQ_POLYGON_CREATE_END;
		} else {
			return FDPolygonCreatePolicy.REQ_POLYGON_CREATE_START;
		}
	}
	
	@Override
	protected boolean handleMove() {
		updateTargetRequest();
		updateTargetUnderMouse();
		setCurrentCommand(getCommand());
//		showTargetFeedback();
		showSourceFeedback();
		return true;
	}
	
	@Override
	protected Request createTargetRequest() {
		Request request = new CreatePolygonRequest();
		request.setType(getCommandName());
		return request;
	}
	
	@Override
	protected boolean handleButtonDown(int button) {
		if (button == 1 ) {
			if ( isInState(STATE_POLYGON_CREATE_MID) || stateTransition(STATE_INITIAL, STATE_POLYGON_CREATE_MID ) ) {
				addPolylinePointToRequest();
				updateTargetRequest();
				showSourceFeedback();
				return handleCreatePolyline();
			}
		}
		
		if (button == 3 && stateTransition(STATE_POLYGON_CREATE_MID, STATE_POLYGON_CREATE_END)) {
			addPolylinePointToRequest();
			updateTargetRequest();
			// TODO 지우기 위치로 적절한가?
			eraseSourceFeedback();
			return handleCreatePolyline();
		}
		
//		super.handleButtonDown(button);
//		if (isInState(STATE_POLYGON_CREATED_STARTED) || isInState(STATE_POLYGON_CREATED_MID)) {
//			handleDrag();
//		}
		
		return true;
	}
	
	private void showSourceFeedback() {
		eraseSourceFeedback();
		
		if (getTargetEditPart() != null) {
			getTargetEditPart().showSourceFeedback(getTargetRequest());
		}
		
//		setFlag(FLAG_SOURCE_FEEDBACK, true);
		
//		System.out.println("SHOW........");

	}
	
	private void addPolylinePointToRequest() {
		CreatePolygonRequest req = (CreatePolygonRequest)getTargetRequest();
		req.addPoint(getLocation());
	}
	
	@Override
	protected void updateTargetRequest() {
		CreatePolygonRequest request = (CreatePolygonRequest) getTargetRequest();
		request.setType(getCommandName());
		request.setCurrentLocation(getLocation());
	}

	// TODO FOR DEBUG
	@Override
	protected boolean stateTransition(int start, int end) {
		return super.stateTransition(start, end);
	}
	
	// TODO FOR DEBUG
	@Override
	protected void setState(int state) {
		super.setState(state);
	}

	private boolean handleCreatePolyline() {
		//eraseSourceFeedback();
		Command endCommand = getCommand();
		setCurrentCommand(endCommand);
		executeCurrentCommand();
		return true;
	}

	private void eraseSourceFeedback() {
//		if ( getFlag(FLAG_SOURCE_FEEDBACK) ) {
			if (getTargetEditPart() != null) {
				getTargetEditPart().eraseSourceFeedback(getTargetRequest());
			}
			
//			setFlag(FLAG_SOURCE_FEEDBACK, false);
//			//System.out.println("ERASE........");
//		}
	}
	
	protected EditPart getTargetEditPart() {
		EditPart part = super.getTargetEditPart();
		if ( part != null ) {
			return (EditPart)part.getViewer().getRootEditPart().getChildren().get(0);
		}
		
		return null;
	}
}
