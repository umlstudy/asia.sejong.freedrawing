package asia.sejong.freedrawing.model.listener;

import org.eclipse.swt.graphics.RGB;


public interface FDElementListener extends FDBaseListener {

	void lineColorChanged(RGB rgbColor);

	void lineStyleChanged(int lineStyle);

	void lineWidthChanged(float lineWidth);
}
