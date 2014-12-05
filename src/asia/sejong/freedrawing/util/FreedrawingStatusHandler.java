package asia.sejong.freedrawing.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.WorkbenchErrorHandlerProxy;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

@SuppressWarnings("restriction")
public class FreedrawingStatusHandler extends AbstractStatusHandler {

	WorkbenchErrorHandlerProxy proxy = new WorkbenchErrorHandlerProxy();
	
	public FreedrawingStatusHandler() {
	}

	@Override
	public void handle(StatusAdapter statusAdapter, int style) {
		System.out.println("-----------------------------------");
		proxy.handle(statusAdapter, style);
		
		Display display = Display.getDefault();
		if ( display != null ) {
			IStatus status = statusAdapter.getStatus();
			ErrorDialog.openError(display.getActiveShell(), "error", "error occured", status);
		}
	}
	
	public boolean supportsNotification(int type) {
		return proxy.supportsNotification(type);
	}
}
