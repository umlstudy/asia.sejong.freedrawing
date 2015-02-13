package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDGroupShape;
import asia.sejong.freedrawing.parts.FDContainerEditPart.command.UngroupingCommand;

public class UngroupingAction extends SelectionAction {

	public UngroupingAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}
	
	public void run() {
		FDContainer parent = getParent(getSelectedObjects().get(0));
		List<FDGroupShape> selectedGroupShapes = getGroupShape(getSelectedObjects());
		
		Command cmd = null;
		if ( selectedGroupShapes.size()>1) {
			CompoundCommand compountCmd = new CompoundCommand();
			for ( FDGroupShape groupShape : selectedGroupShapes ) {
				compountCmd.add(new UngroupingCommand(parent, groupShape));
			}
		} else {
			cmd = new UngroupingCommand(parent, selectedGroupShapes.get(0));
		}
		
		execute(cmd);
	}


	@Override
	protected boolean calculateEnabled() {
		return true;
	}
	
	private List<FDGroupShape> getGroupShape(List<?> selectedObjects) {
		List<FDGroupShape> groupShapes = new ArrayList<FDGroupShape>();
		for ( Object selectedObject : selectedObjects ) {
			if (selectedObject instanceof GraphicalEditPart ) {
				GraphicalEditPart gep = (GraphicalEditPart) selectedObject;
				if ( gep.getModel() instanceof FDGroupShape ) {
					groupShapes.add((FDGroupShape)gep.getModel());
				}
			}
		}
		return groupShapes;
	}
	
	private FDContainer getParent(Object selectedObject) {
		if (selectedObject instanceof GraphicalEditPart ) {
			GraphicalEditPart gep = (GraphicalEditPart) selectedObject;
			return (FDContainer)gep.getParent().getModel();
		}
		
		throw new NullPointerException();
	}
}
