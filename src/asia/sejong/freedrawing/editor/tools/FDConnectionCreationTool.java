package asia.sejong.freedrawing.editor.tools;

import org.eclipse.gef.tools.ConnectionCreationTool;

import asia.sejong.freedrawing.editor.FreedrawingEditor;

public class FDConnectionCreationTool extends ConnectionCreationTool {

	private FreedrawingEditor editor;
	
	public FDConnectionCreationTool(FreedrawingEditor editor) {
		this.editor = editor;
	}

	protected boolean handleButtonDown(int button) {
		
		if ( button == 3 ) {
			
			if ( editor != null ) {
				editor.setActiveTool();
			}
		}
		return super.handleButtonDown(button);
	}
}
