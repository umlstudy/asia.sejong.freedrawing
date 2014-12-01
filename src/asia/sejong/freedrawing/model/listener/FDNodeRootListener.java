package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDWire;


public interface FDNodeRootListener extends FDContainerListener {
	
	void wireAdded(FDWire wire);

	void wireRemoved(FDWire wire);
}
