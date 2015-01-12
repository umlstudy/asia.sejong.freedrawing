package asia.sejong.freedrawing.parts.handles;

import java.util.List;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.swt.graphics.Cursor;

public class ResizableHandleKitEx extends ResizableHandleKit {

	@SuppressWarnings("unchecked")
	public static void addHandle(GraphicalEditPart part, @SuppressWarnings("rawtypes") List handles, int direction, DragTracker tracker, Cursor cursor) {
		handles.add(createHandle(part, direction, tracker, cursor));
	}
	
	static Handle createHandle(GraphicalEditPart owner, int direction, DragTracker tracker, Cursor cursor) {
		ResizeHandleEx handle = new ResizeHandleEx(owner, direction);
		handle.setDragTracker(tracker);
		handle.setCursor(cursor);
		return handle;
	}
	
	@SuppressWarnings("unchecked")
	public static void addMoveHandle(GraphicalEditPart part, @SuppressWarnings("rawtypes") List handles, DragTracker tracker, Cursor cursor) {
		handles.add(moveHandle(part, tracker, cursor));
	}
	
	public static Handle moveHandle(GraphicalEditPart owner, DragTracker tracker, Cursor cursor) {
		MoveHandle moveHandle = new MoveHandleEx(owner);
		moveHandle.setDragTracker(tracker);
		moveHandle.setCursor(cursor);
		return moveHandle;
	}
}
