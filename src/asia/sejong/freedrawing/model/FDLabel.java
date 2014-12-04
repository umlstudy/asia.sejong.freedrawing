package asia.sejong.freedrawing.model;


public class FDLabel extends FDTextShape {

	public FDLabel() {
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDLabel clone() {
		FDLabel rect = (FDLabel)super.clone();
		return rect;
	}
}
