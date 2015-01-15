package asia.sejong.freedrawing.resources;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;

public class ColorManager extends ResourceManager<RGB, Color> {

	public ColorManager(Device device) {
		super(device);
	}
	
	@Override
	protected Color createWith(RGB key) {
		return new Color(getDevice(), key);
	}
}
