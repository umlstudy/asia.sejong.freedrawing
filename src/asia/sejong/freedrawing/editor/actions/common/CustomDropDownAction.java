package asia.sejong.freedrawing.editor.actions.common;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public abstract class CustomDropDownAction extends DropDownAction {

	@Override
	public Menu getMenu(Control parent) {
		dispose();
		if ( menu == null ) {
			menu = new Menu(parent);
			ControlContribution contribution = new ControlContribution(getId()) {

				@Override
				protected Control createControl(Composite parent) {
					return CustomDropDownAction.this.createControl(parent);
				}
			};
			
			contribution.fill(menu, -1);
		}
		return menu;
	}

	protected abstract Control createControl(Composite parent);

}
