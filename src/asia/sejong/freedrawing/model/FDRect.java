package asia.sejong.freedrawing.model;


public class FDRect extends FDTextShape {

	private static final long serialVersionUID = 4824590594732340051L;

	FDRect() {}
	
	//============================================================
	// Clonable
	
	@Override
	public FDRect clone() {
		FDRect rect = (FDRect)super.clone();
		return rect;
	}
}
