package asia.sejong.freedrawing.editor.actions.etc;

import org.eclipse.jface.action.Action;

import asia.sejong.freedrawing.editor.FreedrawingEditor;

public class ChangeEditorScaleAction extends Action {
	
	private int zoomLevelIndex;
	private FreedrawingEditor editor;

	public ChangeEditorScaleAction(FreedrawingEditor editor) {
		this.editor = editor;
	}
	
	public void run() {
		editor.scaleChanged(zoomLevelIndex);
	}

	public void setZoomLevelIndex(int index) {
		this.zoomLevelIndex = index;
	}
}
