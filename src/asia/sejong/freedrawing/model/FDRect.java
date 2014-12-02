package asia.sejong.freedrawing.model;


public class FDRect extends FDTextShape {

	public FDRect() {
	}
	
	//============================================================
	// Clonable
	@Override
	public FDRect clone() {
		FDRect rect = (FDRect)super.clone();
		return rect;
	}
}
