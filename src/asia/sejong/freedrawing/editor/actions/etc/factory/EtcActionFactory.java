package asia.sejong.freedrawing.editor.actions.etc.factory;

import org.eclipse.jface.action.Action;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.editor.actions.common.LocalActionFactory;
import asia.sejong.freedrawing.editor.actions.etc.ChangeEditorScaleAction;
import asia.sejong.freedrawing.editor.actions.etc.ChangeRouterAction;

public abstract class EtcActionFactory extends LocalActionFactory {

	public static final EtcActionFactory CHANGE_EDITOR_SCALE = new EtcActionFactory("CHANGE_EDITOR_SCALE") {
		public Action create(FreedrawingEditor editor) {
			ChangeEditorScaleAction action = new ChangeEditorScaleAction(editor);
			action.setId(getId());
			action.setText("스케일");
			return action;
		}
	};
	
	public static final EtcActionFactory CHANGE_ROUTER = new EtcActionFactory("CHANGE_ROUTER") {
		public Action create(FreedrawingEditor editor) {
			ChangeRouterAction action = new ChangeRouterAction(editor);
			action.setId(getId());
			action.setText("라우터변경");
			return action;
		}
	};

	/**
	 * 생성자
	 * @param actionId
	 */
	protected EtcActionFactory(String actionId) {
		super(actionId);
	}
	
	/**
	 * 팩토리메소드#2
	 * @param actionGroup
	 * @return
	 */
	public Action create(FreedrawingEditor editor) {
		throw new RuntimeException();
	}
}
