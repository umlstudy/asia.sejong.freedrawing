package asia.sejong.freedrawing.model;

import asia.sejong.freedrawing.model.listener.TextObjectListener;

public abstract class TextObject extends BaseObject implements TextObjectListener {
	
	private FontInfo fontInfo;

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
		
		fontChanged(fontInfo);
	}
}
