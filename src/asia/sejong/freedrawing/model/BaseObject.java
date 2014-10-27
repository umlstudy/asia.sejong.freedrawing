package asia.sejong.freedrawing.model;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.BaseObjectListener;

public abstract class BaseObject implements BaseObjectListener {
	
	private RGB borderColor;

	public RGB getBorderColor() {
		return borderColor;
	}

	public final void setBorderColor(RGB borderColor) {
		this.borderColor = borderColor;
		
		// listener
		borderColorChanged(borderColor);
	}
}
