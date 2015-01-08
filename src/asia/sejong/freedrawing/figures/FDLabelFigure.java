package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDLabel;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.resources.ContextManager;

public class FDLabelFigure extends Label implements FDTextShapeFigure {

	FDLabelFigure() {
		setPreferredSize(100, 100);
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
	}

	@Override
	public void setTextEx(String text) {
		super.setText(text);
	}

	@Override
	public void setFontInfoEx(FontInfo fontInfo) {
		Font font = null;
		if ( fontInfo != null ) {
			font = ContextManager.getInstance().getFontManager().get(fontInfo);
		}
		setFont(font);
		
	}

	@Override
	public void setAlphaEx(int alpha) {
	}
	
	@Override
	public void setBackgroundColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setBackgroundColor(color);
		}	
	}
	
	@Override
	public void setFontColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ContextManager.getInstance().getColorManager().get(rgbColor);
			super.setForegroundColor(color);
		}	
	}

	@Override
	public void setLineWidthEx(int lineWidth) {
		
	}

	@Override
	public void setLineStyleEx(int lineStyle) {
		
	}

	@Override
	public void setLineColorEx(RGB rgbColor) {
		
	}

	@Override
	public void setModelAttributes(FDElement model_) {
		FDLabel model = (FDLabel)model_;
		
		setTextEx(model.getText());
		setFontInfoEx(model.getFontInfo());
		setFontColorEx(model.getFontColor());
		//setAlpah(model.getAlpha());
		setBackgroundColorEx(model.getBackgroundColor());
		setLineWidthEx(model.getLineWidth());
		setLineStyleEx(model.getLineStyle());
		setLineColorEx(model.getLineColor());
		
	}
	
	@Override
	public void setLocationEx(Point point) {
		setLocation(point);
	}

	@Override
	public void setSizeEx(int width, int height) {
		setSizeEx(width, height);
	}

	@Override
	public void setDegreeEx(double degree) {
		
	}

//	@Override
//	public void setSelected(boolean selected) {
//		// TODO Auto-generated method stub
//		
//	}
}
