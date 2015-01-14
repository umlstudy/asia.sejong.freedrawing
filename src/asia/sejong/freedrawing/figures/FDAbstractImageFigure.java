package asia.sejong.freedrawing.figures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IImageFigure;

/**
 * Abstract implementation of the image figure. Implements attaching/detaching
 * mechanism for <code>ImageChangedListener</code>
 * 
 * @author aboyko
 * @since 3.6
 */
public abstract class FDAbstractImageFigure extends FDShapeFigureImpl implements
		IImageFigure {

	@SuppressWarnings("rawtypes")
	private List imageListeners = new ArrayList();

	@SuppressWarnings("unchecked")
	public final void addImageChangedListener(ImageChangedListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		imageListeners.add(listener);
	}

	public final void removeImageChangedListener(ImageChangedListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		imageListeners.remove(listener);
	}

	protected final void notifyImageChanged() {
		for (@SuppressWarnings("rawtypes")
		Iterator itr = imageListeners.iterator(); itr.hasNext();) {
			((ImageChangedListener) itr.next()).imageChanged();
		}
	}
}