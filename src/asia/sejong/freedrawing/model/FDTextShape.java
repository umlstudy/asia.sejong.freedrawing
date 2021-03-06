package asia.sejong.freedrawing.model;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDBaseListener;
import asia.sejong.freedrawing.model.listener.TextShapeListener;


public abstract class FDTextShape extends FDShape {
	
	private static final long serialVersionUID = 5724530676305174820L;
	
	private String text;
	private FontInfo fontInfo;
	private RGB fontColor;

	public FontInfo getFontInfo() {
		return fontInfo;
	}
	
	public RGB getFontColor() {
		return fontColor;
	}

	public void setFontColor(RGB fontColor) {
		this.fontColor = fontColor;
		
		fireFontColorChanged(fontColor);
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
		object.fontColor = fontColor;
		if ( fontInfo != null ) {
			object.setFontInfo(fontInfo.clone());
		}
		
		return object;
	}
	
	//============================================================
	// FDShapeListener
	protected void fireTextChanged(String text) {
		for ( FDBaseListener l : listeners ) {
			((TextShapeListener)l).textChanged(text);
		}
	}

	protected void fireFontChanged(FontInfo fontInfo) {
		for (FDBaseListener l : listeners) {
			((TextShapeListener)l).fontChanged(fontInfo);
		}
	}
	
	protected void fireFontColorChanged(RGB fontColor) {
		for (FDBaseListener l : listeners) {
			((TextShapeListener)l).fontColorChanged(fontColor);
		}
	}
}
