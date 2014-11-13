package asia.sejong.freedrawing.util;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

public class OverlayIcon extends CompositeImageDescriptor {

    // the size of the OverlayIcon
    private Point fSize = null;

    // the main image
    private ImageDescriptor fBase = null;

    // the additional image (a pin for example)
    private ImageDescriptor fOverlay = null;

    /**
     * @param base the main image
     * @param overlay the additional image (a pin for example)
     * @param size the size of the OverlayIcon
     */
    public OverlayIcon(ImageDescriptor base, ImageDescriptor overlay, Point size) {
        fBase = base;
        fOverlay = overlay;
        fSize = size;
    }

    protected void drawCompositeImage(int width, int height) {
        ImageData bg;
        if (fBase == null || (bg = fBase.getImageData()) == null) {
			bg = DEFAULT_IMAGE_DATA;
		}
        drawImage(bg, 0, 0);

        if (fOverlay != null) {
			drawTopRight(fOverlay);
		}
    }

    /**
     * @param overlay the additional image (a pin for example)
     * to be drawn on top of the main image
     */
    protected void drawTopRight(ImageDescriptor overlay) {
        if (overlay == null) {
			return;
		}
        int x = getSize().x;
        ImageData id = overlay.getImageData();
        
        if ( id.width != x ) {
        	id = id.scaledTo(x, getSize().y);
        }
        
        x -= id.width;
        drawImage(id, x, 0);
    }

    protected Point getSize() {
        return fSize;
    }
}