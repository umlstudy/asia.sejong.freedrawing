package asia.sejong.freedrawing.draw2d.figures;

import org.eclipse.draw2d.Graphics;

public class FDLabelFigure extends FDTextShapeFigureImpl {

	FDLabelFigure() {
		setPreferredSize(100, 100);
		setAlpha(0);
	}

	@Override
	protected void fillShape(Graphics graphics) {
	}

	@Override
	protected void outlineShape(Graphics graphics) {
	}
}
