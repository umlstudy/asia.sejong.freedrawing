package asia.sejong.freedrawing.editor.actions.common;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Menu;

public abstract class DropDownAction extends Action implements IMenuCreator {
	
	protected Menu menu;
	
	public DropDownAction() {
		super("", AS_DROP_DOWN_MENU);
		setMenuCreator(this);
	}
	
	@Override
	public void dispose() {
		if ( menu != null ) {
			menu.dispose();
			menu = null;
		}
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}
}