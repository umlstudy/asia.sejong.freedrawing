package asia.sejong.freedrawing.model;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;


public class FDPolyline extends FDPolygon {

	private static final long serialVersionUID = 7801623026707315354L;

	FDPolyline(List<Point> points) {
		super(points);
	}

	//============================================================
	// Clonable
	
	@Override
	public FDPolyline clone() {
		FDPolyline polyline = (FDPolyline)super.clone();
		return polyline;
	}
}
