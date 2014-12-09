package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDRectFigure extends Label implements FDTextShapeFigure {

	private LineBorder lineBorder;
	private Integer alpha = 0xff;
	
	public FDRectFigure() {
		setPreferredSize(100, 100);
		lineBorder = new LineBorder(1);
		setBorder(new CompoundBorder(lineBorder, new MarginBorder(2, 2, 2, 2)));
	}
	
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(ColorConstants.white);
//		Rectangle b = getBounds();
//		final int fold = NoteBorder.FOLD;
//		graphics.fillRectangle(b.x + fold, b.y, b.width - fold, fold);
//		graphics.fillRectangle(b.x, b.y + fold, b.width, b.height - fold);
		
		Rectangle r = getBounds();
//		graphics.setBackgroundPattern(new Pattern(Display.getCurrent(), r.x,
//				r.y, r.x + r.width, r.y + r.height, ColorConstants.white,
//				ColorConstants.lightGray));
		
//		Transform tr = new Transform();
//		tr.setRotation(39f);
		graphics.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN));
		graphics.fillRectangle(r);
		graphics.setAntialias(SWT.ON);
//		graphics.rotate(3);

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
	public void setFont(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		setFont(font);
	}
	
	@Override
	public Integer getAlpha() {
		return alpha;
	}

	@Override
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}
