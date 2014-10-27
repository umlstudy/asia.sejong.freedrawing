package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class NodeBorder extends LineBorder {

	private Color lineColor;
	private int lineWidth;

	public void setLineWidth(int width) {
		lineWidth = width;
	}
	
	public void setLineColor(Color color) {
		lineColor = color;
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
//		Rectangle r = figure.getBounds().getCopy();
//		graphics.setForegroundColor(lineColor);
//		graphics.setLineWidth(lineWidth);
//		graphics.drawRectangle(r);
//		
//		// solid long edges around border
//		graphics.drawLine(r.x + FOLD, r.y, r.x + r.width - lineWidth, r.y);
//		graphics.drawLine(r.x, r.y + FOLD, r.x, r.y + r.height - lineWidth);
//		graphics.drawLine(r.x + r.width - lineWidth, r.y, r.x + r.width - lineWidth, r.y + r.height - lineWidth);
//		graphics.drawLine(r.x, r.y + r.height - lineWidth, r.x + r.width - lineWidth, r.y + r.height - lineWidth);
//		// solid short edges
//		graphics.drawLine(r.x + FOLD, r.y, r.x + FOLD, r.y + FOLD);
//		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y + FOLD);
//		// gray small triangle
//		graphics.setBackgroundColor(ColorConstants.lightGray);
//		graphics.fillPolygon(new int[] { r.x, r.y + FOLD, r.x + FOLD, r.y,
//				r.x + FOLD, r.y + FOLD });
//		// dotted short diagonal line
//		graphics.setLineStyle(SWT.LINE_DOT);
//		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y);
	}
}
