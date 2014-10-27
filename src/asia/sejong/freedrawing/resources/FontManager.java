package asia.sejong.freedrawing.resources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import asia.sejong.freedrawing.model.FontInfo;

public class FontManager {

	private Map<FontInfo, Font> fonts;
	private Device device;

	FontManager(Device device) {
		this.device = device;
		this.fonts = new HashMap<FontInfo, Font>();
	}
	
	public void add(FontInfo fi) {
		if ( !fonts.containsKey(fi) ) {
			fonts.put(fi, new Font(device, new FontData(fi.getName(), fi.getHeight(), fi.getStyle())));
		}
	}

	public Font get(FontInfo fi) {
		Font font = fonts.get(fi);
		if ( font == null ) {
			add(fi);
		}
		return fonts.get(fi);
	}

	void dispose() {
		for ( Font font : fonts.values() ) {
			font.dispose();
		}
		
		fonts = null;
	}
}
