package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDShape;

public interface FDContainerListener {
	
	void childShapeAdded(FDShape child);

	void childShapeRemoved(FDShape child);
	
//	void changeToFront(FDNode child);
//
//	void changeToBack(FDNode child);

	void positionChanged(int newPosition, FDShape child);
}
