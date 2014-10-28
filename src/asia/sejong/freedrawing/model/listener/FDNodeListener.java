package asia.sejong.freedrawing.model.listener;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import asia.sejong.freedrawing.model.FDNode;

public interface FDNodeListener extends TextObjectListener {

	void sourceAdded(FDNode source);
	void sourceRemoved(FDNode source);
	void targetAdded(FDNode target, List<Point> targetBendpoints);
	void targetRemoved(FDNode target);
	void locationChanged(int newX, int newY);
	void sizeChanged(int newWidth, int newHeight);
	void textChanged(String newText);
	void bendpointAdded(int locationIndex, Point location, FDNode target);
	void bendpointRemoved(int locationIndex, FDNode target);
	void bendpointMoved(int locationIndex, Point newPoint, FDNode target);
}
