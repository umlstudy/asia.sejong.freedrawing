package asia.sejong.freedrawing.editor.actions;

import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

import asia.sejong.freedrawing.editor.actions.selection.ChangeBackgroundColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeFontAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeFontColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeLineColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeRouterAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeZOrderAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeZOrderAction.ZOrderDirection;
import asia.sejong.freedrawing.editor.actions.selection.MoveAction;
import asia.sejong.freedrawing.editor.actions.selection.MoveAction.Direction;
import asia.sejong.freedrawing.resources.ContextManager;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public abstract class SelectionActionFactory extends LocalActionFactory {

	public static final SelectionActionFactory EDIT_TEXT = new SelectionActionFactory(ActionFactory.RENAME.getId()) {
		public SelectionAction create(IEditorPart part) {
			
			DirectEditAction action = new DirectEditAction(part);
			action.setId(getId());
			action.setText("텍스트 편집");
			
			return action;
		}
	};
	
	// 1Pixel Move
	public static final SelectionActionFactory MOVE_LEFT = new SelectionActionFactory("MOVE_LEFT") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.East, false);
			action.setId(getId());
			action.setText("왼쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_RIGHT = new SelectionActionFactory("MOVE_RIGHT") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.West, false);
			action.setId(getId());
			action.setText("오른쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_DOWN = new SelectionActionFactory("MOVE_DOWN") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.South, false);
			action.setId(getId());
			action.setText("아래로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_UP = new SelectionActionFactory("MOVE_UP") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.North, false);
			action.setId(getId());
			action.setText("위로");
			
			return action;
		}
	};
	
	// XFixel Move
	public static final SelectionActionFactory MOVE_LEFT_FAST = new SelectionActionFactory("MOVE_LEFT_FAST") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.East, true);
			action.setId(getId());
			action.setText("왼쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_RIGHT_FAST = new SelectionActionFactory("MOVE_RIGHT_FAST") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.West, true);
			action.setId(getId());
			action.setText("오른쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_DOWN_FAST = new SelectionActionFactory("MOVE_DOWN_FAST") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.South, true);
			action.setId(getId());
			action.setText("아래로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_UP_FAST = new SelectionActionFactory("MOVE_UP_FAST") {
		public SelectionAction create(IEditorPart part) {
			
			MoveAction action = new MoveAction(part, Direction.North, true);
			action.setId(getId());
			action.setText("위로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory ZORDER_TO_FRONT = new SelectionActionFactory("ZORDER_TO_FRONT") {
		public SelectionAction create(IEditorPart part) {
			
			ChangeZOrderAction action = new ChangeZOrderAction(part, ZOrderDirection.TO_FRONT);
			action.setId(getId());
			action.setText("맨위로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory ZORDER_TO_BACK = new SelectionActionFactory("ZORDER_TO_BACK") {
		public SelectionAction create(IEditorPart part) {
			
			ChangeZOrderAction action = new ChangeZOrderAction(part, ZOrderDirection.TO_BACK);
			action.setId(getId());
			action.setText("맨뒤로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_ROUTER = new SelectionActionFactory("CHANGE_ROUTER") {
		public SelectionAction create(IEditorPart part) {
			
			ChangeRouterAction action = new ChangeRouterAction(part);
			action.setId(getId());
			action.setText("라우터변경");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_LINE_COLOR = new SelectionActionFactory("CHANGE_LINE_COLOR") {
		public SelectionAction create(IEditorPart part) {
			
			ChangeLineColorAction action = new ChangeLineColorAction(part);
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getColorPickImageDescriptor(IconType.NORMAL));
			action.setText("칼라선택");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_BACKGROUND_COLOR = new SelectionActionFactory("CHANGE_BACKGROUND_COLOR") {
		public SelectionAction create(IEditorPart part) {
			
			ChangeBackgroundColorAction action = new ChangeBackgroundColorAction(part);
			action.setId(getId());
			action.setText("배경색");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_FONT = new SelectionActionFactory("CHANGE_FONT") {
		public SelectionAction create(IEditorPart part) {
			
			ChangeFontAction action = new ChangeFontAction(part);
			action.setId(getId());
			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getFontPickImageDescriptor(IconType.NORMAL));
			action.setText("폰트선택");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_FONT_COLOR = new SelectionActionFactory("CHANGE_FONT_COLOR") {
		public SelectionAction create(IEditorPart part) {
			
			ChangeFontColorAction action = new ChangeFontColorAction(part);
			action.setId(getId());
			action.setText("폰트칼라선택");
			
			return action;
		}
	};
	
	/**
	 * 생성자
	 * @param actionId
	 */
	protected SelectionActionFactory(String actionId) {
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
}
