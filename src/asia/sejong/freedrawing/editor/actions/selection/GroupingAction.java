package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.parts.FDContainerEditPart.command.GroupingCommand;

public class GroupingAction extends SelectionAction {

	public GroupingAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}
	
	public void run() {
		FDContainer parent = getParent(getSelectedObjects().get(0));
		List<FDShape> selectedShapes = getShapes(getSelectedObjects());
		execute(new GroupingCommand(parent, selectedShapes));
	}


	@Override
	protected boolean calculateEnabled() {
		return true;
	}
	
	private List<FDShape> getShapes(List<?> selectedObjects) {
		List<FDShape> shapes = new ArrayList<FDShape>();
		for ( Object selectedObject : selectedObjects ) {
			if (selectedObject instanceof GraphicalEditPart ) {
				GraphicalEditPart gep = (GraphicalEditPart) selectedObject;
				if ( gep.getModel() instanceof FDShape ) {
					shapes.add((FDShape)gep.getModel());
				}
			}
		}
		return shapes;
	}
	
	private FDContainer getParent(Object selectedObject) {
		if (selectedObject instanceof GraphicalEditPart ) {
			GraphicalEditPart gep = (GraphicalEditPart) selectedObject;
			return (FDContainer)gep.getParent().getModel();
		}
		
		throw new NullPointerException();
	}
}
