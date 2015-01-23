package asia.sejong.freedrawing.parts.FDPolygonEditPart;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.TargetingTool;

public class FDPolygonCreateionTool extends TargetingTool {

	private FDPolygonEditPart polygonEditPart;
	
	private static final int STATE_POLYGON_CREATED_MID = 0;
	private static final int STATE_POLYGON_CREATED_STARTED = 0x01;

	@Override
	protected String getCommandName() {
		if ( isInState(STATE_POLYGON_CREATED_STARTED | STATE_ACCESSIBLE_DRAG_IN_PROGRESS) ) {
			return FDPoligonCreatePolicy.REQ_POLYGON_CREATE_MID;
		} else if ( isInState(STATE_POLYGON_CREATED_MID | STATE_ACCESSIBLE_DRAG_IN_PROGRESS) ) {
			return FDPoligonCreatePolicy.REQ_POLYGON_CREATE_END;
		} else {
			return FDPoligonCreatePolicy.REQ_POLYGON_CREATE_START;
		}
	}
	
	protected boolean handleButtonDown(int button) {
		if (button == 1 && stateTransition(STATE_POLYGON_CREATED_STARTED, STATE_TERMINAL)) {
			return handleCreateConnection();
		}
		
		super.handleButtonDown(button);
		if (isInState(STATE_POLYGON_CREATED_STARTED) || isInState(STATE_POLYGON_CREATED_MID)) {
			handleDrag();
		}
		
		return true;
	}

	protected boolean handleCreateConnection() {
		eraseSourceFeedback();
		Command endCommand = getCommand();
		setCurrentCommand(endCommand);
		executeCurrentCommand();
		return true;
	}

	protected void eraseSourceFeedback() {
		polygonEditPart.eraseSourceFeedback(createTargetRequest());
	}
}
