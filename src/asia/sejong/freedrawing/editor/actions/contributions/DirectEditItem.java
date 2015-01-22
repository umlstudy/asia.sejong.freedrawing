package asia.sejong.freedrawing.editor.actions.contributions;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public abstract class DirectEditItem extends ControlContribution {

	private String oldValue;
	private Text text; 

	public DirectEditItem(IAction action) {
		super(action.getId() + "_DEI");
	}
	
	@Override
	protected Control createControl(Composite parent) {
		Composite background = new Composite(parent, SWT.CENTER);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		background.setLayout(fillLayout);
		
		text = new Text(background,  SWT.BORDER);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				changeValue(text.getText());
			}
		});
		text.setText(String.format("       "));
		
		return background;
	}
	
	public void changeValue(String newValue) {
		if ( checkValidValue(newValue) ) {
			if ( !newValue.equals(oldValue)) {
				valueChanged(newValue);
				oldValue = newValue;
				if ( !newValue.equals(text.getText()) ) {
					text.setText(newValue);
				}
			}
		}
	}
	
	protected abstract boolean checkValidValue(String value);
	protected abstract void valueChanged(String value);

}
