package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
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

	transient boolean rotate = false;

	FDEllipseFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(final Graphics graphics) {
		graphics.setAntialias(SWT.ON);
		graphics.setXORMode(true);
		graphics.setBackgroundColor(getBackgroundColor());

		// 드로잉역영을 일부러 크게 만듦 - 최적화 필요
		// 사각형 회전시 최대길이를 한변으로 하는 정사각형 영역을 
		// 드로잉 영역으로 설정함
		Point centerPoint = GeometryUtil.centerPoint(getBounds());
		double h = GeometryUtil.calculateHipotenuse(getBounds().width, getBounds().height);
		graphics.setClip(GeometryUtil.createSquare(centerPoint, (int)h));
		
		// TODO TEST
		System.out.println("ellipsefigure");
		
		boolean translated = false;
		if ( degree > 0 ) {
			translated = true;
			graphics.translate(centerPoint.x, centerPoint.y);
			rotate = true;
			graphics.rotate((float)degree);
			System.out.println( " degree " + degree);
		}
		
		super.paintFigure(graphics);
		
		if ( translated ) {
			graphics.translate(-centerPoint.x, -centerPoint.y);
		}
		
		rotate = false;
	}
	
	private Rectangle getTargetBounds() {
		if ( rotate ) {
			return GeometryUtil.createRectangleCenterIsZero(bounds);
		} else {
			return bounds;
		}
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillRectangle(getTargetBounds());
	}

	@Override
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
	
	@Override
	public void erase() {
		if (getParent() == null || !isVisible()) {
			return;
		}

		// 드로잉역영을 일부러 크게 만듦 - 최적화 필요
		// 사각형 회전시 최대길이를 한변으로 하는 정사각형 영역을 
		// 드로잉 영역으로 설정함
		Point centerPoint = GeometryUtil.centerPoint(getBounds());
		double h = GeometryUtil.calculateHipotenuse(getBounds().width, getBounds().height);
		Rectangle r = GeometryUtil.createSquare(centerPoint, (int)h);
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
	public void setLineWidthEx(float lineWidth) {
		
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
}
