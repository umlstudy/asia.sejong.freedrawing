package asia.sejong.freedrawing.util;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.WorkbenchErrorHandlerProxy;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

@SuppressWarnings("restriction")
public class FreedrawingStatusHandler extends AbstractStatusHandler {

	WorkbenchErrorHandlerProxy proxy = new WorkbenchErrorHandlerProxy();
	
	public FreedrawingStatusHandler() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void handle(StatusAdapter statusAdapter, int style) {
		//DEBUG TODO
		//ILog log = Platform.getPlugin(WorkbenchPlugin.PI_WORKBENCH).getLog();
		//Status status1 = new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, "" +Platform.getLogFileLocation());
		//log.log(status1);
		System.out.println("log loc : " + Platform.getLogFileLocation());
		System.out.println("-----------------------------------");
		proxy.handle(statusAdapter, style);
		
		final Display display = Display.getDefault();
		if ( display != null ) {
			final IStatus status = statusAdapter.getStatus();
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					ErrorDialog.openError(display.getActiveShell(), "error", "error occured", status);
				}
			});
		}
	}
	
	public boolean supportsNotification(int type) {
		return proxy.supportsNotification(type);
	}
}
