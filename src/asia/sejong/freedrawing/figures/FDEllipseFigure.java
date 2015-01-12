package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDEllipse;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDEllipseFigure extends RectangleFigure implements FDTextShapeFigure {

	private double degree;

	FDEllipseFigure() {
		setPreferredSize(100, 100);
		setClippingStrategy(new IClippingStrategy() {
			
			@Override
			public Rectangle[] getClip(IFigure childFigure) {
				FDEllipseFigure ep = (FDEllipseFigure)childFigure;
				Rectangle calculateTranslateEffectArea = Rotationer.calculateTranslateEffectArea(ep, ep.degree);
				return new Rectangle[] {calculateTranslateEffectArea,};
			}
		});
	}
	
	boolean rotate = false;
	
	public Rectangle getTargetBounds() {
		if ( rotate ) {
			return new Rectangle(-bounds.width/2, -bounds.height/2, bounds.width, bounds.height);
		} else {
			return bounds;
		}
	}
	
	@Override
	public void paintFigure(final Graphics graphics) {
		graphics.setAntialias(SWT.ON);
		graphics.setXORMode(true);
		graphics.setBackgroundColor(getBackgroundColor());

		// 드로잉역영을 일부러 크게 만듦
		graphics.setClip(new Rectangle(getBounds().x-50, getBounds().y-50, getBounds().width+100, getBounds().height+100));
		
		System.out.println("ellipsefigure");
		if ( degree > 0 ) {
			Point targetCenterPosition = new Point(bounds.width>>1, bounds.height>>1);
			Point targetCenterPositionInGraphics = new Point(bounds.x + targetCenterPosition.x, bounds.y + targetCenterPosition.y);
			graphics.translate(targetCenterPositionInGraphics);
			rotate = true;
			//graphics.setClip(Rotationer.calculateTranslateEffectArea(this, degree));
			graphics.rotate((float)degree);
		}
		
		super.paintFigure(graphics);
		rotate = false;
//		
//		
//		System.out.println("paintFigure");
//		if ( degree > 0 ) {
//			new Rotationer() {
//				@Override
//				protected void paintInRotateState() {
//					FDEllipseFigure.super.paintFigure(graphics);
//				}
//			}.execute(graphics, this, degree);
//		} else {
//			super.paintFigure(graphics);
//		}
	}
	
	public void erase() {
		if (getParent() == null || !isVisible())
			return;

		Rectangle r = new Rectangle(getBounds().x-50, getBounds().y-50, getBounds().width+100, getBounds().height+100);
		getParent().translateToParent(r);
		getParent().repaint(r.x, r.y, r.width, r.height);
	}

	public LineBorder getLineBorder() {
//		return lineBorder;
		return null;
	}

	@Override
	public void setTextEx(String text) {
		
	}
	
	@Override
	public void setLineColorEx(RGB rgbColor) {
		@SuppressWarnings("unused")
		Color color = null; // TODO
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			//super.setBackgroundColor(color);
		}
	}

	@Override
	public void setFontInfoEx(FontInfo fontInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBackgroundColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setBackgroundColor(color);
		}	
	}
	
	@Override
	public void setFontColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setForegroundColor(color);
		}	
	}

	@Override
	public void setAlphaEx(int alpha) {
		
	}

	@Override
	public void setLineWidthEx(int lineWidth) {
		
	}

	@Override
	public void setLineStyleEx(int lineStyle) {
		
	}

	@Override
	public void setModelAttributes(FDElement model_) {
		FDEllipse model = (FDEllipse)model_;
		setTextEx(model.getText());
		setFontInfoEx(model.getFontInfo());
		setFontColorEx(model.getFontColor());
		//setAlpah(model.getAlpha());
		setBackgroundColorEx(model.getBackgroundColor());
		setLineWidthEx(model.getLineWidth());
		setLineStyleEx(model.getLineStyle());
		setLineColorEx(model.getLineColor());
	}

	@Override
	public void setLocationEx(Point point) {
		setLocation(point);
	}

	@Override
	public void setSizeEx(int width, int height) {
		setSize(width, height);
	}

	@Override
	public void setDegreeEx(double degree) {
		this.degree = degree;
	}
	
	@Override
	public double getDegreeEx() {
		return this.degree;
	}
	
	protected void fillShape(Graphics graphics) {
		graphics.fillRectangle(getTargetBounds());
	}

	protected void outlineShape(Graphics graphics) {
		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);

		Rectangle r = Rectangle.SINGLETON.setBounds(getTargetBounds());
		r.x += inset1;
		r.y += inset1;
		r.width -= inset1 + inset2;
		r.height -= inset1 + inset2;

		graphics.drawRectangle(r);
	}
}
