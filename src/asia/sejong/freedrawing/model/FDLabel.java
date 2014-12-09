package asia.sejong.freedrawing.model;


public class FDLabel extends FDTextShape {

	private static final long serialVersionUID = -1236176647745936636L;

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
