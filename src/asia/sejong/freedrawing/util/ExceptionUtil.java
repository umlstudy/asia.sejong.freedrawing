package asia.sejong.freedrawing.util;

public class ExceptionUtil {

	public static void throwRuntimeException(Exception e) {
		if ( e instanceof RuntimeException ) {
			throw (RuntimeException)e;
		} else {
			throw new RuntimeException(e);
		}
	}
}
