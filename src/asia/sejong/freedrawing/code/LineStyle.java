package asia.sejong.freedrawing.code;

import org.eclipse.swt.SWT;

public enum LineStyle {
	DASH(SWT.LINE_DASH, "데쉬"),
	DASHDOT(SWT.LINE_DASHDOT, "데쉬닷"),
	DASHDOTDOT(SWT.LINE_DASHDOTDOT, "데쉬닷닷"),
	DOT(SWT.LINE_DOT, "닷"),
	SOLID(SWT.LINE_SOLID, "솔리드");
	
	public String toString() {
		return desc;
	}
	
	private int style;
	private String desc;

	LineStyle(int style, String desc) {
		this.setStyle(style);
		this.setDesc(desc);
	}

	public int getStyle() {
		return style;
	}

	private void setStyle(int style) {
		this.style = style;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static LineStyle getLineStyle(int lineStyle) {
		for ( LineStyle element : LineStyle.values() ) {
			if ( element.getStyle() == lineStyle ) {
				return element;
			}
		}
		throw new RuntimeException("invalid line style.");
	}
}
