package asia.sejong.freedrawing.editor.actions;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

import asia.sejong.freedrawing.editor.actions.selection.AbstractSelectionAction;
import asia.sejong.freedrawing.editor.actions.selection.ColorPickAction;
import asia.sejong.freedrawing.editor.actions.selection.FontPickAction;
import asia.sejong.freedrawing.editor.actions.selection.MoveLeftAction;
import asia.sejong.freedrawing.editor.actions.toggle.PaletteToggleAction;
import asia.sejong.freedrawing.editor.actions.toggle.ToggleActionGroup;
import asia.sejong.freedrawing.resources.ContextManager;

public abstract class FreedrawingActionFactory extends ActionFactory {

	// 팩토리메소드 타입 #1
	public static final FreedrawingActionFactory EDIT_TEXT = new FreedrawingActionFactory(RENAME.getId()) {
		public SelectionAction create(IEditorPart part) {
			
			DirectEditAction action = new DirectEditAction(part);
			action.setId(getId());
			action.setText("텍스트 편집");
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory MOVE_LEFT = new FreedrawingActionFactory("MOVE_LEFT") {
		public SelectionAction create(IEditorPart part) {
			
			MoveLeftAction action = new MoveLeftAction(part, false);
			action.setId(getId());
			action.setText("왼쪽으로");
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory MOVE_LEFT_PRESSED_ = new FreedrawingActionFactory("MOVE_LEFT_PRESSED") {
		public SelectionAction create(IEditorPart part) {
			
			MoveLeftAction action = new MoveLeftAction(part, true);
			action.setId(getId());
			action.setText("왼쪽으로");
			
			return action;
		}
	};
	
	// 팩토리메소드 타입 #2
	public static final FreedrawingActionFactory TOGGLE_PANNING = new FreedrawingActionFactory("TOGGLE_PANNING") {
		public PaletteToggleAction create(ToggleActionGroup actionGroup, AbstractTool tool) {
			PaletteToggleAction action = new PaletteToggleAction();
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getSelectImageDescriptor());
			action.setText("선택");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup, true);
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory TOGGLE_RECTANGLE = new FreedrawingActionFactory("TOGGLE_RECTANGLE") {
		public PaletteToggleAction create(ToggleActionGroup actionGroup, AbstractTool tool) {
			PaletteToggleAction action = new PaletteToggleAction();
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getRectangleImageDescriptor());
			action.setText("사각형");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup, false);
			
			return action;
		}
	};

	public static final FreedrawingActionFactory TOGGLE_MARQUEE = new FreedrawingActionFactory("TOGGLE_MARQUEE") {
		public PaletteToggleAction create(ToggleActionGroup actionGroup, AbstractTool tool) {
			PaletteToggleAction action = new PaletteToggleAction();
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getMarqueeImageDescriptor());
			action.setText("마큐");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup, false);
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory TOGGLE_CONNECTION = new FreedrawingActionFactory("TOGGLE_CONNECTION") {
		public PaletteToggleAction create(ToggleActionGroup actionGroup, AbstractTool tool) {
			PaletteToggleAction action = new PaletteToggleAction();
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getConnectionImageDescriptor());
			action.setText("연결");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup, false);
			
			return action;
		}
	};
	
	// 팩토리메소드 타입 #3
	public static final FreedrawingActionFactory COLOR_PICK = new FreedrawingActionFactory("COLOR_PICK") {
		public AbstractSelectionAction create(IWorkbenchPart part, EditDomain editDomain) {
			
			ColorPickAction action = new ColorPickAction(part);
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getColorPickImageDescriptor());
			action.setText("칼라선택");
			action.setEditDomain(editDomain);
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory FONT_PICK = new FreedrawingActionFactory("FONT_PICK") {
		public AbstractSelectionAction create(IWorkbenchPart part, EditDomain editDomain) {
			
			FontPickAction action = new FontPickAction(part);
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getFontPickImageDescriptor());
			action.setText("폰트선택");
			action.setEditDomain(editDomain);
			
			return action;
		}
	};

	/**
	 * 생성자
	 * @param actionId
	 */
	protected FreedrawingActionFactory(String actionId) {
		super(actionId);
	}
	
	/**
	 * 팩토리메소드#1
	 * @param part
	 * @return
	 */
	public SelectionAction create(IEditorPart part) {
		throw new RuntimeException();
	}
	
	/**
	 * 팩토리메소드#2
	 * @param actionGroup
	 * @return
	 */
	public PaletteToggleAction create(ToggleActionGroup actionGroup, AbstractTool tool) {
		throw new RuntimeException();
	}
	
	/**
	 * 팩토리메소드#3
	 * 
	 * @param part
	 * @param editDomain
	 * @return
	 */
	public AbstractSelectionAction create(IWorkbenchPart part, EditDomain editDomain) {
		throw new RuntimeException();
	}
	
	/**
	 * 팩토리메소드#4 
	 * @see org.eclipse.ui.actions.ActionFactory#create(org.eclipse.ui.IWorkbenchWindow)
	 */
	@Override
	public IWorkbenchAction create(IWorkbenchWindow window) {
		throw new RuntimeException();
	}
}
