package asia.sejong.freedrawing.parts.FDContainerEditPart.command;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FDContainer;
import asia.sejong.freedrawing.model.FDGroupShape;
import asia.sejong.freedrawing.model.FDShape;

public class UngroupingCommand extends Command {

	private FDContainer container;
	private FDGroupShape fdGroupShape;
	
	public UngroupingCommand(FDContainer container, FDGroupShape fdGroupShape) {
		this.container = container;
		this.fdGroupShape = fdGroupShape;
	}
	
	@Override
	public void execute() {
		container.removeShape(fdGroupShape);
		for ( FDShape shape : fdGroupShape.getShapes() ) {
			container.addShape(shape);
		}
	}
	
	@Override
	public void undo() {
		for ( FDShape shape : fdGroupShape.getShapes() ) {
			container.removeShape(shape);
		}
		container.addShape(fdGroupShape);
	}
}
