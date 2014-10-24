package asia.sejong.freedrawing.editor;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.ui.IEditorPart;

import asia.sejong.freedrawing.editor.actions.PaletteActionGroup;

public class FreedrawingEditDomain extends DefaultEditDomain {
	
	public FreedrawingEditDomain(IEditorPart editorPart) {
		super(editorPart);
		setPaletteActionGroup(paletteActionGroup);
	}

	private PaletteActionGroup paletteActionGroup;

	public PaletteActionGroup getPaletteActionGroup() {
		return paletteActionGroup;
	}

	void setPaletteActionGroup(PaletteActionGroup paletteActionGroup) {
		this.paletteActionGroup = paletteActionGroup;
	}
	
}
