package asia.sejong.freedrawing.editor;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.Tool;
import org.eclipse.ui.IEditorPart;


public class FreedrawingEditDomain extends DefaultEditDomain {
	
	private FreedrawingEditDomainListener editDomainListener;

	public FreedrawingEditDomain(IEditorPart editorPart) {
		super(editorPart);
	}

	public void setActiveTool(Tool tool) {
		super.setActiveTool(tool);
		if ( getEditDomainListener() != null ) {
			getEditDomainListener().activeToolChanged(tool);
		}
	}

	public FreedrawingEditDomainListener getEditDomainListener() {
		return editDomainListener;
	}

	public void setEditDomainListener(FreedrawingEditDomainListener editDomainListener) {
		this.editDomainListener = editDomainListener;
	}
}
