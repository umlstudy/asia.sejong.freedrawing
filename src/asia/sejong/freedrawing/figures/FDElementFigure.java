package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDElement;

public interface FDElementFigure {
	void setLineWidthEx(int lineWidth);
	void setLineStyleEx(int lineStyle);
	void setLineColorEx(RGB rgbColor);
	
	void setModelAttributes(FDElement model);
	
	IFigure getParent();
}
