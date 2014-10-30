package asia.sejong.freedrawing.editor.actions.selection;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.CreationTool;

import asia.sejong.freedrawing.model.FDNode;

public class SelectRectangleAction extends GroupMemberAction {

	@Override
	protected AbstractTool createTool() {
		return new CreationTool(new CreationFactory() {
			
			@Override
			public Object getObjectType() {
				return FDNode.class;
			}
			
			@Override
			public Object getNewObject() {
				return new FDNode();
			}
		});
	}
}
