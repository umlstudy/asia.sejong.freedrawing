package asia.sejong.freedrawing.editor.actions.contributions;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public abstract class DirectEditItem extends ControlContribution {

	private String oldValue;

	public DirectEditItem(String id) {
		super(id);
	}
	
	@Override
	protected Control createControl(Composite parent) {
		Composite background = new Composite(parent, SWT.CENTER);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		background.setLayout(fillLayout);
		
		final Text text = new Text(background,  SWT.BORDER);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if ( e.keyCode == SWT.CR ) {
					changeValue(text);
				}
			}
		});
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				changeValue(text);
			}
		});
		
		return background;
	}
	
	private void changeValue(Text text) {
		if ( checkValidValue(text.getText()) ) {
			String newValue = text.getText();
			valueChanged(newValue);
			oldValue = newValue;
		} else {
			if ( oldValue != null ) {
				text.setText(oldValue);
			}
		}
	}
	
	protected abstract boolean checkValidValue(String value);
	protected abstract void valueChanged(String value);
}
