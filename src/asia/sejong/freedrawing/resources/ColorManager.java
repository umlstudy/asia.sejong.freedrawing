package asia.sejong.freedrawing.resources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;

public class ColorManager {

	private Map<RGB, Color> colors;
	private Device device;

	ColorManager(Device device) {
		this.device = device;
		this.colors = new HashMap<RGB, Color>();
	}
	
	public void add(RGB rgb) {
		if ( !colors.containsKey(rgb) ) {
			colors.put(rgb, new Color(device, rgb));
		}
	}

	public Color get(RGB rgb) {
		Color color = colors.get(rgb);
		if ( color == null ) {
			add(rgb);
		}
		return colors.get(rgb);
	}

	void dispose() {
		for ( Color color : colors.values() ) {
			color.dispose();
		}
		
		colors = null;
	}
}
