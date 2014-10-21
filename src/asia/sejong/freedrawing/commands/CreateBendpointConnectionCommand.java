package asia.sejong.freedrawing.commands;

import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.model.connection.FDBendpointConnection;

public class CreateBendpointConnectionCommand extends CreateConnectionCommand {
	
	private FDBendpointConnection connection;
	private AbstractFDElement source;
	private AbstractFDElement target;
	
	public CreateBendpointConnectionCommand(FDBendpointConnection connection) {
		this.connection = connection;
		System.out.println("CreateBendpointConnectionCommand created!!");
	}
	
	public void execute() {
		System.out.println(this.getClass().getSimpleName() + " execute started!!");
		
		connection.setSource(source);
		connection.setTarget(target);

		System.out.println(this.getClass().getSimpleName() + " execute ended!!");
	}

	@Override
	public String getConnectionName() {
		return "BendpointConnection";
	}

	@Override
	public boolean isValidSource(Object source) {
		if ( source instanceof AbstractFDElement ) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidTarget(Object target) {
		if ( target instanceof AbstractFDElement && source != target ) {
			return true;
		}
		return false;
	}

	@Override
	public void setSource(Object source) {
		if ( isValidSource(source) ) {
			this.source = (AbstractFDElement)source;
		}
	}

	@Override
	public void setTarget(Object target) {
		if ( isValidTarget(target) ) {
			this.target = (AbstractFDElement)target;
		}
	}
}
