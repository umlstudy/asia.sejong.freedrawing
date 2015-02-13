package asia.sejong.freedrawing.draw2d;

import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import asia.sejong.freedrawing.draw2d.figures.FDShapeFigure;
import asia.sejong.freedrawing.draw2d.figures.GeometryUtil;

public class ContainerClippingStrategy implements IClippingStrategy {
	
	public static final ContainerClippingStrategy INSTANCE = new ContainerClippingStrategy();
	
	private ContainerClippingStrategy() {}
	
	@Override
	public Rectangle[] getClip(IFigure childFigure) {
		if ( childFigure instanceof FDShapeFigure ) {
			FDShapeFigure sf = (FDShapeFigure)childFigure;
			if ( sf.getDegreeEx() != 0 ) {
				return new Rectangle[] { GeometryUtil.createSquare(sf.getBounds()) };
			}
		}
		return new Rectangle[] { childFigure.getBounds(),};
	}
}
