package asia.sejong.freedrawing.model.listener;

import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.code.LineStyle;


public interface FDElementListener extends FDBaseListener {

	void lineColorChanged(RGB rgbColor);

	void lineStyleChanged(LineStyle lineStyle);

	void lineWidthChanged(float lineWidth);
}
