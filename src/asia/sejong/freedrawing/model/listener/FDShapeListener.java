package asia.sejong.freedrawing.model.listener;



public interface FDShapeListener extends FDElementListener {

	void locationChanged(int newX, int newY);
	void sizeChanged(int newWidth, int newHeight);
}
