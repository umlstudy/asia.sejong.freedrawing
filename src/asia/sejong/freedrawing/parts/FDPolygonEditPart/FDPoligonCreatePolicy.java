package asia.sejong.freedrawing.parts.FDPolygonEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

import asia.sejong.freedrawing.parts.FDPolygonEditPart.command.PolygonCreateCompleteCommand;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.command.PolygonCreateMidCommand;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.command.PolygonCreateStartCommand;

/**
 * Handles creation and moving Node
 * 
 * @author SeJong
 * 
 */
public class FDPoligonCreatePolicy extends GraphicalEditPolicy {

	public static final String REQ_POLYGON_CREATE_START = "REQ_POLYGON_CREATE_START";
	public static final String REQ_POLYGON_CREATE_MID = "REQ_POLYGON_CREATE_MID";
	public static final String REQ_POLYGON_CREATE_END = "REQ_POLYGON_CREATE_END";
	
	private IFigure feedbackFigure;

	public Command getCommand(Request request) {

		if (REQ_POLYGON_CREATE_START.equals(request.getType())) {
			return getPolygonCreateStartCommand((CreatePolygonRequest) request);
		} else if (REQ_POLYGON_CREATE_MID.equals(request.getType())) {
			return getPolygonCreateMidCommand((CreatePolygonRequest) request);
		} else if (REQ_POLYGON_CREATE_END.equals(request.getType())) {
			return getPolygonCreateCompleteCommand((CreatePolygonRequest) request);
		}

		return null;
	}

	private Command getPolygonCreateCompleteCommand(CreatePolygonRequest request) {
		return new PolygonCreateCompleteCommand(request);
	}

	private Command getPolygonCreateMidCommand(CreatePolygonRequest request) {
		return new PolygonCreateMidCommand(request);
	}

	private Command getPolygonCreateStartCommand(CreatePolygonRequest request) {
		return new PolygonCreateStartCommand(request);
	}
	
	public void showSourceFeedback(Request request) {
		if (REQ_POLYGON_CREATE_MID.equals(request.getType()) 
				|| REQ_POLYGON_CREATE_END.equals(request.getType()) ) {
			if ( feedbackFigure == null ) {
				RectangleFigure rectangleFigure = new RectangleFigure();
				feedbackFigure = rectangleFigure;
			}
			RectangleFigure rectangleFigure =  (RectangleFigure)feedbackFigure;
			rectangleFigure.setLocation(new Point(100,100));
			rectangleFigure.setSize(200,200);
			addFeedback(rectangleFigure);
		}
	}
	
	public void eraseSourceFeedback(Request request) {
		if (REQ_POLYGON_CREATE_MID.equals(request.getType()) 
				|| REQ_POLYGON_CREATE_END.equals(request.getType()) ) {
			eraseFeedbackFigure();
		}
	}
	
	private void eraseFeedbackFigure() {
		if (feedbackFigure != null) {
			removeFeedback(feedbackFigure);
			feedbackFigure = null;
		}
	}
	
	public void deactivate() {
		eraseFeedbackFigure();
		super.deactivate();
	}
}
