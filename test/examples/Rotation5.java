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

public class Rotation5 {
	static float degree =10f;
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
				Rectangle rect = new Rectangle(110, 100, 200, 200);

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

	private static Point getPoint(float x, float y, float m11, float m12, float m21, float m22) {
		return new Point(Math.round(m11*x+m12*y), Math.round(m21*x+m22*y));
	}
}