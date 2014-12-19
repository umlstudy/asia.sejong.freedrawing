package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDRectFigure extends Label implements FDTextShapeFigure {

	private LineBorder lineBorder;
	private Integer alpha = 0xff;
	private int angle;
	
	public FDRectFigure() {
		setPreferredSize(100, 100);
		lineBorder = new LineBorder(1);
		setBorder(new CompoundBorder(lineBorder, new MarginBorder(2, 2, 2, 2)));
	}
	
	protected void paintFigure(Graphics graphics) {
//		Rectangle b = getBounds();
//		final int fold = NoteBorder.FOLD;
//		graphics.fillRectangle(b.x + fold, b.y, b.width - fold, fold);
//		graphics.fillRectangle(b.x, b.y + fold, b.width, b.height - fold);
		
//		Transform tr = new Transform (Display.getCurrent());
//		//tr.setElements (1, 0, 0, -1, 1, 2*(y+rect.height));
//		//tr.setElements (1, 0, 0, 0, 1, 0);
//		tr.rotate(10f);
//		tr.translate(100, 0);
//		gc.setTransform (tr);
//		
		Rectangle r = getBounds();
//		graphics.setBackgroundPattern(new Pattern(Display.getCurrent(), r.x,
//				r.y, r.x + r.width, r.y + r.height, ColorConstants.white,
//				ColorConstants.lightGray));
		
//		Transform tr = new Transform();
//		tr.setRotation(39f);
		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillRectangle(r);
		graphics.setAntialias(SWT.ON);
//		graphics.rotate(30f);

		super.paintFigure(graphics);
	}
	
	protected void paintClientArea(Graphics graphics) {
//		graphics.rotate(90f);
		super.paintClientArea(graphics);
	}
	
//	public Rectangle getBounds() {
////		System.out.println("BB ? " + super.getBounds());
//		return super.getBounds();
//	}

	public void setBorderColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			getLineBorder().setColor(color);
		}
	}
	
//
//	/**
//	 * Adjust the receiver's appearance based upon whether the receiver is selected
//	 * 
//	 * @param selected <code>true</code> if the receiver is selected, else
//	 *            <code>false</code>
//	 */
//	public void setSelected(boolean selected) {
//		((NoteBorder) getBorder()).setLineColor(selected ? ColorConstants.blue : ColorConstants.black);
//		((NoteBorder) getBorder()).setLineWidth(selected ? 2 : 1);
//		erase();
//	}

	public LineBorder getLineBorder() {
		return lineBorder;
	}

	@Override
	public void setFontInfoEx(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		setFont(font);
	}
	
	@Override
	public void setAlphaEx(int alpha) {
		this.alpha = alpha;
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
	public void setLineWidthEx(int lineWidth) {
		lineBorder.setWidth(lineWidth);
	}

	@Override
	public void setLineStyleEx(int lineStyle) {
		// TODO
		if ( lineStyle != 0 ) {
			lineBorder.setStyle(lineStyle);
		} else {
			lineBorder.setStyle(SWT.LINE_DASHDOTDOT);
		}
	}

	@Override
	public void setLineColorEx(RGB rgbColor) {
		
	}

	@Override
	public void setModelAttributes(FDElement model_) {
		FDRect model = (FDRect)model_;
		
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
	public void setTextEx(String text) {
		super.setText(text);
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
	public void setAngleEx(int angle) {
		this.angle = angle;
	}

//	@Override
//	public void setSelected(boolean selected) {
//		setForegroundColor(selected ? ColorConstants.blue : ColorConstants.black);
//		setLineWidthEx(selected ? 2 : 1);
//		erase();
//	}
}
