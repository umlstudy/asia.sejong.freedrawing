package asia.sejong.freedrawing.model.listener;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FontInfo;


public interface TextShapeListener extends FDElementListener {

	void textChanged(String newText);
	void fontChanged(FontInfo fontInfo);
	void fontColorChanged(RGB fontColor);
}
