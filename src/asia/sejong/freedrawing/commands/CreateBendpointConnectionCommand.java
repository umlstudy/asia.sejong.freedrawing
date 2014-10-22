package asia.sejong.freedrawing.commands;

import java.sql.Connection;

import asia.sejong.freedrawing.model.area.AbstractFDElement;
import asia.sejong.freedrawing.model.connection.FDBendpointConnection;
import asia.sejong.freedrawing.util.DebugUtil;

public class CreateBendpointConnectionCommand extends CreateConnectionCommand {
	
	private AbstractFDElement source;
	private AbstractFDElement target;
	
	public CreateBendpointConnectionCommand() {
		super();
		System.out.println("CreateBendpointConnectionCommand created!!");
	}
	
	public void execute() {
		System.out.println(this.getClass().getSimpleName() + " execute started!!");
		
		FDBendpointConnection connection = new FDBendpointConnection();
		
		connection.setSource(source);
		connection.setTarget(target);
		
		getRootModel().addConnection(connection);

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
		DebugUtil.printLogStart();
		System.out.println("Source ? " +source + ", Target ? " + target);
		if ( source != null && target instanceof AbstractFDElement && source != target ) {
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
