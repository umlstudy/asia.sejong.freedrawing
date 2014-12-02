package asia.sejong.freedrawing.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.listener.FDElementListener;

public abstract class FDElement implements Cloneable {
	
	private RGB borderColor;
	
	transient protected Set<FDElementListener> listeners = new HashSet<FDElementListener>();

	public RGB getBorderColor() {
		return borderColor;
	}

	public final void setBorderColor(RGB borderColor) {
		this.borderColor = borderColor;
		
		// send event
		fireBorderColorChanged(borderColor);
	}

	protected void fireBorderColorChanged(RGB rgbColor) {
		for (FDElementListener l : listeners) {
			l.borderColorChanged(rgbColor);
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
	protected FDElement clone() {
		
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
	// Test
	public static void main(String[] args) {
		FDElement ele = new FDElement() {
			
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
