package asia.sejong.freedrawing.util;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class IOUtil {

	public static final byte[] readAll(String fileName) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(fileName);
			return IOUtils.toByteArray(fileInputStream);
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		} finally {
			close(fileInputStream);
		}
	}
	
	public static final String checksum(byte[] bytes ) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] checksum = md.digest(bytes);
			return Base64.encodeBase64String(checksum);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final void close(Closeable target) {
		if  ( target != null ) {
			try {
				target.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void throwException(Exception e) {
		if ( e instanceof RuntimeException ) {
			throw (RuntimeException)e;
		} else {
			throw new RuntimeException(e);
		}
	}
}
