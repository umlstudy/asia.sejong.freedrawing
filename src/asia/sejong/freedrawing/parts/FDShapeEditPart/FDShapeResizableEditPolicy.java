package asia.sejong.freedrawing.parts.FDShapeEditPart;

import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.tools.ResizeTracker;

import asia.sejong.freedrawing.figures.FDElementFigure;
import asia.sejong.freedrawing.figures.FDEllipseFigure;
import asia.sejong.freedrawing.figures.FDLabelFigure;
import asia.sejong.freedrawing.figures.FDRectFigure;
import asia.sejong.freedrawing.figures.FDTextShapeFigure;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.parts.common.FDShapeResizeTracker;

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
			figure = new FDRectFigure();
		} else if (model instanceof FDEllipse ) {
			FDEllipseFigure realFigure = new FDEllipseFigure();
			
//			FDEllipse ellipse = (FDEllipse)model;
//			realFigure.setText(ellipse.getText());
			figure = realFigure;
		} else if (model instanceof FDLabel ) {
			FDLabelFigure realFigure = new FDLabelFigure();
			
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
			f.setText(((FDTextShape)model).getText());
		}
		
		if (model instanceof FDShape ) {
			//FDShapeFigure f = (FDShapeFigure)figure;
		}
		
		if (model instanceof FDElement ) {
			FDElementFigure f = (FDElementFigure)figure;
			f.setBorderColor(((FDElement)model).getBorderColor());
		}
		
		if (model instanceof FDElement ) {
			FDElementFigure f = (FDElementFigure)figure;
			f.setBorderColor(((FDElement)model).getBorderColor());
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
}
