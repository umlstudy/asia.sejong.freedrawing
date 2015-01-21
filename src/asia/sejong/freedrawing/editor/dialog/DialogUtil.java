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

import asia.sejong.freedrawing.model.FontInfo;

public class DialogUtil {

	public static RGB openColorSelectDialog(Display display, Point loc) {
		try {
			Method method = DialogUtil.class.getMethod("openColorSelectDialog", Shell.class);
			return (RGB)openDialog(display, method, loc);
		} catch (Exception e) {
			if ( e instanceof RuntimeException ) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static FontData openFontSelectDialog(Display display, Point dialogLocation, FontInfo lastFontInfo) {
		try {
			Method method = DialogUtil.class.getMethod("openFontSelectDialog", Shell.class, FontInfo.class);
			return (FontData)openDialog(display, method, dialogLocation, lastFontInfo);
		} catch (Exception e) {
			if ( e instanceof RuntimeException ) {
				throw (RuntimeException)e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static Object openDialog(Display display, Method method, Point loc, Object...args) {
		final Shell shell = new Shell(display , SWT.APPLICATION_MODAL); 
	    shell.setLocation(loc);
	    shell.setSize(0,0);
	    shell.setVisible(false);
	    shell.open();
	    
	    Object[] newArgs = new Object[args.length+1];
	    newArgs[0] = shell;
	    for ( int i=0;i<args.length;i++ ) {
	    	newArgs[i+1] = args[i];
	    }
	    try {
	    	return method.invoke(null, newArgs);
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
	
	public static FontData openFontSelectDialog(Shell shell, FontInfo fontInfo) {
		FontDialog dlg = new FontDialog(shell, SWT.APPLICATION_MODAL);
		if ( fontInfo != null ) {
			dlg.setFontList(new FontData[] {fontInfo.createFontData()});
		}
		return dlg.open();
	}
}
