package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.List;

public class FDGroupShape extends FDShape implements FDContainer {

	private static final long serialVersionUID = 9107102127386072200L;
	
	private List<FDShape> shapes;

	public List<FDShape> getShapes() {
		return shapes;
	}

	public void setShapes(List<FDShape> shapes) {
		this.shapes = shapes;
	}
	

	@Override
	public void addShape(FDShape target) {
		throw new RuntimeException();
	}

	@Override
	public void removeShape(FDShape target) {
		throw new RuntimeException();
	}

	@Override
	public int changePosition(int position, FDShape target) {
		throw new RuntimeException();
	}
	
	//============================================================
	// Clonable
	@Override
	public FDGroupShape clone() {
		FDGroupShape object = (FDGroupShape)super.clone();
		
		List<FDShape> newShapes = new ArrayList<FDShape>();
		for ( FDShape shape : shapes ) {
			newShapes.add(shape.clone());
		}
		object.setShapes(newShapes);
		
		return object;
	}
}
