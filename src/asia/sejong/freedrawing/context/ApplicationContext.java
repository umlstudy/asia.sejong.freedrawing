package asia.sejong.freedrawing.context;

import org.eclipse.swt.graphics.Device;

import asia.sejong.freedrawing.resources.ColorManager;
import asia.sejong.freedrawing.resources.FontManager;
import asia.sejong.freedrawing.resources.IconManager;
import asia.sejong.freedrawing.resources.ImageManager;

public class ApplicationContext {

	private static ApplicationContext instance;
	
	private ColorManager colorManager;
	private IconManager iconManager;
	private FontManager fontManager;
	private ImageManager imageManager;
	
	private static int referenceCount = 0;
	
	private ApplicationContext(Device device) {
		setColorManager(new ColorManager(device));
		setIconManager(new IconManager());
		setFontManager(new FontManager(device));
		setImageManager(new ImageManager(device));
	}
	
	public void dispose() {
		synchronized ( ApplicationContext.class ) {
			if ( referenceCount == 1 ) {
				if ( colorManager != null ) {
					colorManager.dispose();
				}

				if ( iconManager != null ) {
					iconManager.dispose();
				}
				
				if ( fontManager != null ) {
					fontManager.dispose();
				}
				
				if ( imageManager != null ) {
					imageManager.dispose();
				}
			}
			
			if ( referenceCount >= 1 ) {
				referenceCount --;
			}
			
			if ( referenceCount == 0 ) {
				instance = null;
			}
			
			if ( referenceCount < 0 ) {
				throw new RuntimeException();
			}
		}
	}
	
	public static ApplicationContext newInstance(Device device) {
		if ( instance == null ) {
			synchronized (ApplicationContext.class) {
				if ( instance == null ) {
					instance = new ApplicationContext(device);
				}
			}
		}
		
		referenceCount++;
		
		return instance;
	}
	
	public static ApplicationContext getInstance() {
		return instance;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public void setColorManager(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public IconManager getIconManager() {
		return iconManager;
	}

	public void setIconManager(IconManager iconManager) {
		this.iconManager = iconManager;
	}

	public FontManager getFontManager() {
		return fontManager;
	}

	public void setFontManager(FontManager fontManager) {
		this.fontManager = fontManager;
	}

	public ImageManager getImageManager() {
		return imageManager;
	}

	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}
}
