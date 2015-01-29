package asia.sejong.freedrawing.startup;

import java.io.PrintStream;

import org.eclipse.ui.IStartup;

public class FreedrawingStartup implements IStartup {

	@Override
	public void earlyStartup() {
		System.out.println("EAREY STARTUP--------------");
		System.out.println("1.표준출력 출력 내용 변경");
		
		PrintStream stream = new PrintStream(System.out) {
			public void println(String msg) {
				StackTraceElement element = Thread.currentThread().getStackTrace()[2];
				super.println(String.format("%s : ( %s: %d)", msg, element.getClassName(), element.getLineNumber()));
			}
		};
		System.setOut(stream);
		//PlatformUI.getWorkbench().addWindowListener(null);
		// 
	}
}
