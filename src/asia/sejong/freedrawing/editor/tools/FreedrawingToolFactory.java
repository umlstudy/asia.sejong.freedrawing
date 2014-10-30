package asia.sejong.freedrawing.editor.tools;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDNode;

public abstract class FreedrawingToolFactory {

	public static FreedrawingToolFactory CONNECTION_CREATION_TOOL = new FreedrawingToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDConnectionCreationTool(editor);
		}
	};
	
	public static FreedrawingToolFactory PANNING_SELECTION_TOOL = new FreedrawingToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDPanningSelectionTool(editor);
		}
	};
	
	public static FreedrawingToolFactory MARQUEE_SELECTION_TOOL = new FreedrawingToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new MarqueeSelectionTool();
		}
	};

	public static FreedrawingToolFactory NODE_CREATION_TOOL = new FreedrawingToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
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
	};

	public abstract AbstractTool createTool(FreedrawingEditor editor);
}
