package asia.sejong.freedrawing.parts.FDImageEditPart;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Image;

import asia.sejong.freedrawing.context.ApplicationContext;
import asia.sejong.freedrawing.figures.FDImageFigure;
import asia.sejong.freedrawing.model.FDImage;
import asia.sejong.freedrawing.model.listener.FDImageListener;
import asia.sejong.freedrawing.parts.FDShapeEditPart.FDShapeEditPart;

public class FDImageEditPart extends FDShapeEditPart implements FDImageListener {
	
	public FDImageEditPart(FDImage element) {
		setModel(element);
		setImage(extractImage(element.getImageBytes()));
	}
	
	private Image extractImage(byte[] imageBytes) {
		return ApplicationContext.getInstance().getImageManager().get(imageBytes);
	}
	
	public void setParent(EditPart parent) {
		super.setParent(parent);
		
		if ( parent != null ) {
			// 복수개의 모델에서 하나의 이미지 모델을 사용토록 하기 위해
			// FDRoot에 저장한 후 이를 참조한다.
			FDImage model = (FDImage)getModel();
			byte[] savedImageBytes = getRootModel().getSavedImageBytes(model.getImageBytes());
			model.setImageBytes(savedImageBytes);
		}
	}
	
	private void setImage(Image image) {
		Assert.isNotNull(image);
		
		FDImageFigure figure = (FDImageFigure)getFigure();
		figure.setImage(image);
		figure.setVisible(true);
	}
	
	protected void refreshVisuals() {
		FDImage m = (FDImage) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		
//		Rectangle clientBounds = bounds;
//		FDImageFigure figure = (FDImageFigure)getFigure();
//		GC drawer = new GC(figure.getImage());
//		SWTGraphics graphics = new SWTGraphics(drawer);
//		graphics.translate(-clientBounds.x, -clientBounds.y);
//		figure.paint(graphics);
//		drawer.dispose();
//		getFigure().setBounds(clientBounds);
////		((ImageFigure)getFigure()).setImage(image);
//		getFigure().invalidate();
//		getFigure().repaint();
		
		super.refreshVisuals();
	}
	
	protected FDImageFigure getImageFigure() {
		return (FDImageFigure) getFigure();
	}

	//============================================================
	// FDImageListener
	
	@Override
	public void imageChanged(byte[] imageBytes) {
		setImage(extractImage(imageBytes));
		
		refreshVisuals();
	}
}