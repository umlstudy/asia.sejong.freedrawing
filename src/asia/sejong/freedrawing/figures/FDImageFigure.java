package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.resources.ContextManager;

public class FDImageFigure extends ImageFigure implements FDShapeFigure {

	public FDImageFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
//		graphics.setAntialias(SWT.ON);
//		graphics.setXORMode(true);
//		graphics.setBackgroundColor(getBackgroundColor());
//		graphics.setForegroundColor(getForegroundColor());
//		graphics.setBackgroundColor(new Color(null, 31, 31, 31));
		
		super.paintFigure(graphics);
	}

	// TODO
	public LineBorder getLineBorder() {
//		return lineBorder;
		return null;
	}

	@Override
	public void setBorderColor(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			setBackgroundColor(color);
		}
	}
}
