package examples;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Rotation6 {
	static float degree =0f;
	public static void main(String[] args) {
		final Display display = new Display();

		final Image image = new Image(display, 110, 60);
		GC gc = new GC(image);
		Font font = new Font(display, "Times", 30, SWT.BOLD);
		gc.setFont(font);
		gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, 110, 60);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawText("SWT", 10, 10, true);
		font.dispose();
		gc.dispose();

		Shell shell = new Shell(display);
		shell.setText("Matrix Tranformations");
		shell.setLayout(new FillLayout());

		final Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.setAdvanced(true);
				if (!gc.getAdvanced()) {
					gc.drawText("Advanced graphics not supported", 30, 30, true);
					return;
				}

				// Original image
				Rectangle rect = new Rectangle(200, 100, 200, 200);

				gc.setForeground(ColorConstants.green);
				gc.setBackground(ColorConstants.gray);
				gc.fillRectangle(rect);

				Graphics g = new SWTGraphics(gc);
				
				double r = degree * 3.141592 / 180.0;
			    float cos45 = (float)Math.cos(r);
			    float sin45 = (float)Math.sin(r);
				Point ocp = new Point(rect.x + rect.width/2, rect.y + rect.height/2);
				Point centerAngled = getPoint(ocp.x,ocp.y, cos45, sin45, -sin45, cos45);
				int dy = ocp.x - centerAngled.x;
				int dx = ocp.y - centerAngled.y;
				System.out.printf("ocp.x ? %d, ocp.y %d\n", ocp.x, ocp.y);
				
				g.translate(dx, dy);
				g.rotate(degree);
				
				g.setForegroundColor(ColorConstants.green);
				g.setBackgroundColor(ColorConstants.lightGreen);
				g.fillRectangle(rect.x,  rect.y, rect.width, rect.height);
				g.drawImage(image, rect.x, rect.y);
				g.setBackgroundColor(ColorConstants.red);
				g.fillArc(rect.x+100, rect.y+150, 10, 10, 0, 360);
				System.out.println("degree " + degree);
				//g.translate(moved.x, moved.y);
			}
		});
		

		shell.setSize(350, 550);
		shell.open();
		
		canvas.addListener(SWT.MouseUp, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				degree = 0;
				for ( int a=72; a>11; a--) {
					Display.getCurrent().asyncExec(new Runnable() {
						@Override
						public void run() {
							degree +=5;
							canvas.redraw();
							System.out.println("redraw");
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
				}
				
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
	}
	
	public static void main2(String...args) {

		for (int angle=0;angle<360;angle+=45 ) {
			double r = (double)angle * 3.141592 / 180.0;
				float cos45 = (float)Math.cos(r);    /* cosine 값  */
				float sin45 = (float)Math.sin(r);     /* sine 값  */
				
				Point originalCenterPoint = new Point(150,150);
				Point centerAngled = getPoint(150,150, cos45, sin45, -sin45, cos45);
//				int dx = originalCenterPoint.x - centerAngled.x;
//				int dy = originalCenterPoint.y - centerAngled.y;
				int dx = centerAngled.x;
				int dy = centerAngled.y;
				System.out.printf("angle : %d, dx : %d, dy : %d\n", angle, dx, dy);
		}
	}

	protected static Point getDiff(int width, int height, float m11, float m12, float m21, float m22) {
		Point[] points = new Point[4];
		points[0] = getPoint(0, 0, m11, m12, m21, m22);
		points[1] = getPoint(width, 0, m11, m12, m21, m22);
		points[2] = getPoint(0, height, m11, m12, m21, m22);
		points[3] = getPoint(width, height, m11, m12, m21, m22);
		
		int minX, maxX, minY, maxY;
		maxX = maxY = Integer.MIN_VALUE;
		minX = minY = Integer.MAX_VALUE;
		
		for ( Point p : points ) {
			if ( maxX < p.x ) {
				maxX=p.x;
			}
			if ( minX > p.x ) {
				minX = p.x;
			}
			if ( maxY < p.y ) {
				maxY=p.y;
			}
			if ( minY > p.y ) {
				minY = p.y;
			}
		}
		
		int newWidth = maxX-minX;
		int newHeight = maxY - minY;
		
		return new Point(newWidth>>1, newHeight>>1);
	}

	private static Point getPoint(float x, float y, float m11, float m12, float m21, float m22) {
		//return new Point(Math.round(m11*x+m12*y), Math.round(m21*x+m22*y));
		//return new Point(Math.round(m21*x+m22*y), Math.round(m11*x+m12*y));
		return new Point(Math.round(m11*x+m12*y), Math.round(m21*x+m22*y));
	}
	
	
	
//	void Rotation(float deg, ImageData ori)
//	{
//	     int i, j, buf1, buf2;
//	     double x, y, p, q;
//	     int xs=ori.width/2;
//	     int ys=ori.height/2;
//	     double r;
//	     double c, s;
//	     int data;
//
//	     r = deg * 3.141592/180.0;   /* radian 값  */
//	     c = Math.cos(r);    /* cosine 값  */
//	     s = Math.sin(r);     /* sine 값  */
//
//	     for (i=-ys; i<ys; i++) {
//	           for (j=-xs; j<xs; j++) {
//	                 y=j*s + i*c;              /*  cos(r)     -sin(r) */    //행렬
//	                 x=j*c - i*s;              /*  sin(r)       cos(r) */
//
//	                 if (y>0) buf1 = (int)y;        /* 양수인 경우 */
//	                 else     buf1 = (int)-y;  /* 음수인 경우 */
//	                 if (x>0) buf2 = x;
//	                 else     buf2 = x - 1;
//
//	                 q = y - buf1;
//	                 p = x - buf2;
//
//	                /* -SIZE/2<buf1<SIZE/2, -SIZE/2<buf2<SIZE/2의 경우 */
//	                 if ((buf1 > -ys) && (buf1 < ys) && (buf2 > -xs) && (buf2 < xs))
//	                      data = RetrunData(p, q, buf1, buf2, xs, ys);
//	                 else
//	                      data = 0;   /* 범위에 들어가지 않는 경우 */
//
//	                 if (data < 0)   data = 0;   /* data가 0 이하 255 이상인 경우  */
//	                            if (data >255)   data=255;
//	                 image_out[i+ys][j+xs]=data; 
//	                                                    /* 그외의 경우는 data를 mapping 한 위치에 저장한다. */
//	          }
//	     }
//	}
}