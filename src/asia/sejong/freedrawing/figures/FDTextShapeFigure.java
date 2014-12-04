package asia.sejong.freedrawing.figures;

import asia.sejong.freedrawing.model.FontInfo;

public interface FDTextShapeFigure extends FDShapeFigure {

	void setText(String text);

	void setFont(FontInfo fontInfo);
	
	String getText();
}
