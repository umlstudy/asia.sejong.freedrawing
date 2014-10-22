package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class FDConnectionFigure extends PolylineConnection {

	public static final Image CONNECTION_IMAGE = new Image(Display.getCurrent(), FDConnectionFigure.class.getResourceAsStream("connection.png"));
	
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
	}
}
