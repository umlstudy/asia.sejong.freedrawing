package asia.sejong.freedrawing.parts.handles;

import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;

public class MoveHandleEx extends MoveHandle {

	public MoveHandleEx(GraphicalEditPart owner) {
		super(owner);
	}

	protected void initialize() {
		setOpaque(false);
//		setBorder(new LineBorder(1));
		setCursor(Cursors.SIZEALL);
	}
}
