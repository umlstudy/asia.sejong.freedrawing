package asia.sejong.freedrawing.model;

import asia.sejong.freedrawing.model.listener.FDElementListener;
import asia.sejong.freedrawing.model.listener.TextShapeListener;


public abstract class FDTextShape extends FDShape {
	
	private String text;
	private FontInfo fontInfo;

	public FontInfo getFontInfo() {
		return fontInfo;
	}
	
	public String getText() {
		return text;
	}

	public boolean setText(String newText) {
		if (newText == null) {
			newText = "";
		}
		if (newText.equals(text)) {
			return false;
		}
		
		text = newText;
		
		// send event
		fireTextChanged(newText);
		
		return true;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
		
		// send event
		fireFontChanged(fontInfo);
	}
	
	//============================================================
	// Clonable
	@Override
	public FDTextShape clone() {
		FDTextShape object = (FDTextShape)super.clone();
		
		object.text = text;
		if ( fontInfo != null ) {
			object.setFontInfo(fontInfo.clone());
		}
		
		return object;
	}
	
	//============================================================
	// FDShapeListener
	protected void fireTextChanged(String text) {
		for ( FDElementListener l : listeners ) {
			((TextShapeListener)l).textChanged(text);
		}
	}

	protected void fireFontChanged(FontInfo fontInfo) {
		for (FDElementListener l : listeners) {
			((TextShapeListener)l).fontChanged(fontInfo);
		}
	}
}
