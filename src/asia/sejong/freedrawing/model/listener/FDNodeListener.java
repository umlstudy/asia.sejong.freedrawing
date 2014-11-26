package asia.sejong.freedrawing.model.listener;

import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.model.FDWire;

public interface FDNodeListener extends TextObjectListener {

	void sourceAdded(FDRect source);
	void sourceRemoved(FDRect source);
	void targetAdded(FDRect target, FDWire targetWire);
	void targetRemoved(FDRect target);
	void locationChanged(int newX, int newY);
	void sizeChanged(int newWidth, int newHeight);
	void textChanged(String newText);
	void bendpointAdded(int locationIndex, Point location, FDRect target);
	void bendpointRemoved(int locationIndex, FDRect target);
	void bendpointMoved(int locationIndex, Point newPoint, FDRect target);
}
