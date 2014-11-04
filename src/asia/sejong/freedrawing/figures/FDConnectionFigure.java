package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class FDConnectionFigure extends PolylineConnection {
	
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
	}
	
	protected void outlineShape(Graphics g) {
		Display display = Display.getCurrent();
		Path path = new Path(display);
		PointList pointList = getPoints();
		Point firstPoint = pointList.getFirstPoint();
		path.moveTo(firstPoint.x, firstPoint.y);
		for ( int i=1;i<pointList.size(); i++ ) {
			Point currPoint = pointList.getPoint(i);
//			Point beforePoint = pointList.getPoint(i-1);
			path.lineTo(currPoint.x, currPoint.y);
//			path.cubicTo(beforePoint.x, beforePoint.y, currPoint.x, currPoint.y, 0.1f, 0.1f);
		}
		g.setAntialias(SWT.ON);
		g.setForegroundColor(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		g.setLineWidth(2);
		g.setAlpha(180);
		g.setInterpolation(SWT.HIGH);
		g.drawPath(path);
	}
}
