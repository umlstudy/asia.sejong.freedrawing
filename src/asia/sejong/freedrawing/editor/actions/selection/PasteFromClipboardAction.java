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

import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.model.FDNodeRoot;
import asia.sejong.freedrawing.parts.FDNodeRootEditPart.cmd.CreateFDNodeCommand;

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
				FDNodeRoot nodeRoot = (FDNodeRoot)root.getContents().getModel();
				ArrayList<?> originals = (ArrayList<?>)getClipboardContents();
				if (originals != null) {
					List<FDNode> copiedNodes = new ArrayList<FDNode>();
					for ( Object item : originals ) {
						if ( item instanceof FDNode ) {
							FDNode copiedNode = ((FDNode) item).clone();
							Point nextLocation = null;
							nextLocation = nodeRoot.getNextLocation(copiedNode.getX(), copiedNode.getY());
							nextLocation = getNextLocation(copiedNodes, nextLocation.x, nextLocation.y);
							copiedNode.setLocation(nextLocation.x, nextLocation.y);
							copiedNodes.add(copiedNode);
						}
					}
					
					if ( copiedNodes.size() > 0 ) {
						CompoundCommand compoundCommand = new CompoundCommand();
						for ( FDNode node : copiedNodes ) {
							compoundCommand.add(new CreateFDNodeCommand(nodeRoot, node));
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
	
	public Point getNextLocation(List<FDNode> nodes, int x, int y) {
		while ( true ) {
			boolean alreadyExistInLocation = false;
			for ( FDNode item : nodes ) {
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
