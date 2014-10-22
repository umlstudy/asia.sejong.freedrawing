package asia.sejong.freedrawing.commands;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.area.FreedrawingData;

public abstract class CreateConnectionCommand extends Command {

	private FreedrawingData freedrawingData;
	
	public CreateConnectionCommand() {
		setLabel("Create " + getConnectionName());
	}

	public abstract String getConnectionName();
	
	public abstract boolean isValidSource(Object source);
	public abstract boolean isValidTarget(Object target);

	public abstract void setSource(Object source);
	public abstract void setTarget(Object target);

	protected FreedrawingData getRootModel() {
		return freedrawingData;
	}

	public void setRootModel(FreedrawingData freedrawingData) {
		this.freedrawingData = freedrawingData;
	}

}
