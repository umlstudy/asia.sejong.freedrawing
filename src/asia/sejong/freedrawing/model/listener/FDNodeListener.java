package asia.sejong.freedrawing.model.listener;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDWire;

public interface FDNodeListener extends TextObjectListener {

//	void sourceAdded(FDRect source);
//	void sourceRemoved(FDRect source);
	void targetAdded(FDRect target);
	void targetRemoved(FDRect target, FDWire removedWire);
	void locationChanged(int newX, int newY);
	void sizeChanged(int newWidth, int newHeight);
	void textChanged(String newText);
//	void bendpointAdded(int locationIndex, Point location, FDRect target);
//	void bendpointRemoved(int locationIndex, FDRect target);
//	void bendpointMoved(int locationIndex, Point newPoint, FDRect target);
}
