package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.model.FDElement;

public interface FDElementFigure extends IFigure {
	void setLineWidthEx(float lineWidth);
	void setLineStyleEx(LineStyle lineStyle);
	void setLineColorEx(RGB rgbColor);
	void setAlphaEx(Integer alpha);
	void setFeedbackEx(boolean feedback);
	boolean isFeedbackEx();
	
	void setModelAttributes(FDElement model);
	
	IFigure getParent();
}
