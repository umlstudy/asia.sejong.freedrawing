package asia.sejong.freedrawing.parts.common;

import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FDWire;
import asia.sejong.freedrawing.parts.FDWireEditPart.FDWireEditPart;

public abstract class FDShapeEditPart extends AbstractGraphicalEditPart {

	protected FDWireEditPart findWireEditPart(FDWire model) {
		if (model == null) {
			return null;
		}
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		return (FDWireEditPart) registry.get(model);
	}

	protected final FDRoot getNodeRoot() {
		return (FDRoot)getViewer().getContents().getModel();
		// TODO REMOVE
//		EditPart editPart = this;
//		while ( editPart != null ) {
//			if ( editPart instanceof FDNodeRootEditPart ) {
//				return ((FDNodeRootEditPart)editPart).getModel();
//			} else {
//				editPart = editPart.getParent();
//			}
//		}
//		
//		return null;
	}
	
	// ==========================================================================
	// FDNodeListener

	/**
	 * Update the figure based upon the new model location
	 */
	public void locationChanged(int x, int y) {
		figure.setLocation(new Point(x, y));
		// update parent layout ( any good idea ? ) 
		figure.getParent().getLayoutManager().setConstraint(figure, figure.getBounds());
	}

	/**
	 * Update the figure based upon the new model size
	 */
	public void sizeChanged(int width, int height) {
		getFigure().setSize(width, height);
		// update parent layout ( any good idea ? ) 
		figure.getParent().getLayoutManager().setConstraint(figure, figure.getBounds());
	}
}
