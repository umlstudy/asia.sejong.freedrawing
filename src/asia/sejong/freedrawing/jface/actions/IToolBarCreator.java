package asia.sejong.freedrawing.jface.actions;

import org.eclipse.swt.widgets.ToolBar;

public interface IToolBarCreator {
    public void dispose();
    public ToolBar getToolBar(ToolBar parent);
}
