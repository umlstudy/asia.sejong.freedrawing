package asia.sejong.freedrawing.util;

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
}
