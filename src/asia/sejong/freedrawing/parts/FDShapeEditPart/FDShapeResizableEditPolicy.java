package asia.sejong.freedrawing.parts.FDShapeEditPart;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;

import asia.sejong.freedrawing.figures.FDElementFigure;
import asia.sejong.freedrawing.figures.FDEllipseFigure;
import asia.sejong.freedrawing.figures.FDImageFigure;
import asia.sejong.freedrawing.figures.FDLabelFigure;
import asia.sejong.freedrawing.figures.FDRectFigure;
import asia.sejong.freedrawing.figures.FDShapeFigure;
import asia.sejong.freedrawing.figures.FDTextShapeFigure;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FDImage;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.RotateCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.request.RotateRequest;
import asia.sejong.freedrawing.parts.common.FDShapeResizeTracker;
import asia.sejong.freedrawing.parts.common.FDShapeRotateTracker;
import asia.sejong.freedrawing.parts.handles.RelativeHandleLocatorEx;
import asia.sejong.freedrawing.parts.handles.ResizableHandleKitEx;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDShapeResizableEditPolicy extends ResizableEditPolicy {

	@Override
	protected IFigure createDragSourceFeedbackFigure() {
		IFigure feedbackFigure = createFeedbackFigure((GraphicalEditPart) getHost(), null);

		feedbackFigure.setBounds(getInitialFeedbackBounds());
		feedbackFigure.validate();
		addFeedback(feedbackFigure);
		
		return feedbackFigure;
	}

	protected IFigure createFeedbackFigure(GraphicalEditPart targetEditPart, IFigure parent) {
		IFigure feedbackFigure = getCustomFeedbackFigure(targetEditPart.getModel());

		if (parent != null) {
			parent.add(feedbackFigure);
		}

		Rectangle targetBounds = targetEditPart.getFigure().getBounds().getCopy();

		IFigure walker = targetEditPart.getFigure().getParent();

		while (walker != ((GraphicalEditPart) targetEditPart.getParent()).getFigure()) {
			walker.translateToParent(targetBounds);
			walker = walker.getParent();
		}

		feedbackFigure.setBounds(targetBounds);

		Iterator<?> childsOfTargetEditPartIter = targetEditPart.getChildren().iterator();
		while (childsOfTargetEditPartIter.hasNext()) {
			createFeedbackFigure((GraphicalEditPart) childsOfTargetEditPartIter.next(), feedbackFigure);
		}
		
		return feedbackFigure;
	}
	
	protected IFigure getCustomFeedbackFigure(Object model) {
		IFigure figure;

		if (model instanceof FDRect) {
			FDRectFigure realFigure = new FDRectFigure();
			realFigure.setModelAttributes((FDElement)model);
			//realFigure.setAlpha(128);
			
			figure = realFigure;
		} else if (model instanceof FDEllipse ) {
			FDEllipseFigure realFigure = new FDEllipseFigure();
			realFigure.setModelAttributes((FDElement)model);
			realFigure.setAlpha(128);
			
//			FDEllipse ellipse = (FDEllipse)model;
//			realFigure.setText(ellipse.getText());
			figure = realFigure;
		} else if (model instanceof FDLabel ) {
			FDLabelFigure realFigure = new FDLabelFigure();
			realFigure.setModelAttributes((FDElement)model);
			
//			FDEllipse ellipse = (FDEllipse)model;
//			realFigure.setText(ellipse.getText());
			figure = realFigure;
		} else if (model instanceof FDImage ) {
			FDImageFigure realFigure = new FDImageFigure();
			FDImage imageModel = (FDImage)model;
			Image image = ContextManager.getInstance().getImageManager().get(imageModel.getImageBytes());
			realFigure.setImage(image);

			realFigure.setModelAttributes((FDElement)model);
			realFigure.setAlphaEx(128);
			
//			FDEllipse ellipse = (FDEllipse)model;
//			realFigure.setText(ellipse.getText());
			figure = realFigure;
		} else {
//			figure = new RectangleFigure();
//			((RectangleFigure) figure).setXOR(true);
//			((RectangleFigure) figure).setFill(true);
//			//figure.setBackgroundColor(LogicColorConstants.ghostFillColor);
//			figure.setBackgroundColor(ColorConstants.white);
//			figure.setForegroundColor(ColorConstants.white);
			
			RectangleFigure r = new RectangleFigure();
			FigureUtilities.makeGhostShape(r);
			r.setLineStyle(Graphics.LINE_DOT);
			r.setForegroundColor(ColorConstants.white);
			r.setBounds(getInitialFeedbackBounds());
			
			figure = r;
		}
		
		if (model instanceof FDTextShape ) {
			FDTextShapeFigure f = (FDTextShapeFigure)figure;
			f.setTextEx(((FDTextShape)model).getText());
		}
		
		if (model instanceof FDShape ) {
			//FDShapeFigure f = (FDShapeFigure)figure;
		}
		
		if (model instanceof FDElement ) {
			FDElementFigure f = (FDElementFigure)figure;
			f.setLineColorEx(((FDElement)model).getLineColor());
		}
		
		if (model instanceof FDElement ) {
			FDElementFigure f = (FDElementFigure)figure;
			f.setLineColorEx(((FDElement)model).getLineColor());
		}
		
		if (figure instanceof Shape ) {
			Shape shape = (Shape)figure;
			shape.setXOR(false);
			shape.setFill(true);
			
			FigureUtilities.makeGhostShape(shape);
			shape.setLineStyle(Graphics.LINE_DOT);
			shape.setForegroundColor(ColorConstants.white);
		}

		
//		RectangleFigure r = new RectangleFigure();
//		FigureUtilities.makeGhostShape(r);
//		r.setLineStyle(Graphics.LINE_DOT);
//		r.setForegroundColor(ColorConstants.white);
//		r.setBounds(getInitialFeedbackBounds());

		return figure;
	}

	@Override
	protected IFigure getFeedbackLayer() {
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}

	@Override
	protected Rectangle getInitialFeedbackBounds() {
		return getHostFigure().getBounds();
	}

	@Override
	protected ResizeTracker getResizeTracker(int direction) {
		// TODO member object 로? 
		return new FDShapeResizeTracker((GraphicalEditPart) getHost(), direction);
	}
	
//	protected IFigure createDragSourceFeedbackFigure() {
//		// Use a ghost rectangle for feedback
////		RectangleFigure r = new RectangleFigure();
////		FigureUtilities.makeGhostShape(r);
////		r.setLineStyle(Graphics.LINE_DASHDOT);
////		r.setLineWidth(2);
////		r.setForegroundColor(ColorConstants.white);
////		r.setBounds(getInitialFeedbackBounds());
////		r.validate();
////		addFeedback(r);
//		FDShapeEditPart shapeEditPart = (FDShapeEditPart)getHost();
//		shapeEditPart.get
//		if ( getHost() )
//		getHost()
//		Rectangle bounds = getInitialFeedbackBounds();
//		Image image = new Image(Display.getCurrent(), bounds.width, bounds.height);
//		GC gc = new GC(image);
//		SWTGraphics graphics = new SWTGraphics(gc);
//		graphics.setAlpha(150);
//		graphics.translate(-bounds.x, -bounds.y);
//		IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
//		figure.paint(graphics);
//		
////		Color background = gc.getBackground ();
////		Pattern p = new Pattern (Display.getCurrent(), 0, 0, 0, bounds.height, background, 100, background, 180);
////		gc.setBackgroundPattern (p);
////		gc.fillRectangle (0, 0, bounds.width, bounds.height);
////		p.dispose ();
//		
//	    gc.dispose();
//	    graphics.dispose();
//	    
//	    ImageFigure imageFigure = new ImageFigure(image);
//	    imageFigure.setOpaque(true);
//	    imageFigure.validate();
//	    addFeedback(imageFigure);
//	    System.out.println("MOVING FIGURE!");
////	    image.dispose();
//		return imageFigure;
//	}
	
	@Override
	protected void addSelectionListener() {
//		selectionListener = new EditPartListener.Stub() {
//			public void selectedStateChanged(EditPart part) {
//				setSelectedState(part.getSelected());
//				setFocus(part.hasFocus());
//			}
//		};
//		getHost().addEditPartListener(selectionListener);
		super.addSelectionListener();
		System.out.println("addSelectionListener");
	}
	
	@Override
	protected void createResizeHandle(@SuppressWarnings("rawtypes") List handles, int direction) {
		if ( ((getResizeDirections() & direction) == direction ) || RelativeHandleLocatorEx.ROTATION == direction ) {
			Cursor cursor = null;
			DragTracker dragTracker = null;
			if ( RelativeHandleLocatorEx.ROTATION == direction ) {
				cursor = Cursors.HAND;
				dragTracker = new FDShapeRotateTracker((GraphicalEditPart) getHost());
			} else if ( (getResizeDirections() & direction) == direction ) {
				dragTracker = getResizeTracker(direction);
				cursor = Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored());
			}
			ResizableHandleKitEx.addHandle((GraphicalEditPart) getHost(), handles, direction, dragTracker, cursor);
		} else {
			// display 'resize' handle to allow dragging or indicate selection
			// only
			createDragHandle(handles, direction);
		}
	}
	
	@Override
	protected void createMoveHandle(@SuppressWarnings("rawtypes") List handles) {
		if (isDragAllowed()) {
			// display 'move' handle to allow dragging
			ResizableHandleKitEx.addMoveHandle((GraphicalEditPart) getHost(), handles, getDragTracker(), Cursors.SIZEALL);
		} else {
			// display 'move' handle only to indicate selection
			ResizableHandleKitEx.addMoveHandle((GraphicalEditPart) getHost(), handles, getSelectTracker(), SharedCursors.ARROW);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected List createSelectionHandles() {
		List selectionHandles = super.createSelectionHandles();
		createResizeHandle(selectionHandles, RelativeHandleLocatorEx.ROTATION);
		return selectionHandles;
	}
	
	@Override
	public Command getCommand(Request request) {
		if (FDShapeRotateTracker.REQ_ROTATE.equals(request.getType()) && request instanceof RotateRequest) {
			RotateRequest newReq = new RotateRequest(FDShapeRotateTracker.REQ_ROTATE_CHILD);
			newReq.setEditParts((FDShapeEditPart)getHost());
			
			RotateRequest oldReq = (RotateRequest)request;
			// TODO
//			newReq.setLocation();
			newReq.setLocation(oldReq.getLocation());
			newReq.setExtendedData(oldReq.getExtendedData());
			return getHost().getParent().getCommand(newReq);
		}
		return super.getCommand(request);
	}
	
	@Override
	public boolean understandsRequest(Request request) {
		if (FDShapeRotateTracker.REQ_ROTATE.equals(request.getType())) {
			return true;
		}
		return super.understandsRequest(request);
	}
	
	@Override
	public void showSourceFeedback(Request request) {
		if ( FDShapeRotateTracker.REQ_ROTATE.equals(request.getType()) ) {
			showRotateFeedback((RotateRequest) request);
		} else {
			super.showSourceFeedback(request);
		}
	}
	
	protected void showRotateFeedback(RotateRequest request) {
		IFigure feedbackFigure = createFeedbackFigure((GraphicalEditPart) getHost(), null);

		feedbackFigure.setBounds(getInitialFeedbackBounds());
		((FDShapeFigure)feedbackFigure).setDegreeEx(request.getDegree());
		feedbackFigure.validate();
		addFeedback(feedbackFigure);
	}

}
