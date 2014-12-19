package asia.sejong.freedrawing.model.listener;

import org.eclipse.swt.graphics.RGB;



public interface FDShapeListener extends FDWireEndPointListener {

	void sizeChanged(int newWidth, int newHeight);

	void backgroundColorChanged(RGB rgbColor);

	void angleChanged(int angle);
}
