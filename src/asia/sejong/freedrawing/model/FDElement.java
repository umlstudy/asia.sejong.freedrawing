package asia.sejong.freedrawing.model;

import java.io.Serializable;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.model.listener.FDBaseListener;
import asia.sejong.freedrawing.model.listener.FDElementListener;

public abstract class FDElement extends FDBase implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1785663272619287600L;

	private RGB lineColor;
	private LineStyle lineStyle;
	private float lineWidth;
	private Integer alpha;
	
	protected FDElement() {
		lineStyle = LineStyle.SOLID;
	}

	public RGB getLineColor() {
		return lineColor;
	}

	public final void setLineColor(RGB lineColor) {
		this.lineColor = lineColor;
		
		// send event
		fireLineColorChanged(lineColor);
	}
	
	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		
		// send event
		fireLineStyleChanged(lineStyle);
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		
		// send event
		fireLineWidthChanged(lineWidth);
	}

	public Integer getAlpha() {
		return alpha;
	}

	public void setAlpha(Integer alpha) {
		this.alpha = alpha;
		
		fireAlphaChanged(alpha);
	}

	protected void fireLineColorChanged(RGB rgbColor) {
		for (FDBaseListener l : listeners) {
			((FDElementListener)l).lineColorChanged(rgbColor);
		}
	}
	
	protected void fireLineStyleChanged(LineStyle lineStyle) {
		for (FDBaseListener l : listeners) {
			((FDElementListener)l).lineStyleChanged(lineStyle);
		}
	}
	
	protected void fireLineWidthChanged(float lineWidth) {
		for (FDBaseListener l : listeners) {
			((FDElementListener)l).lineWidthChanged(lineWidth);
		}
	}
	
	protected void fireAlphaChanged(Integer alpha) {
		for (FDBaseListener l : listeners) {
			((FDElementListener)l).alphaChanged(alpha);
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
		
		object.setLineColor(lineColor);
		object.setLineWidth(lineWidth);
		object.setLineStyle(lineStyle);
		object.setAlpha(alpha);
		
		return object;
	}
	
	//============================================================
	// For Debug
	
	public static void main(String[] args) {
		FDElement ele = new FDElement() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void fireLineColorChanged(RGB borderColor) {
				
			}
		};
		
		ele.setLineColor(new RGB(1, 2, 3));
		try {
			FDElement clone = (FDElement)ele.clone();
			System.out.println(clone);
		} catch ( Exception e) {
			
		}
	}
}
