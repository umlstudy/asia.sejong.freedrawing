package asia.sejong.freedrawing.editor.actions.palette;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import asia.sejong.freedrawing.resources.IconManager.IconType;

public class PaletteDropDownAction extends Action implements IMenuCreator, PaletteActionChangeListener, PaletteIconChangable {
	
	private Menu subMenu;
	
	private List<PaletteAction> actions = new ArrayList<PaletteAction>();
	private PaletteAction selectedAction;
	
	private IconType type;
	
	public PaletteDropDownAction() {
		super("", AS_DROP_DOWN_MENU);
		setMenuCreator(this);
	}
	
	public void addAction(PaletteAction action, boolean selected) {
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
			for ( PaletteAction action : actions ) {
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
	
	protected void addActionToMenu(Menu parent, IAction action) {
		ActionContributionItem item= new ActionContributionItem(action);
		item.fill(parent, -1);
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
	public void actionChanged(PaletteAction action) {
		this.selectedAction = action;
		setImageDescriptor(action.getImageDescriptor());
		setText(action.getText());
	}

	@Override
	public void iconChange(Tool tool, IconType newType) {
		if ( selectedAction != null ) {
			if ( type != newType || tool != selectedAction.getTool() ) {
				type = newType;
				Image icon = selectedAction.getIcon(type);
				if ( icon != null ) {
					setImageDescriptor(ImageDescriptor.createFromImage(icon));
				} else {
					setImageDescriptor(null);
				}
			}
		}
	}

	@Override
	public Tool getTool() {
		if ( selectedAction != null ) {
			return selectedAction.getTool();
		}
		return null;
	}
}