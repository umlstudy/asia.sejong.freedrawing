package asia.sejong.freedrawing.resources;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import asia.sejong.freedrawing.model.FontInfo;

public class FontManager extends ResourceManager<FontInfo, Font> {

	public FontManager(Device device) {
		super(device);
	}

	@Override
	protected Font createWith(FontInfo fi) {
		return new Font(getDevice(), new FontData(fi.getName(), fi.getHeight(), fi.getStyle()));
	}
}
