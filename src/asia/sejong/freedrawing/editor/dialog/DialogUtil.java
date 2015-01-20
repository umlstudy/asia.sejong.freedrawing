package asia.sejong.freedrawing.editor.dialog;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Shell;

public class DialogUtil {

	public static RGB openColorSelectDialog(Display display, Point loc) {
		try {
			Method method = DialogUtil.class.getMethod("openColorSelectDialog", Shell.class);
			return (RGB)openDialog(display, loc, method);
		} catch (Exception e) {
			if ( e instanceof RuntimeException ) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static FontData openFontSelectDialog(Display display, Point loc) {
		try {
			Method method = DialogUtil.class.getMethod("openFontSelectDialog", Shell.class);
			return (FontData)openDialog(display, loc, method);
		} catch (Exception e) {
			if ( e instanceof RuntimeException ) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static Object openDialog(Display display, Point loc, Method method) {
		final Shell shell = new Shell(display , SWT.APPLICATION_MODAL); 
	    shell.setLocation(loc);
	    shell.setSize(0,0);
	    shell.setVisible(false);
	    shell.open();
	    
	    try {
	    	return method.invoke(null, shell);
	    } catch ( Exception e ) {
			if ( e instanceof RuntimeException ) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
	    } finally {
	    	shell.dispose(); 
	    	while (!shell.isDisposed()) { 
	    		if (!display.readAndDispatch()) {
	    			display.sleep(); 
	    		}
	    	}
	    }
	}
	
	public static RGB openColorSelectDialog(Shell shell) {
	    ColorDialog dlg = new ColorDialog(shell, SWT.APPLICATION_MODAL);
		return  dlg.open();
	}
	
	public static FontData openFontSelectDialog(Shell shell) {
		FontDialog dlg = new FontDialog(shell, SWT.APPLICATION_MODAL);
		return dlg.open();
	}
}
