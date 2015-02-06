package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import asia.sejong.freedrawing.util.DebugUtil;

public class FDRectFigure extends FDTextShapeFigureImpl {

	FDRectFigure() {
		setPreferredSize(100, 100);
	}
	
	//FIXME FOR DEBUG
	@Override
	public void paint(Graphics graphics) {
		System.out.println("\n\n\n"+getLocation() + "\n"+ this.toString()+ " fillShape");
		DebugUtil.printStackTraceElement(Thread.currentThread().getStackTrace(), System.out);
		super.paint(graphics);
	}
	
	//FIXME FOR DEBUG
	@Override
	public void remove(IFigure figure) {
		//super.remove(figure);
	}
	
	//FIXME FOR DEBUG
	@Override
	public void erase() {
//		super.erase();
	}
	
	//FIXME FOR DEBUG
	@Override
	public void validate() {
		super.validate();
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillRectangle(getBoundsInZeroPoint());
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);

		Rectangle r = Rectangle.SINGLETON.setBounds(getBoundsInZeroPoint());
		r.x += inset1;
		r.y += inset1;
		r.width -= inset1 + inset2;
		r.height -= inset1 + inset2;

		graphics.drawRectangle(r);
	}
}
