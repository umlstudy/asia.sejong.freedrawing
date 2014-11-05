package asia.sejong.freedrawing.model;

import org.eclipse.swt.graphics.RGB;

public abstract class BaseObject implements Cloneable {
	
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
	
	protected BaseObject clone() {
		BaseObject object = null;;
		try {
			object = (BaseObject)this.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		object.setBorderColor(borderColor);
		return object;
	}
}
