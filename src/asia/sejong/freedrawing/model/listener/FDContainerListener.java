package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDShape;

public interface FDContainerListener extends FDBaseListener {
	
	void childShapeAdded(FDShape child);

	void childShapeRemoved(FDShape child);

	void positionChanged(int newPosition, FDShape child);
}
