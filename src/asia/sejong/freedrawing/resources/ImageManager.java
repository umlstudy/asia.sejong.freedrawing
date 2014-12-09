package asia.sejong.freedrawing.resources;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

import asia.sejong.freedrawing.util.IOUtil;

public class ImageManager {

	private Map<String, Image> images;
	private Device device;

	ImageManager(Device device) {
		this.device = device;
		this.images = new HashMap<String, Image>();
	}
	
	public void add(byte[] imageData) {
		String checksum = IOUtil.checksum(imageData);
		if ( !images.containsKey(checksum) ) {
			images.put(checksum, new Image(device, new ByteArrayInputStream(imageData)));
		}
	}

	public Image get(byte[] imageData) {
		String checksum = IOUtil.checksum(imageData);
		Image image = images.get(checksum);
		if ( image == null ) {
			add(imageData);
		}
		return images.get(checksum);
	}

	void dispose() {
		for ( Image image : images.values() ) {
			image.dispose();
		}
		
		images = null;
	}
}
