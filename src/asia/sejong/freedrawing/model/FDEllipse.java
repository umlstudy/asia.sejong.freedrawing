package asia.sejong.freedrawing.model;


public class FDEllipse extends FDTextShape {

	private static final long serialVersionUID = -7323596012520749962L;

	FDEllipse() {
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDEllipse clone() {
		FDEllipse ellipse = (FDEllipse)super.clone();
		return ellipse;
	}
}
