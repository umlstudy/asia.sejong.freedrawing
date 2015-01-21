package asia.sejong.freedrawing.editor.actions.selection;

import java.util.List;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDTextShape;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.parts.FDTextShapeEditPart.command.FontChangeCommand;

public class ChangeFontAction extends ElementSelectionAction<FDTextShape> {
	
	private FontInfo fontInfo;

	public ChangeFontAction(FreedrawingEditor part) {
		super(part);
	}
	
	public void run() {
		
		if ( fontInfo != null ) {
			
			List<FDTextShape> lists = getElements(FDTextShape.class);

			if (lists.size()>0) {
				execute(new FontChangeCommand(lists, fontInfo));
			} else {
				// TODO change color
			}
		}
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
		getEditorContext().setFontInfo(fontInfo);
	}
	
	public FontInfo getFontInfo() {
		return fontInfo;
	}
	
//	@Override
//	protected FDTextShape filter(Object model) {
//		if ( model instanceof FDTextShape ) {
//			return (FDTextShape)model;
//		}
//		return null;
//	}
}
