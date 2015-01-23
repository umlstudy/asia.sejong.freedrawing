package asia.sejong.freedrawing.model;

import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.context.FreedrawingEditorContext;
import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.util.UIUtil;

public class FDModelFactory {

	public static FDElement createModel(Class<? extends FDElement> modelClass) {
		FDElement model = null;
		
		if ( modelClass == FDEllipse.class ) {
			model = new FDEllipse();
		} else if ( modelClass == FDImage.class ) {
			model = new FDImage();
		} else if ( modelClass == FDLabel.class ) {
			model = new FDLabel();
		} else if ( modelClass == FDRect.class ) {
			model = new FDRect();
		} else if ( modelClass == FDPolygon.class ) {
			model = new FDPolygon();
		} else {
			throw new RuntimeException(modelClass.getName());
		}
		
		setDefaultValues(model);
		
		return model;
	}
	
	private static void setDefaultValues(FDElement model) {
		
		FreedrawingEditorContext editorContext = null; 
		IEditorPart editor = UIUtil.getActiveEditor();
		if ( editor instanceof FreedrawingEditor ) {
			editorContext = ((FreedrawingEditor)editor).getEditorContext();
		}
		
		if ( editorContext == null ) {
			editorContext = new FreedrawingEditorContext();
		}
		
		model.setLineColor(editorContext.getLineColor());
		model.setLineStyle(LineStyle.getLineStyle(editorContext.getLineStyle()));
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
		
		setDefaultValues(model);
		
		return model;
	}
}
