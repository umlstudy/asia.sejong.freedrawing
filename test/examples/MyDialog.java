package examples;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MyDialog extends Dialog {

	protected MyDialog(Shell parentShell) {
		super(parentShell);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("test");
		newShell.setSize(200, 100);
	}

	protected void createButtonsForButtonBar(Composite parent) {
	}

	protected Control createDialogArea(Composite parent) {
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(new FillLayout());

		ScrolledComposite sc = new ScrolledComposite(content, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);

		Composite composite = new Composite(sc, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));

		new Label(composite, SWT.NONE).setText("1111");
		new Label(composite, SWT.NONE).setText("2222");
		new Label(composite, SWT.NONE).setText("3333");
		new Label(composite, SWT.NONE).setText("4444");
		new Label(composite, SWT.NONE).setText("5555");
		new Label(composite, SWT.NONE).setText("6666");
		new Label(composite, SWT.NONE).setText("7777");

		sc.setContent(composite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return parent;
	}
	
	public static void main(String...args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Eclipse jface PopupDialog  Demo");
		shell.setLayout(new GridLayout());
		shell.setSize(400, 300);
		
		MyDialog  dialog = new MyDialog(shell);
		dialog.open();
		
		shell.layout();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
}