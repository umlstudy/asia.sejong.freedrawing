package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ChangeRouterCommand;


public class ChangeRouterAction extends SelectionAction {

	public ChangeRouterAction(IEditorPart part) {
		super(part);
	}
	
	public void run() {
		FDRoot root = getRoot(getSelectedObjects());
		if ( root != null ) {
			execute(new ChangeRouterCommand(root));
		}
	}

	private FDRoot getRoot(List<?> objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;
		
		EditPart editPart = (EditPart) objects.get(0);
		return (FDRoot)editPart.getViewer().getContents().getModel();
	}

	@Override
	protected boolean calculateEnabled() {
		FDRoot root = getRoot(getSelectedObjects());
		if ( root != null ) {
			return true;
		}
		
		return false;
	}
}
