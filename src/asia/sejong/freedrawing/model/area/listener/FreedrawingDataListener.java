package asia.sejong.freedrawing.model.area.listener;

import asia.sejong.freedrawing.model.connection.AbstractFDConnection;

public interface FreedrawingDataListener extends FDContainerListener {
	
	void childConnectionAdded(AbstractFDConnection child);
}
