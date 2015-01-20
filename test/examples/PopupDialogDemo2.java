package examples;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PopupDialogDemo2 {

	public PopupDialogDemo2() {

	}

	
	public static void main(String[] args) {
		open();
	}
	static void open(){
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Eclipse jface PopupDialog  Demo");
		shell.setLayout(new GridLayout());
		shell.setSize(400, 300);
	
		
		
		Button button = new Button(shell,SWT.PUSH);
		button.setText("Open PopupDialog");
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//int shellStyle = PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE;
				int shellStyle = SWT.BORDER_SOLID | SWT.SHADOW_IN ;
				boolean takeFocusOnOpen = true;
				boolean persistSize = true;
				boolean persistLocation = true;
				boolean showDialogMenu = false;
				boolean showPersistActions = true;
				String titleText = null;
				//"titleText"; 
				String infoText = null;//"infoText\r\nhelloworl\r\nsss";
				PopupDialog  dialog = new PopupDialog(shell, shellStyle, takeFocusOnOpen, persistSize, persistLocation, showDialogMenu, showPersistActions, titleText, infoText){

					@Override
					protected Control createDialogArea(Composite parent) {
						Composite composite = (Composite) super.createDialogArea(parent);
						Label label = new Label(composite, SWT.NONE);
						label.setText("Hello wwwww");
//						Text text = new Text(composite,SWT.SINGLE | SWT.BORDER);
//						text.setText("Hello World");
						return composite;
					}
					
					@Override
					protected Color getBackground() {
						return ColorConstants.green;
					}
					
					protected Point getDefaultLocation(Point initialSize) {
						System.out.println(getShell().getLocation());
						return getShell().getLocation();
					}
				};
				
				
				dialog.open();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}
		});
		
		shell.layout();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}

}