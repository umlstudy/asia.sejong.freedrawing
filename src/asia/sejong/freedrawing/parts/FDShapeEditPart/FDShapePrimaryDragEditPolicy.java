package asia.sejong.freedrawing.parts.FDShapeEditPart;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.graphics.Cursor;

import asia.sejong.freedrawing.figures.FDShapeFigure;
import asia.sejong.freedrawing.figures.FigureFactory;
import asia.sejong.freedrawing.parts.FDShapeEditPart.request.RotateRequest;
import asia.sejong.freedrawing.parts.common.FDShapeResizeTracker;
import asia.sejong.freedrawing.parts.common.FDShapeRotateTracker;
import asia.sejong.freedrawing.parts.handles.RelativeHandleLocatorEx;
import asia.sejong.freedrawing.parts.handles.ResizableHandleKitEx;

public class FDShapePrimaryDragEditPolicy extends FDShapeRotateEditPolicy {

	@Override
	protected IFigure createDragSourceFeedbackFigure() {
		IFigure feedbackFigure = createFeedbackFigure((GraphicalEditPart) getHost(), null);

		feedbackFigure.setBounds(getInitialFeedbackBounds());
		feedbackFigure.validate();
		addFeedback(feedbackFigure);
		
		return feedbackFigure;
	}

	protected IFigure createFeedbackFigure(GraphicalEditPart targetEditPart, IFigure parent) {
		IFigure feedbackFigure = FigureFactory.createCustomFeedbackFigure(targetEditPart.getModel(), getInitialFeedbackBounds());

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
	
	@Override
	protected IFigure getFeedbackLayer() {
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}

	@Override
	protected Rectangle getInitialFeedbackBounds() {
		return getHostFigure().getBounds();
	}

//	protected ResizeTracker getResizeTracker(int direction) {
//		// TODO member object 로? 
//		return new FDShapeResizeTracker((GraphicalEditPart) getHost(), direction);
//	}
	
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
		if ( RelativeHandleLocatorEx.ROTATION == direction ) {
			Cursor cursor = Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored());
			DragTracker dragTracker = new FDShapeRotateTracker((GraphicalEditPart) getHost(), direction);
			ResizableHandleKitEx.addHandle((GraphicalEditPart) getHost(), handles, direction, dragTracker, cursor);
		} else if ( (getResizeDirections() & direction) == direction ) {
//			Cursor cursor = null;
//			DragTracker dragTracker = null;
//			if ( RelativeHandleLocatorEx.ROTATION == direction ) {
//				cursor = Cursors.HAND;
//				dragTracker = new FDShapeRotateTracker((GraphicalEditPart) getHost());
//			} else if ( (getResizeDirections() & direction) == direction ) {
//				dragTracker = getResizeTracker(direction);
//				cursor = Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored());
//			}
				
			Cursor cursor = Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored());
			DragTracker dragTracker = new FDShapeResizeTracker((GraphicalEditPart) getHost(), direction);
			ResizableHandleKitEx.addHandle((GraphicalEditPart) getHost(), handles, direction, dragTracker, cursor);
		} else {
			// display 'resize' handle to allow dragging or indicate selection
			// only
			createDragHandle(handles, direction);
		}
		
//		createDragHandle(handles, direction);
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
	public void showSourceFeedback(Request request) {
		if ( FDShapeRotateTracker.REQ_ROTATE.equals(request.getType()) ) {
			showRotateFeedback((RotateRequest) request);
		} else {
			super.showSourceFeedback(request);
		}
	}
	
	@Override
	public void eraseSourceFeedback(Request request) {
		if ( FDShapeRotateTracker.REQ_ROTATE.equals(request.getType()) ) {
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
		} else {
			super.eraseSourceFeedback(request);
		}
	}
	
	protected void showRotateFeedback(RotateRequest request) {

		IFigure feedback = getDragSourceFeedbackFigure();

		PrecisionRectangle rect = new PrecisionRectangle(getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());

		feedback.translateToRelative(rect);
		
		// 각을 입력한다.
		((FDShapeFigure)feedback).setDegreeEx(request.getDegree());
		
		feedback.setBounds(rect);
		feedback.validate();
	}
}
