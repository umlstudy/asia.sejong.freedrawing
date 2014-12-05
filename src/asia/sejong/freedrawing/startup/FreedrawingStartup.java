package asia.sejong.freedrawing.startup;

import org.eclipse.ui.IStartup;

public class FreedrawingStartup implements IStartup {

	@Override
	public void earlyStartup() {
		System.out.println("EAREY STARTUP--------------");
	}
}
