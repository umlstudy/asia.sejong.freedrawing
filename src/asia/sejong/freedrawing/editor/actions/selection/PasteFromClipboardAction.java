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

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDRoot;
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
				RootEditPart root = gep.getRoot();
				FDRoot nodeRoot = (FDRoot)root.getContents().getModel();
				ArrayList<?> originals = (ArrayList<?>)getClipboardContents();
				if (originals != null) {
					List<FDRect> copiedNodes = new ArrayList<FDRect>();
					for ( Object item : originals ) {
						if ( item instanceof FDRect ) {
							FDRect copiedNode = (FDRect)((FDRect) item).clone();
							Point nextLocation = null;
							nextLocation = nodeRoot.getNextLocation(copiedNode.getX(), copiedNode.getY());
							nextLocation = getNextLocation(copiedNodes, nextLocation.x, nextLocation.y);
							copiedNode.setLocation(nextLocation.x, nextLocation.y);
							copiedNodes.add(copiedNode);
						}
					}
					
					if ( copiedNodes.size() > 0 ) {
						CompoundCommand compoundCommand = new CompoundCommand();
						for ( FDRect node : copiedNodes ) {
							compoundCommand.add(new FDShapeCreateCommand(nodeRoot, node));
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
	
	public Point getNextLocation(List<FDRect> nodes, int x, int y) {
		while ( true ) {
			boolean alreadyExistInLocation = false;
			for ( FDRect item : nodes ) {
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
