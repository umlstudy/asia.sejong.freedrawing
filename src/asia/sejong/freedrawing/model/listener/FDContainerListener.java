package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDNode;

public interface FDContainerListener {
	
	void childNodeAdded(FDNode child);

	void childNodeRemoved(FDNode child);

//	void changeToFront(FDNode child);
//
//	void changeToBack(FDNode child);

	void positionChanged(int newPosition, FDNode child);
}
