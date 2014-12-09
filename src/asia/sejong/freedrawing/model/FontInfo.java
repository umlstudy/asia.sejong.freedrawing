package asia.sejong.freedrawing.model;

import java.io.Serializable;

import org.eclipse.swt.graphics.FontData;

public class FontInfo implements Cloneable, Serializable {

	private static final long serialVersionUID = -9098716567818460239L;
	
	private String name;
	private int height;
	private int style;
	
	public FontInfo(String name, int height, int style) {
		this.name = name;
		this.height = height;
		this.style = style;
	}

	private FontInfo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public static FontInfo create(FontData fontData) {
		FontInfo fontInfo = new FontInfo();
		fontInfo.setName(fontData.getName());
		fontInfo.setHeight(fontData.getHeight());
		fontInfo.setStyle(fontData.getStyle());
		return fontInfo;
	}
	
	@Override
	public FontInfo clone() {
		FontInfo fi = new FontInfo();
		fi.setHeight(height);
		fi.setName(name);
		fi.setStyle(style);
		
		return fi;
	}
}
