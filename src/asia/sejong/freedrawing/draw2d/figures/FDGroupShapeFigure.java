package asia.sejong.freedrawing.draw2d.figures;

import java.util.List;

import org.eclipse.draw2d.Graphics;

import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDGroupShape;
import asia.sejong.freedrawing.model.FDShape;

public class FDGroupShapeFigure extends FDShapeFigureImpl {

	@Override
	protected void fillShape(Graphics graphics) {
		
	}

	@Override
	protected void outlineShape(Graphics graphics) {
	}
	
	@Override
	public void setModelAttributes(FDElement model_) {
		FDGroupShape model = (FDGroupShape)model_;
		
		super.setModelAttributes(model_);
		
		List<FDShape> shapes = model.getShapes();
	}
}