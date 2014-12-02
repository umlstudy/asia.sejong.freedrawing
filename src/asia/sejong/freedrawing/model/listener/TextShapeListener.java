package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FontInfo;


public interface TextShapeListener extends FDElementListener {

	void fontChanged(FontInfo fontInfo);
}
