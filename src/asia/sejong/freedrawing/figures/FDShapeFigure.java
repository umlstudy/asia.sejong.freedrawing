package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

public interface FDShapeFigure extends FDElementFigure {

//	Integer getAlpha();

	void setBackgroundColorEx(RGB rgbColor);
//	void setSelected(boolean selected);
	void setLocationEx(Point point);
	void setSizeEx(Dimension size);
	void setDegreeEx(double degree);
	double getDegreeEx() ;
}
