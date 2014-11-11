package examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Toolbar1 {

  public static void main(String[] args) {
    Display display = new Display();
    Image image = new Image(display, 16, 16);
    Color color = display.getSystemColor(SWT.COLOR_RED);
    GC gc = new GC(image);
    gc.setBackground(color);
    gc.fillRectangle(image.getBounds());
    gc.dispose();
    Shell shell = new Shell(display);
    ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.BORDER);
    for (int i = 0; i < 12; i++) {
      ToolItem item = new ToolItem(toolBar, SWT.DROP_DOWN);
      item.setImage(null);
    }
    toolBar.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    image.dispose();
    display.dispose();
  }

}