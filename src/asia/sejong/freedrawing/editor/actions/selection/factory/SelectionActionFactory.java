package asia.sejong.freedrawing.editor.actions.selection.factory;

import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.actions.ActionFactory;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.editor.actions.common.LocalActionFactory;
import asia.sejong.freedrawing.editor.actions.selection.ChangeAlphaAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeBackgroundColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeFontAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeFontColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeLineColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeLineStyleAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeLineThickAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeRotateAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeZOrderAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeZOrderAction.ZOrderDirection;
import asia.sejong.freedrawing.editor.actions.selection.MoveAction;
import asia.sejong.freedrawing.editor.actions.selection.MoveAction.Direction;

public abstract class SelectionActionFactory extends LocalActionFactory {

	public static final SelectionActionFactory EDIT_TEXT = new SelectionActionFactory(ActionFactory.RENAME.getId()) {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			DirectEditAction action = new DirectEditAction(part);
			action.setId(getId());
			action.setText("텍스트 편집");
			
			return action;
		}
	};
	
	//============================================================
	// 1Pixel Move
	
	public static final SelectionActionFactory MOVE_LEFT = new SelectionActionFactory("MOVE_LEFT") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.East, false);
			action.setId(getId());
			action.setText("왼쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_RIGHT = new SelectionActionFactory("MOVE_RIGHT") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.West, false);
			action.setId(getId());
			action.setText("오른쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_DOWN = new SelectionActionFactory("MOVE_DOWN") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.South, false);
			action.setId(getId());
			action.setText("아래로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_UP = new SelectionActionFactory("MOVE_UP") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.North, false);
			action.setId(getId());
			action.setText("위로");
			
			return action;
		}
	};
	
	//============================================================
	// XFixel Move
	
	public static final SelectionActionFactory MOVE_LEFT_FAST = new SelectionActionFactory("MOVE_LEFT_FAST") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.East, true);
			action.setId(getId());
			action.setText("왼쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_RIGHT_FAST = new SelectionActionFactory("MOVE_RIGHT_FAST") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.West, true);
			action.setId(getId());
			action.setText("오른쪽으로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_DOWN_FAST = new SelectionActionFactory("MOVE_DOWN_FAST") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.South, true);
			action.setId(getId());
			action.setText("아래로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory MOVE_UP_FAST = new SelectionActionFactory("MOVE_UP_FAST") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			MoveAction action = new MoveAction(part, Direction.North, true);
			action.setId(getId());
			action.setText("위로");
			
			return action;
		}
	};
	
	//============================================================
	// ZORDER
	
	public static final SelectionActionFactory ZORDER_TO_FRONT = new SelectionActionFactory("ZORDER_TO_FRONT") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeZOrderAction action = new ChangeZOrderAction(part, ZOrderDirection.TO_FRONT);
			action.setId(getId());
			action.setText("맨위로");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory ZORDER_TO_BACK = new SelectionActionFactory("ZORDER_TO_BACK") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeZOrderAction action = new ChangeZOrderAction(part, ZOrderDirection.TO_BACK);
			action.setId(getId());
			action.setText("맨뒤로");
			
			return action;
		}
	};
	
	//============================================================
	// EDITOR PROPERTIES
	
	public static final SelectionActionFactory CHANGE_ROTATION = new SelectionActionFactory("CHANGE_ROTATION") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeRotateAction action = new ChangeRotateAction(part);
			action.setId(getId());
			action.setText("각도변경");
			
			return action;
		}
	};
//	
//	public static final SelectionActionFactory CHANGE_ROUTER = new SelectionActionFactory("CHANGE_ROUTER") {
//		public SelectionAction create(IEditorPart part) {
//			
//			ChangeRouterAction action = new ChangeRouterAction(part);
//			action.setId(getId());
//			action.setText("라우터변경");
//			
//			return action;
//		}
//	};
	
	//============================================================
	// LINE ATTRIBUTES
	
	public static final SelectionActionFactory CHANGE_LINE_STYLE = new SelectionActionFactory("CHANGE_LINE_STYLE") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeLineStyleAction action = new ChangeLineStyleAction(part);
			action.setId(getId());
//			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getColorPickImageDescriptor(IconType.NORMAL));
			action.setText("선종류");

			return action;
		}
	};
	
//	public static final SelectionActionFactory CHANGE_LINE_STYLE_DASH = new SelectionActionFactory("CHANGE_LINE_STYLE_DASH") {
//		public SelectionAction create(IEditorPart part) {
//			
//			ChangeLineStyleAction action = new ChangeLineStyleAction(part, LineStyle.DASH);
//			action.setId(getId());
////			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getColorPickImageDescriptor(IconType.NORMAL));
//			action.setText("데쉬");
//			
//			return action;
//		}
//	};
//	
//	public static final SelectionActionFactory CHANGE_LINE_STYLE_DASHDOT = new SelectionActionFactory("CHANGE_LINE_STYLE_DASHDOT") {
//		public SelectionAction create(IEditorPart part) {
//			
//			ChangeLineStyleAction action = new ChangeLineStyleAction(part, LineStyle.DASHDOT);
//			action.setId(getId());
////			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getColorPickImageDescriptor(IconType.NORMAL));
//			action.setText("데쉬점");
//			
//			return action;
//		}
//	};
//	
//	public static final SelectionActionFactory CHANGE_LINE_STYLE_DASHDOTDOT = new SelectionActionFactory("CHANGE_LINE_STYLE_DASHDOTDOT") {
//		public SelectionAction create(IEditorPart part) {
//			
//			ChangeLineStyleAction action = new ChangeLineStyleAction(part, SWT.LINE_DASHDOTDOT);
//			action.setId(getId());
////			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getColorPickImageDescriptor(IconType.NORMAL));
//			action.setText("데쉬점점");
//			
//			return action;
//		}
//	};
//	
//	public static final SelectionActionFactory CHANGE_LINE_STYLE_DOT = new SelectionActionFactory("CHANGE_LINE_STYLE_DOT") {
//		public SelectionAction create(IEditorPart part) {
//			
//			ChangeLineStyleAction action = new ChangeLineStyleAction(part, SWT.LINE_DOT);
//			action.setId(getId());
////			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getColorPickImageDescriptor(IconType.NORMAL));
//			action.setText("점");
//			
//			return action;
//		}
//	};
//	
//	public static final SelectionActionFactory CHANGE_LINE_STYLE_SOLID = new SelectionActionFactory("CHANGE_LINE_STYLE_SOLID") {
//		public SelectionAction create(IEditorPart part) {
//			
//			ChangeLineStyleAction action = new ChangeLineStyleAction(part, SWT.LINE_SOLID);
//			action.setId(getId());
////			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getColorPickImageDescriptor(IconType.NORMAL));
//			action.setText("솔리드");
//			
//			return action;
//		}
//	};
	
	//============================================================
	// OBJECT ATTRIBUTES
	
	public static final SelectionActionFactory CHANGE_LINE_COLOR = new SelectionActionFactory("CHANGE_LINE_COLOR") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeLineColorAction action = new ChangeLineColorAction(part);
			action.setId(getId());
			action.setText("선색");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_BACKGROUND_COLOR = new SelectionActionFactory("CHANGE_BACKGROUND_COLOR") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeBackgroundColorAction action = new ChangeBackgroundColorAction(part);
			action.setId(getId());
			action.setText("배경색");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_FONT = new SelectionActionFactory("CHANGE_FONT") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeFontAction action = new ChangeFontAction(part);
			action.setId(getId());
//			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getFontPickImageDescriptor(IconType.NORMAL));
			action.setText("글꼴");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_ALPHA = new SelectionActionFactory("CHANGE_ALPHA") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeAlphaAction action = new ChangeAlphaAction(part);
			action.setId(getId());
//			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getFontPickImageDescriptor(IconType.NORMAL));
			action.setText("투명도");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_FONT_COLOR = new SelectionActionFactory("CHANGE_FONT_COLOR") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeFontColorAction action = new ChangeFontColorAction(part);
			action.setId(getId());
			action.setText("글자색");
			
			return action;
		}
	};
	
	public static final SelectionActionFactory CHANGE_LINE_WIDTH = new SelectionActionFactory("CHANGE_LINE_WIDTH") {
		@Override
		public SelectionAction create(FreedrawingEditor part) {
			
			ChangeLineThickAction action = new ChangeLineThickAction(part);
			action.setId(getId());
			action.setText("선두께");
			
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
	public SelectionAction create(FreedrawingEditor part) {
		throw new RuntimeException();
	}
}
