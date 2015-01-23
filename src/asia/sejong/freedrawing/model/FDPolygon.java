package asia.sejong.freedrawing.model;


public class FDPolygon extends FDTextShape {

	private static final long serialVersionUID = -4826394023152452709L;

	FDPolygon() {
	}
	
	//============================================================
	// Clonable
	
	@Override
	public FDPolygon clone() {
		FDPolygon polygon = (FDPolygon)super.clone();
		return polygon;
	}
}
