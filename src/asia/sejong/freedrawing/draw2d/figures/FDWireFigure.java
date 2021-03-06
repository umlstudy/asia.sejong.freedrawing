package asia.sejong.freedrawing.draw2d.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.context.ApplicationContext;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDWire;

public class FDWireFigure extends PolylineConnection implements FDElementFigure {
	
	private boolean roundedCorner = false;
	private boolean feedback;
	
	FDWireFigure() {
		super();
	}
	
	private boolean isRoundedCorner() {
		return roundedCorner;
	}

	public void setRoundedCorner(boolean roundedCorner) {
		this.roundedCorner = roundedCorner;
	}
	
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
	}
	
	protected void outlineShape(Graphics g) {
		Display display = Display.getCurrent();
		Path path = new Path(display);
		PointList pointList = getPoints();
		
		g.setAntialias(SWT.ON);
		
		if ( getForegroundColor() != null ) {
			g.setForegroundColor(getForegroundColor());
		}
		g.setLineWidth(getLineWidth());
		if ( getAlpha() != null ) {
			g.setAlpha(getAlpha());
		}
		g.setInterpolation(SWT.HIGH);
		
		// TODO
		if ( isRoundedCorner() ) {
			
			if ( pointList == null || pointList.size() < 1 ) {
				super.outlineShape(g);
				return;
			}
			Point firstPoint = pointList.getFirstPoint();
			path.moveTo(firstPoint.x, firstPoint.y);
			for ( int i=1;i<pointList.size(); i++ ) {
				Point currPoint = pointList.getPoint(i);
				
				if ( (i+1) < pointList.size() ) {
					Point startPoint = pointList.getPoint(i-1);
					Point endPoint = pointList.getPoint(i+1);
					Point startBezierPoint1 = getBezierPoint(startPoint, currPoint, 20, 0.5f);
					Point endBezierPoint1 = getBezierPoint(endPoint, currPoint, 20, 0.5f);
					
					Point startBezierPoint2 = getBezierPoint(startPoint, currPoint, 10, 0.25f);
					Point endBezierPoint2 = getBezierPoint(endPoint, currPoint, 10, 0.25f);
					
//				System.out.println("-----------------------");
//				System.out.println("SP : " + startPoint);
//				System.out.println("MP : " + currPoint);
//				System.out.println("EP : " + endPoint);
//				System.out.println("SB1 : " + startBezierPoint1);
//				System.out.println("EB1 : " + endBezierPoint1);
//				System.out.println("SB2 : " + startBezierPoint2);
//				System.out.println("EB2 : " + endBezierPoint2);
//				g.drawText(String.format("S1,%d",startBezierPoint1.y), startBezierPoint1);
//				g.drawText(String.format("S2,%d",startBezierPoint2.y), startBezierPoint2);
//				g.drawText("E2", endBezierPoint2);
//				g.drawText("E1", endBezierPoint1);
					if ( startBezierPoint1 == null || endBezierPoint1 == null || startBezierPoint2 == null || endBezierPoint2 == null) {
						path.lineTo(currPoint.x, currPoint.y);
					} else {
						path.lineTo(startBezierPoint1.x, startBezierPoint1.y);
						path.cubicTo(startBezierPoint2.x, startBezierPoint2.y, endBezierPoint2.x, endBezierPoint2.y, endBezierPoint1.x, endBezierPoint1.y);
					}
				} else {
					path.lineTo(currPoint.x, currPoint.y);
				}
			}
			
			g.drawPath(path);
		} else {
			super.outlineShape(g);
		}
	}

	protected static int getIntercept(float slope, int x, int y) {
		return y - (int)(slope * x);
	}

	protected static float getSlope(Point sp, Point ep) {
		int sizeX = Math.abs(ep.x - sp.x);
		int sizeY = Math.abs(ep.y - sp.y);
		
		float slope=0f;
		if ( ep.x >= sp.x && ep.y >= sp.y ) {
			slope = (float)sizeY/sizeX;
		} else {
			slope = ((float)sizeY/sizeX) * -1f;
		}
		
		return slope;
	}
	
	protected static int getY(Point sp, Point ep, int newX) {
		
		float slope = getSlope(sp, ep);
		float inte = getIntercept(slope, sp.x, sp.y);
		return (int)((float)newX*slope + inte);
	}

	/**
	 * FOR TEST
	 * @param args
	 */
	public static void main(String[] args ) {
		
//		System.out.println(getBezierPoint(new Point(100, 100), new Point(400, 500), 3, 20));
//		System.out.println(getBezierPoint(new Point(100, 100), new Point(110, 110), 3, 20));
//		System.out.println(getBezierPoint(new Point(100, 100), new Point(90, 90), 3, 20));
//		System.out.println(getBezierPoint(new Point(100, 100), new Point(60, 60), 3, 20));
//		System.out.println(getBezierPoint(new Point(100, 100), new Point(40, 40), 3, 20));
//		System.out.println(getBezierPoint(new Point(100, 100), new Point(98, 98), 3, 20));
		
		Point p1 = new Point(0, 0);
		Point p2 = new Point(200, 200);
//		System.out.printf("x: %d, y: %d\n", 3, getY(p1, p2, 3));
//		System.out.printf("x: %d, y: %d\n", 300, getY(p1, p2, 300));
//		
//		p1 = new Point(10, 0);
//		p2 = new Point(210, 200);
//		System.out.printf("x: %d, y: %d\n", 3, getY(p1, p2, 3));
//		System.out.printf("x: %d, y: %d\n", 300, getY(p1, p2, 300));

		p1 = new Point(210, 0);
		p2 = new Point(10, 200);
		System.out.printf("x: %d, y: %d\n", 0, getY(p1, p2, 0));
		System.out.printf("x: %d, y: %d\n", 300, getY(p1, p2, 300));
	}
	
	public Point getBezierPoint(Point sp, Point ep, int maxCurbe, double defaultRate) {
		int sizeX = Math.abs(ep.x - sp.x);
		int sizeY = Math.abs(ep.y - sp.y);
		
		double lineLen = Math.sqrt(Math.pow(sizeX,2) + Math.pow(sizeY,2));
		
		if ( lineLen > maxCurbe<<1 ) {
			defaultRate = maxCurbe/lineLen;
		}
		
		int relativeBeizerLocX = (int)(sizeX * defaultRate);
		int relativeBeizerLocY = (int)(sizeY * defaultRate);

		int beizerLocX = sp.x>ep.x? ep.x + relativeBeizerLocX : ep.x - relativeBeizerLocX;
		int beizerLocY = sp.y>ep.y? ep.y + relativeBeizerLocY : ep.y - relativeBeizerLocY;
		
		return new Point(beizerLocX, beizerLocY);
	}
	
	@Override
	public void setLineStyle(int lineStyle) {
		if ( lineStyle > 0 ) {
			super.setLineStyle(lineStyle);
		}
	}

	@Override
	public void setModelAttributes(FDElement model_) {
		FDWire model = (FDWire)model_;
		
		setLineWidthEx(model.getLineWidth());
		setLineStyleEx(model.getLineStyle());
		setLineColorEx(model.getLineColor());
		setAlphaEx(model.getAlpha());
	}

	@Override
	public void setLineWidthEx(float lineWidth) {
		setLineWidth((int)lineWidth);
	}

	@Override
	public void setLineStyleEx(LineStyle lineStyle) {
		setLineStyle(lineStyle.getStyle());
	}

	@Override
	public void setLineColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ApplicationContext.getInstance().getColorManager().get(rgbColor);
			setForegroundColor(color);
		}
	}

	@Override
	public void setAlphaEx(Integer alpha) {
		setAlpha(alpha); // TODO repaint....
	}

	@Override
	public void setFeedbackEx(boolean feedback) {
		this.feedback = feedback;
	}

	@Override
	public boolean isFeedbackEx() {
		return feedback;
	}
}
