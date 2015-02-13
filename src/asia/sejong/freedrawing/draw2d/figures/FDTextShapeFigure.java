package asia.sejong.freedrawing.draw2d.figures;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FontInfo;

public interface FDTextShapeFigure extends FDShapeFigure {

	void setTextEx(String text);

	void setFontInfoEx(FontInfo fontInfo);
	
//	String getText();

	void setFontColorEx(RGB fontColor);
}
