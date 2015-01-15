package asia.sejong.freedrawing.model;

import asia.sejong.freedrawing.context.FreedrawingEditorContext;

public class FDModelFactory {

	public static FDElement createModel(Class<? extends FDElement> modelClass, FreedrawingEditorContext editorContext) {
		FDElement model = null;
		
		if ( modelClass == FDEllipse.class ) {
			model = new FDEllipse();
		} else if ( modelClass == FDImage.class ) {
			model = new FDImage();
		} else if ( modelClass == FDLabel.class ) {
			model = new FDLabel();
		} else if ( modelClass == FDRect.class ) {
			model = new FDRect();
		} else {
			throw new RuntimeException(modelClass.getName());
		}
		
		setDefaultValues(model, editorContext);
		
		return model;
	}
	
	private static void setDefaultValues(FDElement model, FreedrawingEditorContext editorContext) {
		model.setLineColor(editorContext.getLineColor());
		model.setLineStyle(editorContext.getLineStyle());
		model.setLineWidth(editorContext.getLineWidth());
		
		if ( model instanceof FDShape) {
			FDShape shape = (FDShape)model;
			shape.setSize(editorContext.getDefaultWidth(), editorContext.getDefaultHeight());
			shape.setDegree(editorContext.getDegree());
			shape.setAlpha(editorContext.getAlpha());
			shape.setBackgroundColor(editorContext.getBackgroundColor());
		}
		
		if ( model instanceof FDTextShape ) {
			FDTextShape shape = (FDTextShape)model;
			shape.setFontInfo(editorContext.getFontInfo());
			shape.setFontColor(editorContext.getFontColor());
		}
	}

	public static FDWire createWire(FDWireEndPoint src, FDWireEndPoint tar) {
		FDWire model = FDWire.newInstance(src, tar);
		return model;
	}
}
