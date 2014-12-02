package asia.sejong.freedrawing.model;


public class FDEllipse extends FDTextShape {

	public FDEllipse() {
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDEllipse clone() {
		FDEllipse rect = (FDEllipse)super.clone();
		return rect;
	}
}
