package asia.sejong.freedrawing.model;

import org.eclipse.swt.graphics.RGB;

public abstract class BaseObject {
	
	private RGB borderColor;

	public RGB getBorderColor() {
		return borderColor;
	}

	public final void setBorderColor(RGB borderColor) {
		this.borderColor = borderColor;
		
		// send event
		fireBorderColorChanged(borderColor);
	}

	protected abstract void fireBorderColorChanged(RGB borderColor);
}
