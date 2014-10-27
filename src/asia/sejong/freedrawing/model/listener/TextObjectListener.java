package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FontInfo;


public interface TextObjectListener extends BaseObjectListener {

	void fontChanged(FontInfo fontInfo);
}
