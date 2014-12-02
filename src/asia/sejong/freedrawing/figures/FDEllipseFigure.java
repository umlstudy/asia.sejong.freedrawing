package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class FDEllipseFigure extends Ellipse {

	public FDEllipseFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
		graphics.setAntialias(SWT.ON);

		super.paintFigure(graphics);
	}

	public void setBorderColor(Color color) {
//		getLineBorder().setColor(color);
	}

	// TODO
	public LineBorder getLineBorder() {
//		return lineBorder;
		return null;
	}
}
