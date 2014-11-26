package asia.sejong.freedrawing.editor.tools;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.CreationTool;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDRect;

public abstract class FDToolFactory {

	public static FDToolFactory CONNECTION_CREATION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDConnectionCreationTool(editor);
		}
	};
	
	public static FDToolFactory PANNING_SELECTION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDPanningSelectionTool(editor);
		}
	};
	
	public static FDToolFactory MARQUEE_SELECTION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new FDMarqueeSelectionTool();
		}
	};

	public static FDToolFactory NODE_CREATION_TOOL = new FDToolFactory() {
		@Override
		public AbstractTool createTool(FreedrawingEditor editor) {
			return new CreationTool(new CreationFactory() {
				
				@Override
				public Object getObjectType() {
					return FDRect.class;
				}
				
				@Override
				public Object getNewObject() {
					return new FDRect();
				}
			});
		}
	};

	public abstract AbstractTool createTool(FreedrawingEditor editor);
}
