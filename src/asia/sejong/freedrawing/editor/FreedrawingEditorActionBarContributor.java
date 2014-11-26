package asia.sejong.freedrawing.editor;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;

import asia.sejong.freedrawing.editor.actions.SelectionActionFactory;

public class FreedrawingEditorActionBarContributor extends ActionBarContributor {

	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new LabelRetargetAction(ActionFactory.SELECT_ALL.getId(), "Select All"));
		addRetargetAction(new LabelRetargetAction(SelectionActionFactory.EDIT_TEXT.getId(), "Edit text"));
		addRetargetAction((RetargetAction)ActionFactory.COPY.create(getPage().getWorkbenchWindow()));
		addRetargetAction((RetargetAction)ActionFactory.PASTE.create(getPage().getWorkbenchWindow()));
	}
	
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	}

	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.PRINT.getId());
	}
}
