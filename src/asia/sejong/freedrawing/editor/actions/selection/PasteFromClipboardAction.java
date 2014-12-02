package asia.sejong.freedrawing.editor.actions.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDShape;
import asia.sejong.freedrawing.parts.FDRootEditPart.command.FDShapeCreateCommand;

public class PasteFromClipboardAction extends SelectionAction {

	public PasteFromClipboardAction(IEditorPart editor) {
		super(editor);
		setId(ActionFactory.PASTE.getId());
		setText("Paste model to clipboard");
	}
	
	protected Command createPasteCommand() {
		
		Command result = null;
		List<?> selection = getSelectedObjects();
		if (selection != null && selection.size() > 0) {
			Object obj = selection.get(0);
			if (obj instanceof GraphicalEditPart ) {
				GraphicalEditPart gep = (GraphicalEditPart) obj;
				RootEditPart rootEditPart = gep.getRoot();
				FDRoot root = (FDRoot)rootEditPart.getContents().getModel();
				ArrayList<?> originals = (ArrayList<?>)getClipboardContents();
				if (originals != null) {
					List<FDShape> copiedShapes = new ArrayList<FDShape>();
					for ( Object item : originals ) {
						if ( item instanceof FDShape ) {
							FDShape copiedShape = ((FDShape) item).clone();
							Point nextLocation = null;
							nextLocation = root.getNextLocation(copiedShape.getX(), copiedShape.getY());
							nextLocation = getNextLocation(copiedShapes, nextLocation.x, nextLocation.y);
							copiedShape.setLocation(nextLocation.x, nextLocation.y);
							copiedShapes.add(copiedShape);
						}
					}
					
					if ( copiedShapes.size() > 0 ) {
						CompoundCommand compoundCommand = new CompoundCommand();
						for ( FDShape shpae : copiedShapes ) {
							compoundCommand.add(new FDShapeCreateCommand(root, shpae));
						}
						return compoundCommand;
					}
				}
			}
		}
		return result;
	}

	protected boolean calculateEnabled() {
		return true;
	}

	public void run() {
		execute(createPasteCommand());
	}

	protected Object getClipboardContents() {
		return Clipboard.getDefault().getContents();
	}
	
	public Point getNextLocation(List<FDShape> shapes, int x, int y) {
		while ( true ) {
			boolean alreadyExistInLocation = false;
			for ( FDShape item : shapes ) {
				if ( item.getX() == x && item.getY() == y ) {
					alreadyExistInLocation = true;
				}
			}
			
			if ( alreadyExistInLocation ) {
				x+=10;
				y+=10;
			} else {
				break;
			}
		}
		
		return new Point(x, y);
	}
	
//	protected Point getPasteLocation(GraphicalEditPart container) {
//		Point result = new Point(10, 10);
//		IFigure fig = container.getContentPane();
//		result.translate(fig.getClientArea(Rectangle.SINGLETON).getLocation());
//		fig.translateToAbsolute(result);
//		return result;
//	}
}
