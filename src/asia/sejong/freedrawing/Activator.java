package asia.sejong.freedrawing;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "asia.sejong.freedrawing"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		System.out.println("플러그인생성 " + this.getClass().getName());
		
		//DEBUG TODO
		ILog log = Platform.getPlugin(WorkbenchPlugin.PI_WORKBENCH).getLog();
		Status status1 = new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, "플러그인생성 ");
		log.log(status1);

	}

	StatusManager.INotificationListener notiListener = new StatusManager.INotificationListener() {
		@Override
		public void statusManagerNotified(int type, StatusAdapter[] adapters) {
			System.out.println("---------------------");
		}
	};

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("플러그인초기 " + this.getClass().getName());

		plugin = this;
		
		StatusManager.getManager().addListener(notiListener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		StatusManager.getManager().removeListener(notiListener);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
