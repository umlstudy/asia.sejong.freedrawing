package asia.sejong.freedrawing.model.listener;



public interface FDShapeListener extends FDWireEndPointListener {

	void sizeChanged(int newWidth, int newHeight);
}
