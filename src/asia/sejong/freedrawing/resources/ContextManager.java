package asia.sejong.freedrawing.resources;

import org.eclipse.swt.graphics.Device;

public class ContextManager {

	private static ContextManager instance;
	
	private ColorManager colorManager;
	private IconManager imageManager;
	private FontManager fontManager;
	
	private static Integer refCount = 0;
	
	private ContextManager(Device device) {
		setColorManager(new ColorManager(device));
		setImageManager(new IconManager());
		setFontManager(new FontManager(device));
		
		refCount++;
	}
	
	public void dispose() {
		synchronized ( ContextManager.class ) {
			if ( refCount == 1 ) {
				if ( colorManager != null ) {
					colorManager.dispose();
				}

				if ( imageManager != null ) {
					imageManager.dispose();
				}
				
				if ( fontManager != null ) {
					fontManager.dispose();
				}
			}
			
			if ( refCount >= 1 ) {
				refCount --;
			}
			
			if ( refCount == 0 ) {
				instance = null;
			}
			
			if ( refCount < 0 ) {
				throw new RuntimeException();
			}
		}
	}
	
	public static ContextManager newInstance(Device device) {
		if ( instance == null ) {
			synchronized (ContextManager.class) {
				if ( instance == null ) {
					instance = new ContextManager(device);
				}
			}
		}
		return instance;
	}
	
	public static ContextManager getInstance() {
		return instance;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public void setColorManager(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public IconManager getImageManager() {
		return imageManager;
	}

	public void setImageManager(IconManager imageManager) {
		this.imageManager = imageManager;
	}

	public FontManager getFontManager() {
		return fontManager;
	}

	public void setFontManager(FontManager fontManager) {
		this.fontManager = fontManager;
	}
}
