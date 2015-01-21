package asia.sejong.freedrawing.editor.actions.etc;

import org.eclipse.jface.action.Action;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDRoot;

public class ChangeRouterAction extends Action {

	private FreedrawingEditor editor;
	
	public ChangeRouterAction(FreedrawingEditor editor) {
		this.editor = editor;
	}
	
	public void run() {
		FDRoot root = (FDRoot)editor.getEditorContext().getNodeRoot();
		if ( root != null ) {
			Integer connectionRouter = root.getConnectionRouter();
			if ( connectionRouter == FDRoot.ROUTER_MANUAL ) {
				root.setConnectionRouter(FDRoot.ROUTER_SHORTEST_PATH);
			} else {
				root.setConnectionRouter(FDRoot.ROUTER_MANUAL);
			}
		}
	}

//	private FDRoot getRoot(List<?> objects) {
//		if (objects.isEmpty())
//			return null;
//		if (!(objects.get(0) instanceof EditPart))
//			return null;
//		
//		EditPart editPart = (EditPart) objects.get(0);
//		return (FDRoot)editPart.getViewer().getContents().getModel();
//	}
//
//	@Override
//	protected boolean calculateEnabled() {
//		FDRoot root = getRoot(getSelectedObjects());
//		if ( root != null ) {
//			return true;
//		}
//		
//		return false;
//	}
}
