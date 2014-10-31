package asia.sejong.freedrawing.editor;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.editor.actions.selection.SelectableActionGroup;

public class FreedrawingEditDomain extends DefaultEditDomain {
	
	private SelectableActionGroup selectableActionGroup;

	public FreedrawingEditDomain(IEditorPart editorPart) {
		super(editorPart);
	}

	public SelectableActionGroup getPaletteActionGroup() {
		return selectableActionGroup;
	}

	void setSelectableActionGroup(SelectableActionGroup selectableActionGroup) {
		this.selectableActionGroup = selectableActionGroup;
	}
	
	public void focusGained(FocusEvent event, EditPartViewer viewer) {
		super.focusGained(event, viewer);
		System.out.println("FOCUS GAINED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	
}
