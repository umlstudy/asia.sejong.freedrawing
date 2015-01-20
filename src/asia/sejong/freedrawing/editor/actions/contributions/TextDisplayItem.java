package asia.sejong.freedrawing.editor.actions.contributions;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class TextDisplayItem extends ControlContribution {

	private Text text;
	
	public TextDisplayItem(String id) {
		super(id);
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite background = new Composite(parent, SWT.CENTER);
//		RowLayout layout = new RowLayout();
//	    layout.center = true;
//		background.setLayout(layout);
		
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
//		fillLayout.marginHeight = 5;
		background.setLayout(fillLayout);
		
		text = new Text(background,  SWT.BORDER);
		text.setEditable(false);
		
		//text.setLayoutData(RowDataFactory.swtDefaults().hint(-1, -1).create());
		return background;
	}

}
