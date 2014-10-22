package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDNode;

public interface FDNodeListener {

	void sourceChanged(FDNode source);
	void targetChanged(FDNode target);
}
