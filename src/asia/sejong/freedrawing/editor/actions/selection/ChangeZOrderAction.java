package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ToBackZOrderCommand;
import asia.sejong.freedrawing.parts.FDShapeEditPart.command.ToFrontZOrderCommand;

public class ChangeZOrderAction extends SelectionAction {

	private ZOrderDirection direction;
	
	public static enum ZOrderDirection {
		TO_FRONT, TO_BACK;
	}
	
	public ChangeZOrderAction(IWorkbenchPart part, ZOrderDirection direction) {
		super(part);
		this.direction = direction;
		setLazyEnablementCalculation(false);
	}
	
	public Command createZOrderCommand(List<?> objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;

		String actionName = null;
		switch ( direction ) {
		case TO_FRONT : 
			actionName = "To Front";
			break;
		case TO_BACK :
			actionName = "To Back";
			break;
		}
			
		CompoundCommand compoundCmd = new CompoundCommand(actionName);
		for (int i = 0; i < objects.size(); i++) {
			EditPart object = (EditPart) objects.get(i);
			if ( object.getModel() instanceof FDRect ) {
				
				Command cmd = null;
				switch ( direction ) {
				case TO_FRONT : 
					cmd = new ToFrontZOrderCommand((FDRect)object.getModel());
					break;
				case TO_BACK :
					cmd = new ToBackZOrderCommand((FDRect)object.getModel());
					actionName = "To Back";
					break;
				}

				if (cmd != null) {
					compoundCmd.add(cmd);
				}
			}
		}

		return compoundCmd;
	}
	
	public void run() {
		execute(createZOrderCommand(getSelectedObjects()));
	}

	@Override
	protected boolean calculateEnabled() {
		Command cmd = createZOrderCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}
}
