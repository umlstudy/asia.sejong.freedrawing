package asia.sejong.freedrawing.resources;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageManager {

	private final Image connectionImage;
	private final Image rectangleImage;
	private final Image selectImage;
	private final Image marqueeImage;
	private final Image colorPickImage;
	private final Image fontPickImage;
	
	ImageManager() {
		connectionImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("image/connection.png"));
		rectangleImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("image/rectangle.png"));
		selectImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("image/select.png"));
		marqueeImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("image/marquee.png"));
		colorPickImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("image/colorpick.png"));
		fontPickImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("image/fontpick.png"));
	}
	
	void dispose() {
		connectionImage.dispose();
		rectangleImage.dispose();
		selectImage.dispose();
		marqueeImage.dispose();
		colorPickImage.dispose();
		fontPickImage.dispose();
	}
	
	private static ImageDescriptor getDescriptor(Image image ) {
		return ImageDescriptor.createFromImage(image);
	}
	
	public ImageDescriptor getConnectionImageDescriptor() {
		return getDescriptor(connectionImage);
	}
	
	public ImageDescriptor getRectangleImageDescriptor() {
		return getDescriptor(rectangleImage);
	}
	
	public ImageDescriptor getSelectImageDescriptor() {
		return getDescriptor(selectImage);
	}
	
	public ImageDescriptor getMarqueeImageDescriptor() {
		return getDescriptor(marqueeImage);
	}
	
	public ImageDescriptor getColorPickImageDescriptor() {
		return getDescriptor(colorPickImage);
	}

	public ImageDescriptor getFontPickImageDescriptor() {
		return getDescriptor(fontPickImage);
	}
}
