package asia.sejong.freedrawing.editor.actions.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class DropDownAction<T extends IAction> extends Action implements IMenuCreator, DropDownActionChangeListener<T> {
	
	private Menu subMenu;
	
	private List<T> actions = new ArrayList<T>();
	private T selectedAction;
	
	public DropDownAction() {
		super("", AS_DROP_DOWN_MENU);
		setMenuCreator(this);
	}
	
	public void addAction(T action, boolean selected) {
		actions.add(action);
		
		if ( selected ) {
			actionChanged(action);
		}
	}
	
	public void run() {
		selectedAction.run();
	}

	@Override
	public Menu getMenu(Control parent) {
		dispose();
		if ( subMenu == null ) {
			subMenu = new Menu(parent);
			for ( T action : actions ) {
				addActionToMenu(subMenu, action);
			}
		}
		return subMenu;
//			final ImageDescriptor createFromImage = ImageDescriptor.createFromImage(JFaceResources.getImageRegistry().get("LOCKED_JOB"));
//
//			
//			Action a = new Action("AA", AS_CHECK_BOX) {
//				
//				public void run() {
//					System.out.println("AAAAAAAAAAAAAA");
//					ActionSwitchableAction.this.setImageDescriptor(createFromImage);
////					firePropertyChange(CHECKED, Boolean.FALSE, Boolean.TRUE);
////					firePropertyChange(CHECKED, Boolean.FALSE, Boolean.TRUE);
//					ActionSwitchableAction.this.setChecked(!ActionSwitchableAction.this.isChecked());
//					ActionSwitchableAction.this.setChecked(true);
//					
////					ImageDescriptor fontPickImageDescriptor = ContextManager.getInstance().getImageManager().getFontPickImageDescriptor();
////					
////					createFromImage.getImageData().
////					OverlayIcon decorationOverlayIcon = new OverlayIcon(createFromImage, fontPickImageDescriptor, new Point(24,24)); 
////					decorationOverlayIcon.createImage();
////					FigureToggleAction.this.setImageDescriptor(ImageDescriptor.createFromImage(decorationOverlayIcon.createImage()));
//					//OverlayIcon resultIcon = new OverlayIcon(fontPickImageDescriptor, createFromImage, new Point(16, 16));   
//				    //swt.graphics.Image icon = resultIcon.createImage();
////					if (!FigureToggleAction.this.isChecked()) {
////						firePropertyChange(CHECKED, Boolean.TRUE, Boolean.FALSE);
////					} else {
////						firePropertyChange(CHECKED, Boolean.FALSE, Boolean.TRUE);
////					}
//				}
//			};
//			a.setImageDescriptor(createFromImage);
//			
	}
	
	protected void addActionToMenu(Menu parent, T action) {
		ActionContributionItem item= new ActionContributionItem(action);
		item.fill(parent, -1);
	}
	
	protected T getSelectedAction() {
		return selectedAction;
	}
	
	@Override
	public void dispose() {
		if ( subMenu != null ) {
			subMenu.dispose();
			subMenu = null;
		}
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}

	@Override
	public void actionChanged(T action) {
		this.selectedAction = action;
		setImageDescriptor(action.getImageDescriptor());
		setText(action.getText());
	}
}