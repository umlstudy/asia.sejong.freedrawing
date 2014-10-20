package asia.sejong.freedrawing.editor;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.jface.action.IToolBarManager;

public class FreedrawingEditorActionBarContributor extends ActionBarContributor {

	protected void buildActions() {
//		addRetargetAction(new UndoRetargetAction());
//		addRetargetAction(new RedoRetargetAction());
//		addRetargetAction(new DeleteRetargetAction());
//		addRetargetAction(new LabelRetargetAction(ActionFactory.SELECT_ALL.getId(), "Select All"));
	}
	
	public void contributeToToolBar(IToolBarManager toolBarManager) {
//		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
//		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	}

	protected void declareGlobalActionKeys() {
	}
}
