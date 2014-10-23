package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDNode;

public interface FDNodeListener {

	void sourceAdded(FDNode source);
	void sourceRemoved(FDNode source);
	void targetAdded(FDNode target);
	void targetRemoved(FDNode target);
	void locationChanged(int newX, int newY);
	void sizeChanged(int newWidth, int newHeight);
}
