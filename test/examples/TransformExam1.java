package examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class TransformExam1 {
  public static void main(String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setText("Advanced Graphics");
    FontData fd = shell.getFont().getFontData()[0];
    final Font font = new Font(display, fd.getName(), 60, SWT.BOLD
        | SWT.ITALIC);
    final Image image = new Image(display, 640, 480);
    final Rectangle rect = image.getBounds();
    GC gc = new GC(image);
    gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
    gc.fillOval(rect.x, rect.y, rect.width, rect.height);
    gc.dispose();
    shell.addListener(SWT.Paint, new Listener() {
      public void handleEvent(Event event) {
        GC gc = event.gc;
        Transform tr = new Transform(display);
        tr.translate(50, 120);
        tr.rotate(30);
        gc.drawImage(image, 0, 0, rect.width, rect.height, 0, 0,
            rect.width / 2, rect.height / 2);
        //gc.setAlpha(100);
        gc.setTransform(tr);
        Path path = new Path(display);
        path.addString("SWT", 0, 0, font);
        gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.setBackground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
        gc.fillPath(path);
        gc.drawPath(path);
        tr.dispose();
        path.dispose();
      }
    });
    shell.setSize(shell.computeSize(rect.width / 2, rect.height / 2));
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    image.dispose();
    font.dispose();
    display.dispose();
  }
}
