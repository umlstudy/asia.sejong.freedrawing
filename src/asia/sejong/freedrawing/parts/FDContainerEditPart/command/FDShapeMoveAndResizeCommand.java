package asia.sejong.freedrawing.parts.FDContainerEditPart.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDShape;

/**
 * Move and resize a {@link FDRect}
 */
public class FDShapeMoveAndResizeCommand extends Command
{
	private final FDShape shape;
	private final Rectangle bounds;
	private Rectangle oldBounds;

	public FDShapeMoveAndResizeCommand(FDShape shape, Rectangle bounds) {
		this.shape = shape;
		this.bounds = bounds;
//		System.out.println(bounds.toString() + "  FDShapeMoveAndResizeCommand");
		setLabel("Modify " + getShapeType());
	}
	
	private String getShapeType() {
		return shape.getClass().getSimpleName();
	}

	/**
	 * Move and resize the {@link FDRect}
	 */
	@Override
	public void execute() {
		oldBounds = new Rectangle(shape.getLocation(), shape.getSize());
		shape.setLocation(bounds.x, bounds.y);
		shape.setSize(bounds.width, bounds.height);
	}
	
	/**
	 * Restore the {@link FDRect} to its original location and size
	 */
	@Override
	public void undo() {
		shape.setLocation(oldBounds.x, oldBounds.y);
		shape.setSize(oldBounds.width, oldBounds.height);
	}
}
