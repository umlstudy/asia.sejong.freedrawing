package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class FDRectFigure extends FDTextShapeFigureImpl implements FDTextShapeFigure {

	FDRectFigure() {
		setPreferredSize(100, 100);
	}
	
//	@Override
//	public void paintFigure(final Graphics graphics) {
//		graphics.setAntialias(SWT.ON);
//		graphics.setXORMode(true);
//		graphics.setBackgroundColor(getBackgroundColor());
//
//		// 드로잉역영을 일부러 크게 만듦 - 최적화 필요
//		// 사각형 회전시 최대길이를 한변으로 하는 정사각형 영역을 
//		// 드로잉 영역으로 설정함
//		Point centerPoint = GeometryUtil.centerPoint(getBounds());
//		double h = GeometryUtil.calculateHipotenuse(getBounds().width, getBounds().height);
//		graphics.setClip(GeometryUtil.createSquare(centerPoint, (int)h));
//		
//		// TODO TEST
//		System.out.println("ellipsefigure");
//		
//		boolean translated = false;
//		if ( degree > 0 ) {
//			translated = true;
//			graphics.translate(centerPoint.x, centerPoint.y);
//			rotate = true;
//			graphics.rotate((float)degree);
//			System.out.println( " degree " + degree);
//		}
//		
//		super.paintFigure(graphics);
//		
//		if ( translated ) {
//			graphics.translate(-centerPoint.x, -centerPoint.y);
//		}
//		
//		rotate = false;
//	}
	
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
