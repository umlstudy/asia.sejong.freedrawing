package asia.sejong.freedrawing.figures;

import org.eclipse.swt.graphics.RGB;

public interface FDShapeFigure extends FDElementFigure {

	public Integer getAlpha();
	public void setAlpha(int alpha);
	public void setBackgroundColor(RGB rgbColor);
	public void setLineWidth(int lineWidth);
	public void setLineStyle(int lineStyle);
}
