package examples;

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * draw a reflection of an image
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ReflImage3 {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		shell.setLayout(new FillLayout ());
		final Image image = display.getSystemImage (SWT.ICON_QUESTION);
		shell.addListener (SWT.Paint, new Listener () {
			@Override
			public void handleEvent (Event e) {
				Rectangle rect = image.getBounds ();
				GC gc = e.gc;
				int x = 150, y = 10;
				//gc.drawImage (image, x, y);
				Transform tr = new Transform (e.display);
				//tr.setElements (1, 0, 0, -1, 1, 2*(y+rect.height));
				//tr.setElements (1, 0, 0, 0, 1, 0);
				tr.rotate(10f);
				tr.translate(100, 0);
				gc.setTransform (tr);
				gc.drawImage (image, x, y);
//				gc.setTransform (null);
//				Color background = gc.getBackground ();
//				Pattern p = new Pattern (e.display, x, y+rect.height, x, y+(2*rect.height), background, 0, background, 255);
//				gc.setBackgroundPattern (p);
//				gc.fillRectangle (x, y+rect.height, rect.width, rect.height);
//				p.dispose ();
				tr.dispose ();
			}
		});
		shell.setSize (600, 400);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}
	
}
