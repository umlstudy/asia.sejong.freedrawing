package asia.sejong.freedrawing.editor.actions;

import asia.sejong.freedrawing.editor.actions.palette.PaletteDropDownAction;
import asia.sejong.freedrawing.resources.ContextManager;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public abstract class PaletteDropDownActionFactory extends LocalActionFactory {

	public static final PaletteDropDownActionFactory PALETTE_DROP_DOWN = new PaletteDropDownActionFactory("PALETTE_DROP_DOWN") {
		public PaletteDropDownAction create() {
			PaletteDropDownAction action = new PaletteDropDownAction();
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getRectangleImageDescriptor(IconType.NORMAL));
			
			return action;
		}
	};
	
	/**
	 * 생성자
	 * @param actionId
	 */
	protected PaletteDropDownActionFactory(String actionId) {
		super(actionId);
	}
	
	public PaletteDropDownAction create() {
		throw new RuntimeException();
	}
}
