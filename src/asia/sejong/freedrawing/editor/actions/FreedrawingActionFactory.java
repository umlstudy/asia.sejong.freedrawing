package asia.sejong.freedrawing.editor.actions;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

import asia.sejong.freedrawing.editor.actions.clickable.AbstractClickableAction;
import asia.sejong.freedrawing.editor.actions.clickable.ColorPickAction;
import asia.sejong.freedrawing.editor.actions.clickable.FontPickAction;
import asia.sejong.freedrawing.editor.actions.selection.GroupMemberAction;
import asia.sejong.freedrawing.editor.actions.selection.SelectConnectionAction;
import asia.sejong.freedrawing.editor.actions.selection.SelectMarqueeAction;
import asia.sejong.freedrawing.editor.actions.selection.SelectPanningAction;
import asia.sejong.freedrawing.editor.actions.selection.SelectRectangleAction;
import asia.sejong.freedrawing.editor.actions.selection.SelectableActionGroup;
import asia.sejong.freedrawing.resources.ContextManager;

public abstract class FreedrawingActionFactory extends ActionFactory {

	// 팩토리메소드#1
	public static final FreedrawingActionFactory EDIT_TEXT = new FreedrawingActionFactory(RENAME.getId()) {
		public SelectionAction create(IEditorPart part) {
			
			DirectEditAction action = new DirectEditAction(part);
			action.setId(getId());
			action.setText("텍스트 편집");
			
			return action;
		}
	};
	
	// 팩토리메소드#2
	public static final FreedrawingActionFactory SELECT_PANNING = new FreedrawingActionFactory("SELECT_PANNING") {
		public GroupMemberAction create(SelectableActionGroup actionGroup) {
			SelectPanningAction action = new SelectPanningAction();
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getSelectImageDescriptor());
			action.setText("선택");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup);
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory SELECT_RECTANGLE = new FreedrawingActionFactory("SELECT_RECTANGLE") {
		public GroupMemberAction create(SelectableActionGroup actionGroup) {
			SelectRectangleAction action = new SelectRectangleAction();
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getRectangleImageDescriptor());
			action.setText("사각형");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup);
			
			return action;
		}
	};

	public static final FreedrawingActionFactory SELECT_MARQUEE = new FreedrawingActionFactory("SELECT_MARQUEE") {
		public GroupMemberAction create(SelectableActionGroup actionGroup) {
			SelectMarqueeAction action = new SelectMarqueeAction();
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getMarqueeImageDescriptor());
			action.setText("마큐");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup);
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory SELECT_CONNECTION = new FreedrawingActionFactory("SELECT_CONNECTION") {
		public GroupMemberAction create(SelectableActionGroup actionGroup) {
			SelectConnectionAction action = new SelectConnectionAction();
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getConnectionImageDescriptor());
			action.setText("연결");
			action.setEditDomain(actionGroup.getEditDomain());
			action.setActionGroup(actionGroup);
			
			return action;
		}
	};
	
	// 팩토리메소드#3
	public static final FreedrawingActionFactory COLOR_PICK = new FreedrawingActionFactory("COLOR_PICK") {
		public AbstractClickableAction create(IWorkbenchPart part, EditDomain editDomain) {
			
			ColorPickAction action = new ColorPickAction(part);
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getColorPickImageDescriptor());
			action.setText("칼라선택");
			action.setEditDomain(editDomain);
			
			return action;
		}
	};
	
	public static final FreedrawingActionFactory FONT_PICK = new FreedrawingActionFactory("FONT_PICK") {
		public AbstractClickableAction create(IWorkbenchPart part, EditDomain editDomain) {
			
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
	public GroupMemberAction create(SelectableActionGroup actionGroup) {
		throw new RuntimeException();
	}
	
	/**
	 * 팩토리메소드#3
	 * 
	 * @param part
	 * @param editDomain
	 * @return
	 */
	public AbstractClickableAction create(IWorkbenchPart part, EditDomain editDomain) {
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
