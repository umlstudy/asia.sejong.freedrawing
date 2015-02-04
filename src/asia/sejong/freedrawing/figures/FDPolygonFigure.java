package asia.sejong.freedrawing.figures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.mapping.ModelStatus;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDPolygon;

public class FDPolygonFigure extends FDTextShapeFigureImpl {

	List<Point> points;
//	Rectangle bounds;
	
	FDPolygonFigure(List<Point> points) {
		this.points = new ArrayList<Point>(points);
//		this.bounds = createBounds(this.points);
		setPreferredSize(100, 100);
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		graphics.setForegroundColor(ColorConstants.green);
//		graphics.pushState();
//		graphics.translate(getLocation());
//		for (int i=0;i<points.size();i++) {
//			Point p = points.get(i);
//			// TODO 
//			System.out.println(String.format("idx:%d - x:%d,y:%d\n",i, p.x,p.y));
//		}
//		System.out.println("polygon");
		graphics.drawPolygon(getIntPoints());
		//System.out.println("intarray ? " + Arrays.toString(getIntPoints()));
		System.out.println("points ? " + points);
		// TODO 디버그
		graphics.setLineStyle(SWT.LINE_DASH);
		graphics.setForegroundColor(ColorConstants.blue);
		graphics.drawRectangle(getBoundsInZeroPoint());
		System.out.println("rect ? " + getBoundsInZeroPoint());
		
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

	private int[] getIntPoints() {
		PointList pointList = new PointList();
//		System.out.println("BBB " +bounds);
//		System.out.println("PPP " +points);
		Rectangle bounds = getBounds();
		for ( Point point : points ) {
			pointList.addPoint(point.x-(bounds.width>>1) , point.y-(bounds.height>>1));
		}
//		System.out.println("---- points ? " + points);
//		System.out.println("-????- points ? " + getBounds());
		System.out.println("----" + pointList.getFirstPoint().toString() + pointList.getLastPoint() );
		return pointList.toIntArray();
	}
//	
//	@Override
//	public Rectangle getBounds() {
//		return bounds;
//	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
	}
	
	public void setPointsEx(List<Point> pointOrigins) {
		Rectangle newBounds = FDPolygon.createBounds(pointOrigins);
//		System.out.println(" oldBounds ? " + getBounds());
//		System.out.println(" pointOrigins ? " + pointOrigins);
//		System.out.println(" newBounds ? " + newBounds);
		setBounds(newBounds);
		this.points.clear();
		Rectangle bounds = getBounds();
		for ( Point elem : pointOrigins ) {
			System.out.println(String.format("elem.x %d -bounds.x %d ? ", elem.x,bounds.x) );
			points.add(new Point(elem.x-bounds.x, elem.y-bounds.y));
		}
		System.out.println(" bounds ? " + bounds);
		System.out.println(" points ? " + points);
	}
	
//	public List<Point> getPoints() {
//		return points;
//	}
	
	@Override
	public void setModelAttributes(FDElement model_) {
		super.setModelAttributes(model_);
		
		FDPolygon model = (FDPolygon)model_;
		
		model.setSize(model.getSize().width, model.getSize().height);
		setPointsEx(model.getPoints());
	}
}
