package examples;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Rotation1 {
	static int angle = 40;
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

		final Rectangle rect = image.getBounds();
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
//				int x = 30, y = 30;
//				gc.drawImage(image, x, y);
//				x += rect.width + 30;
				
				gc.setForeground(ColorConstants.green);
				gc.setBackground(ColorConstants.lightGreen);
				gc.fillRectangle(50, 50, 200, 200);
				gc.drawImage(image, 50, 50);

				Transform transform = new Transform(display);

				// Rotate by 45 degrees
				// float cos45 = (float)Math.cos(45);
				float cos45 = (float) Math.cos(Math.PI / angle);

				// float sin45 = (float)Math.sin(45);
				float sin45 = (float) Math.sin(Math.PI / angle);

//				transform.setElements(cos45, sin45, -sin45, cos45, 140, 30);
				
				Point centerOriginal = new Point(150,150);
				Point centerAngled = getPoint(150,150, cos45, sin45, -sin45, cos45);
				int dx = centerOriginal.x - centerAngled.x;
				int dy = centerOriginal.y - centerAngled.y;
				
				transform.setElements(cos45, sin45, -sin45, cos45, -dx, -dy);
				//transform.setElements(cos45, sin45, -sin45, cos45, 0, 0);
				
				
				gc.setTransform(transform);
				gc.setForeground(ColorConstants.green);
				gc.setBackground(ColorConstants.lightGreen);
				gc.fillRectangle(50, 50, 200, 200);
				gc.drawImage(image, 50, 50);

				transform.dispose();
			}
		});
		

		shell.setSize(350, 550);
		shell.open();
		
//		for ( int a=40; a>1; a--) {
//			Display.getCurrent().syncExec(new Runnable() {
//				@Override
//				public void run() {
//					canvas.redraw();
//					System.out.println("redraw");
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
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
		return new Point(Math.round(m11*x+m12*y), Math.round(m21*x+m22*y));
	}
}