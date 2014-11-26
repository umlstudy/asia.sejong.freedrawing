package asia.sejong.freedrawing.model;

import org.eclipse.swt.graphics.RGB;

public abstract class FDElement implements Cloneable {
	
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
	
	protected FDElement clone() {
		
//		Object cloned;
//		try {
//			cloned = super.clone();
//		} catch (CloneNotSupportedException e) {
//			throw new RuntimeException(e);
//		}
//		return (FDElement)cloned;
//		
		FDElement object = null;;
		try {
			object = (FDElement)this.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		object.setBorderColor(borderColor);
		return object;
	}
	
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
