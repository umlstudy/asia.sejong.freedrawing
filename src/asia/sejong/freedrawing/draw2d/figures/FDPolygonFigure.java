package asia.sejong.freedrawing.draw2d.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDPolygon;

public class FDPolygonFigure extends FDTextShapeFigureImpl {

	private List<Point> points;
	
	FDPolygonFigure(List<Point> points) {
		this.points = new ArrayList<Point>(points);
		setPreferredSize(100, 100);
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillPolygon(getIntPoints());
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		int oriLineStyle = graphics.getLineStyle();
		graphics.drawPolygon(getIntPoints());
		
		if ( isFeedbackEx() ) {
			graphics.setLineStyle(SWT.LINE_DASH);
			
			graphics.drawRectangle(getBoundsInZeroPoint());
			
			graphics.setLineStyle(oriLineStyle);
		}
	}

	private int[] getIntPoints() {
		PointList pointList = new PointList();
		Rectangle bounds = getBounds();
		for ( Point point : points ) {
			pointList.addPoint(point.x-(bounds.width>>1) , point.y-(bounds.height>>1));
		}
		return pointList.toIntArray();
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
	}
	
	public void setPointsEx(List<Point> pointOrigins) {
		this.points.clear();
		Rectangle bounds = getBounds();
		for ( Point elem : pointOrigins ) {
			points.add(new Point(elem.x-bounds.x, elem.y-bounds.y));
		}
	}
	
	@Override
	public void setModelAttributes(FDElement model_) {
		super.setModelAttributes(model_);
		
		FDPolygon model = (FDPolygon)model_;
		
		setPointsEx(model.getPoints());
		setBounds(model.getBounds());
	}
}
