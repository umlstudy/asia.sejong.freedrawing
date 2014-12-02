package asia.sejong.freedrawing.editor.actions;

import org.eclipse.gef.tools.AbstractTool;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.editor.actions.palette.PaletteAction;
import asia.sejong.freedrawing.editor.tools.FDToolFactory;
import asia.sejong.freedrawing.resources.ContextManager;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public abstract class PaletteActionFactory extends LocalActionFactory {

	public static final PaletteActionFactory TOGGLE_PANNING = new PaletteActionFactory("TOGGLE_PANNING") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.PANNING_SELECTION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getImageManager().getSelectImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getSelectImageDescriptor(IconType.NORMAL));
			action.setText("선택");
			
			return action;
		}
	};
	
	public static final PaletteActionFactory TOGGLE_RECTANGLE = new PaletteActionFactory("TOGGLE_RECTANGLE") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.RECT_CREATION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getImageManager().getRectangleImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getRectangleImageDescriptor(IconType.NORMAL));
			action.setText("사각형");
			
			return action;
		}
	};
	
	public static final PaletteActionFactory CREATE_ELLIPSE = new PaletteActionFactory("CREATE_ELLIPSE") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.ELLIPSE_CREATION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, null);
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(null);
			action.setText("원");
			
			return action;
		}
	};

	public static final PaletteActionFactory TOGGLE_MARQUEE = new PaletteActionFactory("TOGGLE_MARQUEE") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.MARQUEE_SELECTION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getImageManager().getMarqueeImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getMarqueeImageDescriptor(IconType.NORMAL));
			action.setText("마큐");
			
			return action;
		}
	};
	
	public static final PaletteActionFactory TOGGLE_CONNECTION = new PaletteActionFactory("TOGGLE_CONNECTION") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.CONNECTION_CREATION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getImageManager().getConnectionImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getImageManager().getConnectionImageDescriptor(IconType.NORMAL));
			action.setText("연결");
			
			return action;
		}
	};

	/**
	 * 생성자
	 * @param actionId
	 */
	protected PaletteActionFactory(String actionId) {
		super(actionId);
	}
	
	/**
	 * 팩토리메소드#2
	 * @param actionGroup
	 * @return
	 */
	public PaletteAction create(FreedrawingEditor editor) {
		throw new RuntimeException();
	}
}
