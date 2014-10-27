package asia.sejong.freedrawing.editor;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.editor.actions.selection.PaletteSelectionActionGroup;

public class FreedrawingEditDomain extends DefaultEditDomain {
	
	public FreedrawingEditDomain(IEditorPart editorPart) {
		super(editorPart);
		setPaletteActionGroup(paletteActionGroup);
	}

	private PaletteSelectionActionGroup paletteActionGroup;

	public PaletteSelectionActionGroup getPaletteActionGroup() {
		return paletteActionGroup;
	}

	void setPaletteActionGroup(PaletteSelectionActionGroup paletteActionGroup) {
		this.paletteActionGroup = paletteActionGroup;
	}
	
}
