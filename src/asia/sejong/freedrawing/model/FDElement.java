package asia.sejong.freedrawing.model;

import java.io.Serializable;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDBaseListener;
import asia.sejong.freedrawing.model.listener.FDElementListener;

public abstract class FDElement extends FDBase implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1785663272619287600L;

	private RGB borderColor;

	public RGB getBorderColor() {
		return borderColor;
	}

	public final void setBorderColor(RGB borderColor) {
		this.borderColor = borderColor;
		
		// send event
		fireBorderColorChanged(borderColor);
	}

	protected void fireBorderColorChanged(RGB rgbColor) {
		for (FDBaseListener l : listeners) {
			((FDElementListener)l).borderColorChanged(rgbColor);
		}
	}
	
	//============================================================
	// Listener add and remove
	
	public void addListener(FDElementListener listener ) {
		if ( listener != null ) {
			listeners.add(listener);
		}
	}

	public void removeListener(FDElementListener listener ) {
		if ( listener != null ) {
			listeners.remove(listener);
		}
	}
	
	//============================================================
	// FDElement
	
	@Override
	public FDElement clone() {
		
		FDElement object = null;;
		try {
			object = (FDElement)this.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		object.setBorderColor(borderColor);
		return object;
	}
	
	//============================================================
	// For Debug
	
	public static void main(String[] args) {
		FDElement ele = new FDElement() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void fireBorderColorChanged(RGB borderColor) {
				
			}
		};
		
		ele.setBorderColor(new RGB(1, 2, 3));
		try {
			FDElement clone = (FDElement)ele.clone();
			System.out.println(clone);
		} catch ( Exception e) {
			
		}
	}
}
