package asia.sejong.freedrawing.parts.FDContainerEditPart.command;

import java.util.List;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDGroupShape;
import asia.sejong.freedrawing.model.FDShape;

public class GroupingCommand extends Command {

	private FDContainer container;
	private FDGroupShape fdGroupShape;
	
	public GroupingCommand(FDContainer container, List<FDShape> shapes) {
		this.container = container;
		this.fdGroupShape = new FDGroupShape();
		this.fdGroupShape.setShapes(shapes);
	}
	
	@Override
	public void execute() {
		for ( FDShape shape : fdGroupShape.getShapes() ) {
			container.removeShape(shape);
		}
		container.addShape(fdGroupShape);
	}
	
	@Override
	public void undo() {
		container.removeShape(fdGroupShape);
		for ( FDShape shape : fdGroupShape.getShapes() ) {
			container.addShape(shape);
		}
	}
}
