package asia.sejong.freedrawing.model.listener;



public interface FDImageListener extends FDShapeListener {
	void imageChanged(byte[] imageBytes);
}
