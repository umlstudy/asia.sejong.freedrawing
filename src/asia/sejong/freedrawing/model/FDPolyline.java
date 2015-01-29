package asia.sejong.freedrawing.model;



public class FDPolyline extends FDPolygon {

	private static final long serialVersionUID = 7801623026707315354L;

	FDPolyline() {
		super();
	}

	//============================================================
	// Clonable
	
	@Override
	public FDPolyline clone() {
		FDPolyline polyline = (FDPolyline)super.clone();
		return polyline;
	}
}
