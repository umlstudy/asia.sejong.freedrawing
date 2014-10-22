package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDNode;

public interface FDNodeListener {

	void sourceChanged(FDNode oldSource, FDNode newSource);
	void targetChanged(FDNode oldTarget, FDNode newTarget);
	void locationChanged(int newX, int newY);
	void sizeChanged(int newWidth, int newHeight);
}
