package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

public class FDRectangleFigure extends Label {

	public static final Image RECTANGLE_IMAGE = new Image(Display.getCurrent(), FDRectangleFigure.class.getResourceAsStream("rectangle.png"));

	public FDRectangleFigure() {
		setPreferredSize(100, 100);
		setBorder(new CompoundBorder(new LineBorder(2), new MarginBorder(2, 2, 2, 2)));
	}
	
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(ColorConstants.white);
//		Rectangle b = getBounds();
//		final int fold = NoteBorder.FOLD;
//		graphics.fillRectangle(b.x + fold, b.y, b.width - fold, fold);
//		graphics.fillRectangle(b.x, b.y + fold, b.width, b.height - fold);
		
		Rectangle r = getBounds();
		graphics.setBackgroundPattern(new Pattern(Display.getCurrent(), r.x,
				r.y, r.x + r.width, r.y + r.height, ColorConstants.white,
				ColorConstants.lightGray));
		graphics.fillRectangle(r);
		
		super.paintFigure(graphics);
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
}
