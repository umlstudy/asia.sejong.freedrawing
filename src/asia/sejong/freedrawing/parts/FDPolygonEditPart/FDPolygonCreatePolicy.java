package asia.sejong.freedrawing.parts.FDPolygonEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

import asia.sejong.freedrawing.figures.FDFigureFactory;
import asia.sejong.freedrawing.figures.FDPolygonFigure;
import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDModelFactory;
import asia.sejong.freedrawing.model.FDPolygon;
import asia.sejong.freedrawing.parts.FDContainerEditPart.command.FDShapeCreateCommand;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.command.PolygonCreateMidCommand;
import asia.sejong.freedrawing.parts.FDPolygonEditPart.command.PolygonCreateStartCommand;

/**
 * Handles creation and moving Node
 * 
 * @author SeJong
 * 
 */
public class FDPolygonCreatePolicy extends GraphicalEditPolicy {

	public static final String REQ_POLYGON_CREATE_START = "REQ_POLYGON_CREATE_START";
	public static final String REQ_POLYGON_CREATE_MID = "REQ_POLYGON_CREATE_MID";
	public static final String REQ_POLYGON_CREATE_END = "REQ_POLYGON_CREATE_END";
	
	private IFigure feedbackFigure;

	@Override
	public Command getCommand(Request request) {
		if ( request instanceof CreatePolygonRequest ) {
			if (REQ_POLYGON_CREATE_START.equals(request.getType())) {
				return getPolygonCreateStartCommand((CreatePolygonRequest) request);
			} else if (REQ_POLYGON_CREATE_MID.equals(request.getType())) {
				return getPolygonCreateMidCommand((CreatePolygonRequest) request);
			} else if (REQ_POLYGON_CREATE_END.equals(request.getType())) {
				return getPolygonCreateCompleteCommand((CreatePolygonRequest) request);
			}
		}

		return null;
	}
	
	@Override
	public EditPart getTargetEditPart(Request request) {
		if ( request instanceof CreatePolygonRequest ) {
			if ( request.getType().equals(REQ_POLYGON_CREATE_MID) || 
					request.getType().equals(REQ_POLYGON_CREATE_START) ) {
				return getHost();
			}
		}
		
		return null;
	}

	private Command getPolygonCreateCompleteCommand(CreatePolygonRequest request) {
		//return new PolygonCreateCompleteCommand(request);
		FDPolygon polygon = request.createModel();
		FDContainer container = (FDContainer)((EditPart)getHost().getRoot().getChildren().get(0)).getModel();
		return new FDShapeCreateCommand(container, polygon);
	}

	private Command getPolygonCreateMidCommand(CreatePolygonRequest request) {
		return new PolygonCreateMidCommand(request);
	}

	private Command getPolygonCreateStartCommand(CreatePolygonRequest request) {
		return new PolygonCreateStartCommand(request);
	}
	
	@Override
	public void showSourceFeedback(Request request) {
		if (REQ_POLYGON_CREATE_MID.equals(request.getType()) 
				|| REQ_POLYGON_CREATE_END.equals(request.getType()) ) {

			CreatePolygonRequest cpr = (CreatePolygonRequest)request;
			FDPolygon model = (FDPolygon)FDModelFactory.createModel(FDPolygon.class);
			model.setPoints(cpr.getMovingPoints());
			if ( feedbackFigure == null ) {
				FDPolygonFigure polygonFigure = (FDPolygonFigure)FDFigureFactory.createFigure(model);
				polygonFigure.setFeedbackEx(true);
				feedbackFigure = polygonFigure;
			} else {
				FDPolygonFigure polygonFigure =  (FDPolygonFigure)feedbackFigure;
				polygonFigure.setModelAttributes(model);
			}
			
			addFeedback(feedbackFigure);
//			System.out.println("AAAA " + "child ? " + getFeedbackLayer().getChildren().size() + "_" + feedbackFigure ) ;
		}
	}
	
	@Override
	public void eraseSourceFeedback(Request request) {
		if (REQ_POLYGON_CREATE_MID.equals(request.getType()) 
				|| REQ_POLYGON_CREATE_END.equals(request.getType()) ) {
			eraseFeedbackFigure();
		}
	}
	
	private void eraseFeedbackFigure() {
		if (feedbackFigure != null) {
//			System.out.println("RRRR " + "child ? " + getFeedbackLayer().getChildren().size() + "_" + feedbackFigure ) ;
			removeFeedback(feedbackFigure);
			feedbackFigure = null;
		} else {
			// TODO
//			System.out.println("E");
		}
	}
	
	@Override
	public void deactivate() {
		eraseFeedbackFigure();
		super.deactivate();
	}
}
