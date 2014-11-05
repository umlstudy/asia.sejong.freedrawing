package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

public class MoveLeftAction extends SelectionAction {

	private boolean pressed = false;
	
	public MoveLeftAction(IWorkbenchPart part, boolean pressed) {
		super(part);
		this.pressed = pressed;
		setLazyEnablementCalculation(false);
	}
	
	public Command createMoveCommand(List<?> objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;

		ChangeBoundsRequest moveReq = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
		int movePoint = -1;
		if ( pressed ) {
			movePoint = -5;
		}
		moveReq.setMoveDelta(new Point(movePoint, 0));
		moveReq.setEditParts(objects);

		CompoundCommand compoundCmd = new CompoundCommand("Move Left");
		for (int i = 0; i < objects.size(); i++) {
			EditPart object = (EditPart) objects.get(i);
			Command cmd = object.getCommand(moveReq);
			if (cmd != null) {
				compoundCmd.add(cmd);
			}
		}

		return compoundCmd;
	}
	
	public void run() {
		System.out.println(getId());
		execute(createMoveCommand(getSelectedObjects()));
	}

	@Override
	protected boolean calculateEnabled() {
		Command cmd = createMoveCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}
}
