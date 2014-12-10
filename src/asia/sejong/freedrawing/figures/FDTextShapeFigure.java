package asia.sejong.freedrawing.figures;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FontInfo;

public interface FDTextShapeFigure extends FDShapeFigure {

	void setText(String text);

	void setFont(FontInfo fontInfo);
	
	String getText();

	void setFontColor(RGB fontColor);
}
