package asia.sejong.freedrawing.model.listener;


public interface FDNodeListener extends TextObjectListener {

//	void sourceAdded(FDRect source);
//	void sourceRemoved(FDRect source);
//	void wireAdded(FDWire wire);
//	void wireRemoved(FDWire wire);
	void locationChanged(int newX, int newY);
	void sizeChanged(int newWidth, int newHeight);
	void textChanged(String newText);
//	void bendpointAdded(int locationIndex, Point location, FDRect target);
//	void bendpointRemoved(int locationIndex, FDRect target);
//	void bendpointMoved(int locationIndex, Point newPoint, FDRect target);
}
