package asia.sejong.freedrawing.model.area.listener;

import asia.sejong.freedrawing.model.connection.AbstractFDConnection;

public interface FDElementListener extends FDContainerListener {

	void connectionSourceSetted(AbstractFDConnection connection);
	void connectionTargetSetted(AbstractFDConnection connection);
	
}
