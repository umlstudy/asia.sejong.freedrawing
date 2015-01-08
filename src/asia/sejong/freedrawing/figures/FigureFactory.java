package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FDImage;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.resources.ContextManager;

public class FigureFactory {

	public static IFigure newFDEllipseFigure() {
		return new FDEllipseFigure();
	}

	public static IFigure createFigure(Class<? extends FDElement> clazz) {
		if ( clazz == FDEllipse.class ) {
			return new FDEllipseFigure();
		} else if ( clazz == FDImage.class ) {
			return new FDImageFigure();
		} else if ( clazz == FDLabel.class ) {
			return new FDLabelFigure();
		} else if ( clazz == FDRect.class ) {
			return new FDRectFigure();
		} else if ( clazz == FDWire.class ) {
			return new FDWireFigure();
		}
		
		throw new RuntimeException(clazz.getName());
	}

	public static IFigure createCustomFeedbackFigure(Object model, Rectangle initialFeedbackBounds) {
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
			r.setBounds(initialFeedbackBounds);
			
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
}
