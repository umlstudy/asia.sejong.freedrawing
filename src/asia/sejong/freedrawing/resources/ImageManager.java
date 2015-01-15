package asia.sejong.freedrawing.resources;

import java.io.ByteArrayInputStream;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

import asia.sejong.freedrawing.util.IOUtil;

public class ImageManager extends ResourceManager<String, Image> {

	public ImageManager(Device device) {
		super(device);
	}
	
	public void add(byte[] imageData) {
		String checksum = IOUtil.checksum(imageData);
		if ( !getResources().containsKey(checksum) ) {
			getResources().put(checksum, new Image(getDevice(), new ByteArrayInputStream(imageData)));
		}
	}

	public Image get(byte[] imageData) {
		String checksum = IOUtil.checksum(imageData);
		Image image = getResources().get(checksum);
		if ( image == null ) {
			add(imageData);
		}
		return getResources().get(checksum);
	}

	@Override
	protected Image createWith(String key) {
		throw new UnsupportedOperationException();
	}
}
