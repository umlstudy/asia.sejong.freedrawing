package asia.sejong.freedrawing.editor.actions.contributions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import asia.sejong.freedrawing.context.ApplicationContext;

public class ColorDisplayItem extends ControlContribution {

	private Label label;
	
	public ColorDisplayItem(String id) {
		super(id);
	}
	
	public void setRgb(RGB rgb) {
		Color color = ApplicationContext.getInstance().getColorManager().get(rgb);
		this.label.setBackground(color);
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite background = new Composite(parent, SWT.CENTER);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 5;
		background.setLayout(fillLayout);
		
		label = new Label(background,  SWT.CENTER);
		label.setAlignment(SWT.CENTER);
		label.setBackground(ColorConstants.blue);
		
		return background;
	}

}
