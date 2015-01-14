package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * A Figure that simply contains an Image. Use this Figure, instead of a Label,
 * when displaying Images without any accompanying text. This figure is not
 * intended to have a layout mananger or children.
 * <P>
 * Note that it is the client's responsibility to dispose the given image. There
 * is no "free" resource management in draw2d.
 * 
 * @author Pratik Shah
 */
public class FDImageFigure extends FDAbstractImageFigure {

	private Image img;
	private Dimension size = new Dimension();
	private int alignment;

	/**
	 * Constructor<br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 */
	public FDImageFigure() {
		this(null, PositionConstants.CENTER);
	}

	/**
	 * Constructor<br>
	 * The default alignment is <code>PositionConstants.CENTER</code>.
	 * 
	 * @param image
	 *            The Image to be displayed
	 */
	public FDImageFigure(Image image) {
		this(image, PositionConstants.CENTER);
	}

	/**
	 * Constructor
	 * 
	 * @param image
	 *            The Image to be displayed
	 * @param alignment
	 *            A PositionConstant indicating the alignment
	 * 
	 * @see ImageFigure#setImage(Image)
	 * @see ImageFigure#setAlignment(int)
	 */
	public FDImageFigure(Image image, int alignment) {
		setImage(image);
		setAlignment(alignment);
	}

	/**
	 * @return The Image that this Figure displays
	 */
	public Image getImage() {
		return img;
	}

	/**
	 * Calculates the necessary size to display the Image within the figure's
	 * client area.
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (getInsets() == NO_INSETS)
			return size;
		Insets i = getInsets();
		return size.getExpanded(i.getWidth(), i.getHeight());
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		
		//paintFigureNotFit(graphics);
		paintFigureFit(graphics);
	}
	
	private void paintFigureFit(Graphics graphics) {

		if (getImage() == null) {
			return;
		}

		int drawAreaImageWidth = getBounds().width;
		int drawAreaImageHeight = getBounds().height;
		Image drawAreaImage = new Image(Display.getCurrent(), drawAreaImageWidth, drawAreaImageHeight);
		GC offscreenGc = new GC(drawAreaImage);
		// Draw the background
		offscreenGc.fillRectangle(drawAreaImage.getBounds());
		offscreenGc.setAntialias(SWT.ON);
		offscreenGc.setAdvanced(true);
		
		Image image = getImage();
		float wScale = 1.0f;
		float hScale = 1.0f;
		ImageData data = image.getImageData();
		if ( drawAreaImageWidth != data.width) {
			wScale = drawAreaImageWidth / (float) data.width;
		}
		if ( drawAreaImageHeight != data.height) {
			hScale = drawAreaImageHeight / (float) data.height;
		}
		
		if (wScale > 0.0 && hScale > 0.0 ) {
			Image scaledImage = new Image(Display.getCurrent(), data.scaledTo(Math.round(data.width * wScale), Math.round(data.height * hScale)));
			offscreenGc.drawImage(scaledImage, 0, 0);
			scaledImage.dispose();
		}
		
		Rectangle bounds = getBoundsInZeroPoint();
		graphics.drawImage(drawAreaImage, bounds.x, bounds.y);
		drawAreaImage.dispose();
	}
	
	protected void paintFigureNotFit(Graphics graphics) {

		if (getImage() == null)
			return;

		// do not fit
		int x, y;
		Rectangle area = getBoundsInZeroPoint().getShrinked(getInsets());
		switch (alignment & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.NORTH:
			y = area.y;
			break;
		case PositionConstants.SOUTH:
			y = area.y + area.height - size.height;
			break;
		default:
			y = (area.height - size.height) / 2 + area.y;
			break;
		}
		switch (alignment & PositionConstants.EAST_WEST) {
		case PositionConstants.EAST:
			x = area.x + area.width - size.width;
			break;
		case PositionConstants.WEST:
			x = area.x;
			break;
		default:
			x = (area.width - size.width) / 2 + area.x;
			break;
		}
		graphics.drawImage(getImage(), x, y);
	}

	/**
	 * Sets the alignment of the Image within this Figure. The alignment comes
	 * into play when the ImageFigure is larger than the Image. The alignment
	 * could be any valid combination of the following:
	 * 
	 * <UL>
	 * <LI>PositionConstants.NORTH</LI>
	 * <LI>PositionConstants.SOUTH</LI>
	 * <LI>PositionConstants.EAST</LI>
	 * <LI>PositionConstants.WEST</LI>
	 * <LI>PositionConstants.CENTER or PositionConstants.NONE</LI>
	 * </UL>
	 * 
	 * @param flag
	 *            A constant indicating the alignment
	 */
	public void setAlignment(int flag) {
		alignment = flag;
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 * <p>
	 * IMPORTANT: Note that it is the client's responsibility to dispose the
	 * given image.
	 * 
	 * @param image
	 *            The Image to be displayed. It can be <code>null</code>.
	 */
	public void setImage(Image image) {
		if (img == image)
			return;
		img = image;
		if (img != null)
			size = new Rectangle(image.getBounds()).getSize();
		else
			size = new Dimension();
		revalidate();
		notifyImageChanged();
		repaint();
	}

	@Override
	protected void fillShape(Graphics graphics) {
	}

	@Override
	protected void outlineShape(Graphics graphics) {
	}
}