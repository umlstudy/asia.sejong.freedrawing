package asia.sejong.freedrawing.resources.image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageManager {

	private final Image connectionImage;
	private final Image rectangleImage;
	private final Image selectImage;
	private final Image marqueeImage;

	private ImageManager() {
		connectionImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("connection.png"));
		rectangleImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("rectangle.png"));
		selectImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("select.png"));
		marqueeImage = new Image(Display.getCurrent(), getClass().getResourceAsStream("marquee.png"));
	}
	
	public void dispose() {
		connectionImage.dispose();
		rectangleImage.dispose();
		selectImage.dispose();
		marqueeImage.dispose();
	}
	
	public static ImageManager newInstance() {
		return new ImageManager();
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
}
