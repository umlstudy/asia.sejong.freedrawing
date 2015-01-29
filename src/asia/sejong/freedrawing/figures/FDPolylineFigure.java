package asia.sejong.freedrawing.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDPolygon;

public class FDPolylineFigure extends FDTextShapeFigureImpl {

	List<Point> points;

	FDPolylineFigure(List<Point> points) {
		this.points = new ArrayList<Point>(points);
		setPreferredSize(100, 100);
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
	}

	@Override
	protected void outlineShape(Graphics graphics) {
//		graphics.pushState();
//		graphics.translate(getLocation());
		for (int i=0;i<points.size();i++) {
			Point p = points.get(i);
			// TODO 
			//System.out.println(String.format("idx:%d - x:%d,y:%d\n",i, p.x,p.y));
		}
		int[] intPoints = getPoints();
		int sx=intPoints[0];
		int sy=intPoints[1];

		int oriLineWidth = graphics.getLineWidth();

		graphics.setForegroundColor(ColorConstants.blue);
		graphics.setBackgroundColor(ColorConstants.darkGray);
		graphics.setLineWidth(10);
		graphics.fillOval(sx-5, sy-5, 10, 10);
		
		graphics.setLineWidth(oriLineWidth);
		if ( intPoints.length > 2 ) {
			for ( int i=2;i<intPoints.length; i+=2) {
				int ex=intPoints[i];
				int ey=intPoints[i+1];
				graphics.drawLine(sx, sy, ex, ey);
				sx = ex;
				sy = ey;
			}
		}
//		graphics.popState();
//		graphics.drawLine(-10,-19, 100,100);
		
		
//		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
//		int inset1 = (int) Math.floor(lineInset);
//		int inset2 = (int) Math.ceil(lineInset);
//
//		Rectangle r = Rectangle.SINGLETON.setBounds(getBoundsInZeroPoint());
//		r.x += inset1;
//		r.y += inset1;
//		r.width -= inset1 + inset2;
//		r.height -= inset1 + inset2;
//
//		graphics.drawRectangle(r);
	}

	private int[] getPoints() {
		Rectangle bounds = getBounds();
		PointList pointList = new PointList();
		for ( Point point : points ) {
			pointList.addPoint(point.x-bounds.x-(bounds.width>>1) , point.y-bounds.y-(bounds.height>>1));
		}
		return pointList.toIntArray();
	}
	
	@Override
	public Rectangle getBounds() {
		PointList pointList = new PointList();
		for ( Point point : points ) {
			pointList.addPoint(point);
		}
		return pointList.getBounds();
	}
	
	public void setPointsEx(List<Point> points) {
		this.points.clear();
		this.points.addAll(points);
	}
	
	@Override
	public void setModelAttributes(FDElement model_) {
		super.setModelAttributes(model_);
		
		FDPolygon model = (FDPolygon)model_;
		
		setPointsEx(model.getPoints());
	}
}
