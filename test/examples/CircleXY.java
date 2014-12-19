package examples;

import org.eclipse.draw2d.geometry.Point;

public class CircleXY {

	public static void main(String[] args ) {

		// 반지름 r, 원의 중심 (x1, y1)일 경우
		// CDC::Ellipse(50,100,150,200)일 경우
		// r = 50.
		// x1 = 100.   y1 = 150. 이 되겠져...
		double r  = 30;
		double x1 = 100;
		double y1 = 100;

		//testPrint(r, x1, y1);
		
		int ex = 100, ey = 100;
		System.out.printf("원의 중점 : %d, %d\n", 0,0);
		System.out.printf("주어진 원둘레 좌표 하나 : %d, %d\n", ex,ey);
		
		
		double rad = getRadius(0, 0, ex, ey);
		double baseX = rad;
		double baseY=0.0;
		
		Point p = getTranslate(rad, 30f, baseX, baseY);
		System.out.println(p);
	}
	
	public static double getRadius(int sx, int sy, int ex, int ey) {
		int width = ex - sx;
		int height = ey - sy;
		
		return Math.sqrt(width*width + height * height);
	}
	
	private static Point getTranslate(double r, double angle, double targetX, double targetY) {
		double pi = 3.1415926535;
		double fRadian = pi / 180. * angle;
		double newX = r * Math.cos(fRadian);
		double newY = r * Math.sin(fRadian);
		Point p = new Point();
		p.x = (int) Math.round(targetX - newX);
		p.y = (int) Math.round(targetY - newY);
		return p;
	}

	public static void testPrint(double r, double x1, double y1, double oriX, double oriY) {
		double fRadian;
		double x, y;
		Point ptCircle[] = new Point[360];
		double pi = 3.1415926535;
		for (int i = 0 ; i < 360 ; i++) {
		   fRadian = pi / 180. * (double)i;
		   x = r * Math.cos(fRadian) + x1;
		   y = r * Math.sin(fRadian) + y1;
		   Point p = new Point();
		   p.x = (int)Math.round(oriX-x);
		   p.y = (int)Math.round(oriY-y);
		   
		   ptCircle[i]= p;
		}
		
		for ( int idx=0; idx<ptCircle.length; idx++ ) {
			System.out.printf("%d도? x : %d, y : %d\n", idx, ptCircle[idx].x, ptCircle[idx].y);
		}
	}
	
//	private static double getBaseX(double hAngle, double rad, double xOnhAngle) {
//		return xOnhAngle-(rad*Math.cos(hAngle));
//	}
//
//	public static double getHAngle(int sx, int sy, int ex, int ey) {
//		double width = ex - sx;
//		double height = ey - sy;
//		
//		return 90f*(height/(width+height));
//	}
//	
//	public static double getRadius(int sx, int sy, int ex, int ey) {
//		int width = ex - sx;
//		int height = ey - sy;
//		
//		return Math.sqrt(width*width + height * height);
//	}
}
