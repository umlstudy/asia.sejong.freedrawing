package asia.sejong.freedrawing.editor.actions.contributions;


import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public abstract class DropDownToolItemContribution extends ContributionItem {

	protected DropDownToolItemContribution(String id) {
        super(id);
    }

    public final void fill(Composite parent) {
    	Assert.isTrue(false, "Can't add a control to a menu");//$NON-NLS-1$
    }

    public final void fill(Menu parent, int index) {
        Assert.isTrue(false, "Can't add a control to a menu");//$NON-NLS-1$
    }

    public final void fill(ToolBar parent, int index) {
    	ToolItem ti = new ToolItem((ToolBar)parent, SWT.DROP_DOWN, index);
		ti.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				dropDownSelected(event);
			}

		});
    }
    
    protected abstract void dropDownSelected(Event event);
}