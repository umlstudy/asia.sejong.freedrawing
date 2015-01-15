package asia.sejong.freedrawing.resources;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import asia.sejong.freedrawing.util.OverlayIcon;

public class IconManager {
	
	public static enum IconType {
		NORMAL,
		SELECTED,
		MOUSE_OVER;
	}

	private final Image[] connectionImage = new Image[IconType.values().length];
	private final Image[] rectangleImage = new Image[IconType.values().length];
	private final Image[] selectImage = new Image[IconType.values().length];
	private final Image[] marqueeImage = new Image[IconType.values().length];
	private final Image[] colorPickImage = new Image[IconType.values().length];
	private final Image[] fontPickImage = new Image[IconType.values().length];
	
	public IconManager() {
		Display display = Display.getCurrent();
		
		Point point = new Point(32, 32);
		int arc = 4;
		Image selectedImageBg  = createBackgroundImage(display, point, arc, SWT.COLOR_BLUE);
		Image mouseOverImageBg = createBackgroundImage(display, point, arc, SWT.COLOR_DARK_GRAY);

		Image image = new Image(display, getClass().getResourceAsStream("icons/connection.png"));
		createImages(connectionImage, image, selectedImageBg, mouseOverImageBg, point);
		
		image = new Image(display, getClass().getResourceAsStream("icons/rectangle.png"));
		createImages(rectangleImage, image, selectedImageBg, mouseOverImageBg, point);
		
		image = new Image(display, getClass().getResourceAsStream("icons/select.png"));
		createImages(selectImage, image, selectedImageBg, mouseOverImageBg, point);
		
		image = new Image(display, getClass().getResourceAsStream("icons/marquee.png"));
		createImages(marqueeImage, image, selectedImageBg, mouseOverImageBg, point);
		
		image = new Image(display, getClass().getResourceAsStream("icons/colorpick.png"));
		createImages(colorPickImage, image, selectedImageBg, mouseOverImageBg, point);
		
		image = new Image(display, getClass().getResourceAsStream("icons/fontpick.png"));
		createImages(fontPickImage, image, selectedImageBg, mouseOverImageBg, point);
		
		selectedImageBg.dispose();
		mouseOverImageBg.dispose();
	}
	
	private void createImages(Image[] images, Image targetImage, Image selectedImageBg, Image mouseOverImageBg, Point point) {
		
		if ( targetImage.getBounds().width != point.x ) {
			ImageData imageData = targetImage.getImageData();
			ImageData scaled = imageData.scaledTo(point.x, point.y);
			targetImage.dispose();
			targetImage = new Image(Display.getCurrent(), scaled);
			images[IconType.NORMAL.ordinal()] = new Image(Display.getCurrent(), scaled);
		} else {
			images[IconType.NORMAL.ordinal()] = targetImage;
		}

		ImageDescriptor tiDescriptor = ImageDescriptor.createFromImage(targetImage);
		OverlayIcon selectedBgImageIcon = new OverlayIcon(
				ImageDescriptor.createFromImage(selectedImageBg)
				, tiDescriptor
				, point);
		images[IconType.SELECTED.ordinal()] = selectedBgImageIcon.createImage();

		OverlayIcon mouseOverBgImageIcon = new OverlayIcon(
				ImageDescriptor.createFromImage(mouseOverImageBg)
				, tiDescriptor
				, point);
		images[IconType.MOUSE_OVER.ordinal()] = mouseOverBgImageIcon.createImage();
	}

	public void dispose() {
		disposeImages(connectionImage);
		disposeImages(rectangleImage);
		disposeImages(selectImage);
		disposeImages(marqueeImage);
		disposeImages(colorPickImage);
		disposeImages(fontPickImage);
	}
	
	private void disposeImages(Image[] images) {
		for ( Image image : images ) {
			image.dispose();
		}
	}

	private static ImageDescriptor getDescriptor(Image image ) {
		return ImageDescriptor.createFromImage(image);
	}
	
	public ImageDescriptor getConnectionImageDescriptor(IconType type) {
		return getDescriptor(connectionImage[type.ordinal()]);
	}
	
	public Image[] getConnectionImages() {
		return connectionImage;
	}
	
	public ImageDescriptor getRectangleImageDescriptor(IconType type) {
		return getDescriptor(rectangleImage[type.ordinal()]);
	}
	
	public Image[] getRectangleImages() {
		return rectangleImage;
	}
	
	public ImageDescriptor getSelectImageDescriptor(IconType type) {
		return getDescriptor(selectImage[type.ordinal()]);
	}
	
	public Image[] getSelectImages() {
		return selectImage;
	}
	
	public ImageDescriptor getMarqueeImageDescriptor(IconType type) {
		return getDescriptor(marqueeImage[type.ordinal()]);
	}
	
	public Image[] getMarqueeImages() {
		return marqueeImage;
	}
	
	public ImageDescriptor getColorPickImageDescriptor(IconType type) {
		return getDescriptor(colorPickImage[type.ordinal()]);
	}
	
	public Image[] getColorPickImages() {
		return colorPickImage;
	}
	
	public ImageDescriptor getFontPickImageDescriptor(IconType type) {
		return getDescriptor(fontPickImage[type.ordinal()]);
	}
	
	public Image[] getFontPickImages() {
		return fontPickImage;
	}
	
	private static final Image createBackgroundImage(Display display, Point point, int arc, int color) {
		Image image = new Image(display, point.x, point.y);
		GC gc = new GC(image);
		gc.setAntialias(SWT.ON);
		gc.setBackground(display.getSystemColor(color));
		gc.fillRoundRectangle(0, 0, point.x, point.y, arc, arc);
		gc.dispose();
		
		return image;
	}
	
	// ----------------------------
	// TEST
	public static void main(String... args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Canvas Example");
		shell.setLayout(new FillLayout());

		Canvas canvas = new Canvas(shell, SWT.NONE);

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image im = new IconManager().getConnectionImageDescriptor(IconType.SELECTED).createImage();
				e.gc.drawImage(im, 10, 10);
				im.dispose();
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
