package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDLabelFigure extends Label implements FDTextShapeFigure {

	public FDLabelFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
	}

	// TODO
	public LineBorder getLineBorder() {
		return null;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
	}

	@Override
	public void setBorderColor(RGB rgbColor) {
	}

	@Override
	public void setFont(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		setFont(font);
		
	}
}
