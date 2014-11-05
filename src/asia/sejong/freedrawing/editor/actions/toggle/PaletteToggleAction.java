package asia.sejong.freedrawing.editor.actions.toggle;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.Action;

public class PaletteToggleAction extends Action {
	
	private DefaultEditDomain editDomain;
	private AbstractTool tool;
	private ToggleActionGroup actionGroup;
	
	public PaletteToggleAction() {
		super("", AS_CHECK_BOX);
	}
	
	public void run() {
		getEditDomain().setActiveTool(getTool());
		
		// 툴에 뷰어 설정
		GraphicalViewer viewer = (GraphicalViewer)((GraphicalEditor)getEditDomain().getEditorPart()).getAdapter(GraphicalViewer.class);
		getTool().setViewer(viewer);
	}

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = (DefaultEditDomain)editDomain;
	}

	public AbstractTool getTool() {
		return tool;
	}

	public void setTool(AbstractTool tool) {
		this.tool = tool;
	}

	public ToggleActionGroup getActionGroup() {
		return actionGroup;
	}

	public void setActionGroup(ToggleActionGroup actionGroup, boolean isDefaultAction) {
		this.actionGroup = actionGroup;
		this.actionGroup.addAction(this, isDefaultAction);
	}
}