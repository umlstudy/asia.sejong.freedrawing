package asia.sejong.freedrawing.util;

import java.io.OutputStream;
import java.io.PrintStream;

public class DebugUtil {
	
	public static void printLogStart() {
		System.out.println(methodLog(getSte(), "START"));
	}

	public static void printLogEnd() {
		System.out.println(methodLog(getSte(), "START"));
	}
	
	public static String methodStartLog() {
		return methodLog(getSte(), "START");
	}

	public static String methodEndLog(StackTraceElement ste) {
		return methodLog(getSte(), "END");
	}

	private static String methodLog(StackTraceElement ste, String msg) {
		  String methodName = ste.getMethodName();
		  String className = ste.getFileName();
		  int lineNumber = ste.getLineNumber();
		  
		  return String.format("%s.%s[%d] %s", className, methodName, lineNumber, msg);
	}
	
	private static StackTraceElement getSte() {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[3];
	}
	
	public static void printStackTraceElement(final StackTraceElement[] stackTrace, final OutputStream out) {
		PrintStream ps = new PrintStream(out);
		for (StackTraceElement element : stackTrace) {
			ps.printf("%s(%d)\n", element.getClassName(), element.getLineNumber());
		}
	}
}
