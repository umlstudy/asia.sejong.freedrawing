package asia.sejong.freedrawing.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class UIUtil {

	public static final IEditorPart getActiveEditor() {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if ( workbenchWindow != null ) {
			IWorkbenchPage page = workbenchWindow.getActivePage();
			if ( page != null ) {
				return page.getActiveEditor();
			}
		}
		return null;
	}

	public static Point getLocation(Control ctrl) {
		Point location = new Point(0, 0);
		while ( ctrl != null ) {
			Rectangle bounds = ctrl.getBounds();
			location.x += bounds.x;
			location.y += bounds.y;
			ctrl = ctrl.getParent();
		}
		
		return location;
	}
}
