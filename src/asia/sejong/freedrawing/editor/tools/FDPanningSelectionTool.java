package asia.sejong.freedrawing.editor.tools;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.SelectionTool;

import asia.sejong.freedrawing.editor.FreedrawingEditor;

public class FDPanningSelectionTool extends SelectionTool {
	
	private FreedrawingEditor freedrawingEditor;
	
	public FDPanningSelectionTool(FreedrawingEditor freedrawingEditor) {
		this.freedrawingEditor = freedrawingEditor;
	}

	protected boolean handleButtonDown(int button) {
//		
//		if ( button == 3 ) {
//			if ( freedrawingEditor != null ) {
//				freedrawingEditor.setSelectedEditPart(getTargetEditPart());
//			}
//		}
		return super.handleButtonDown(button);
	}
	
	protected void setTargetEditPart(EditPart editpart) {
		super.setTargetEditPart(editpart);
		if ( freedrawingEditor != null ) {
			freedrawingEditor.setTargetEditPart(getTargetEditPart());
		}
	}
}
