package asia.sejong.freedrawing.model;


public abstract class TextObject extends BaseObject {
	
	private FontInfo fontInfo;

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
		
		// send event
		fireFontChanged(fontInfo);
	}

	protected abstract void fireFontChanged(FontInfo fontInfo);
	
	protected TextObject clone() {
		TextObject object = (TextObject)super.clone();
		if ( fontInfo != null ) {
			object.setFontInfo(fontInfo.clone());
		}
		return object;
	}
}
