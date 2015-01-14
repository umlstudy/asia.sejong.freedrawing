package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class GeometryUtil {

    public static Point centerPoint(Rectangle rect) {
        return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
    }

	public static double calculateHipotenuse(int width, int height) {
		return Math.sqrt(width * width + height * height);
	}

	public static Rectangle createSquare(Point cp, int aSide) {
		return new Rectangle(cp.x-(aSide/2), cp.y-(aSide/2), aSide, aSide);
	}

	public static double calculateDegree(int centerX, int centerY, int distenceX, int distenceY) {
		double rslt = 180f * Math.atan2(centerX-distenceX, centerY-distenceY)/Math.PI * -1f; 
		if (rslt < 0 ) {
			rslt = 360f+rslt;
		}
		return rslt;
	}

	public static Rectangle createRectangleCenterIsZero(Rectangle bounds) {
		return new Rectangle(-bounds.width/2, -bounds.height/2, bounds.width, bounds.height);
	}
}
