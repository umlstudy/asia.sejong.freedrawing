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

public class MoveAction extends SelectionAction {

	private Direction direction;
	
	public static enum Direction {
		East, West, South, North;
	}
	
	public MoveAction(IWorkbenchPart part, Direction direction) {
		super(part);
		this.direction = direction;
		setLazyEnablementCalculation(false);
	}
	
	public Command createMoveCommand(List<?> objects) {
		if (objects.isEmpty())
			return null;
		if (!(objects.get(0) instanceof EditPart))
			return null;

		ChangeBoundsRequest moveReq = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);

		Point point = new Point(0,0);
		String actionName = null;
		switch ( direction ) {
		case East : 
			actionName = "Move Left";
			point.setX(-1);
			break;
		case West :
			actionName = "Move Right";
			point.setX(1);
			break;
		case South :
			actionName = "Move Down";
			point.setY(1);
			break;
		case North :
			actionName = "Move Up";
			point.setY(-1);
			break;
		}
			
		moveReq.setMoveDelta(point);
		moveReq.setEditParts(objects);

		CompoundCommand compoundCmd = new CompoundCommand(actionName);
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
