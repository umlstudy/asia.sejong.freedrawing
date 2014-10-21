package asia.sejong.freedrawing.util;

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

	public static String methodLog(StackTraceElement ste, String msg) {
		  String methodName = ste.getMethodName();
		  String className = ste.getClassName();
		  int lineNumber = ste.getLineNumber();
		  
		  return String.format("%s.%s[%d] %s", className, methodName, lineNumber, msg);
	}
	
	public static String getMethodName(final int depth) {
	  final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	  return ste[ste.length - 1 - depth].getMethodName();
	}
	
	public static StackTraceElement getSte() {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[ste.length -2];
	}
}
