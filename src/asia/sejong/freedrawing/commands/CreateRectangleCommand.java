package asia.sejong.freedrawing.commands;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.area.FDRectangle;
import asia.sejong.freedrawing.model.area.FreedrawingData;

public class CreateRectangleCommand extends Command {

	private FreedrawingData freedrawingData;
	private FDRectangle rectangle;
	
	public CreateRectangleCommand(FreedrawingData freedrawingData, FDRectangle rectangle) {
		this.freedrawingData = freedrawingData;
		this.rectangle = rectangle;
	}
	
	public void execute() {
		freedrawingData.addElement(rectangle);
	}
}
