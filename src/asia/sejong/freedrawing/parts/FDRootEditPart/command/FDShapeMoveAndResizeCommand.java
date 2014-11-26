package asia.sejong.freedrawing.parts.FDRootEditPart.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRect;

/**
 * Move and resize a {@link FDRect}
 */
public class FDShapeMoveAndResizeCommand extends Command
{
	private final FDRect node;
	private final Rectangle rect;
	private Rectangle oldRect;

	public FDShapeMoveAndResizeCommand(FDRect node, Rectangle rect) {
		this.node = node;
		this.rect = rect;
		setLabel("Modify " + getNodeName());
	}
	
	private String getNodeName() {
		return "node";
	}

	/**
	 * Move and resize the {@link FDRect}
	 */
	public void execute() {
		oldRect = new Rectangle(node.getX(), node.getY(), node.getWidth(), node.getHeight());
//		System.out.println("oldRect ? " + oldRect);
		node.setLocation(rect.x, rect.y);
//		System.out.println("newRect ? " + rect);
		node.setSize(rect.width, rect.height);
	}
	
	/**
	 * Restore the {@link FDRect} to its original location and size
	 */
	public void undo() {
		node.setLocation(oldRect.x, oldRect.y);
		node.setSize(oldRect.width, oldRect.height);
	}
}
