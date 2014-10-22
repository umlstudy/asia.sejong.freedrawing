package asia.sejong.freedrawing.parts.FDNodeRootEditPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDNode;

/**
 * Move and resize a {@link FDNode}
 */
public class MoveAndResizeFDNodeCommand extends Command
{
	private final FDNode node;
	private final Rectangle rect;
	private Rectangle oldRect;

	public MoveAndResizeFDNodeCommand(FDNode node, Rectangle rect) {
		this.node = node;
		this.rect = rect;
		setLabel("Modify " + getNodeName());
	}
	
	private String getNodeName() {
		return "node";
	}

	/**
	 * Move and resize the {@link FDNode}
	 */
	public void execute() {
		oldRect = new Rectangle(node.getX(), node.getY(), node.getWidth(), node.getHeight());
		node.setLocation(rect.x, rect.y);
		node.setSize(rect.width, rect.height);
	}
	
	/**
	 * Restore the {@link FDNode} to its original location and size
	 */
	public void undo() {
		node.setLocation(oldRect.x, oldRect.y);
		node.setSize(oldRect.width, oldRect.height);
	}
}
