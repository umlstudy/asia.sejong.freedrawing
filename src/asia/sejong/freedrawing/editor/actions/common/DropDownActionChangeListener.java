package asia.sejong.freedrawing.editor.actions.common;

import org.eclipse.jface.action.IAction;

public interface DropDownActionChangeListener<T extends IAction> {

	void actionChanged(T action);
}
