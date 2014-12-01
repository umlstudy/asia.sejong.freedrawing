package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDRect;

public interface FDContainerListener {
	
	void childNodeAdded(FDRect child);

	void childNodeRemoved(FDRect child);
	
//	void changeToFront(FDNode child);
//
//	void changeToBack(FDNode child);

	void positionChanged(int newPosition, FDRect child);
}
