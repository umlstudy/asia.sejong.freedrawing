package asia.sejong.freedrawing.model;


public abstract class FDTextShape extends FDShape {
	
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
	
	protected FDTextShape clone() {
		FDTextShape object = (FDTextShape)super.clone();
		if ( fontInfo != null ) {
			object.setFontInfo(fontInfo.clone());
		}
		return object;
	}
}
