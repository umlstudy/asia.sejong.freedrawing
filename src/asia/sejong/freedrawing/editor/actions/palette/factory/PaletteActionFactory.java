package asia.sejong.freedrawing.editor.actions.palette.factory;

import org.eclipse.gef.tools.AbstractTool;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.editor.actions.common.LocalActionFactory;
import asia.sejong.freedrawing.editor.actions.palette.PaletteAction;
import asia.sejong.freedrawing.editor.tools.FDToolFactory;
import asia.sejong.freedrawing.resources.ContextManager;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public abstract class PaletteActionFactory extends LocalActionFactory {

	public static final PaletteActionFactory SELECT_PANNING = new PaletteActionFactory("SELECT_PANNING") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.PANNING_SELECTION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getIconManager().getSelectImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getSelectImageDescriptor(IconType.NORMAL));
			action.setText("선택");
			
			return action;
		}
	};
	
	public static final PaletteActionFactory SELECT_MARQUEE = new PaletteActionFactory("SELECT_MARQUEE") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.MARQUEE_SELECTION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getIconManager().getMarqueeImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getMarqueeImageDescriptor(IconType.NORMAL));
			action.setText("마큐");
			
			return action;
		}
	};
	
	public static final PaletteActionFactory CREATE_RECTANGLE = new PaletteActionFactory("CREATE_RECTANGLE") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.RECT_CREATION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getIconManager().getRectangleImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getRectangleImageDescriptor(IconType.NORMAL));
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
	
	public static final PaletteActionFactory CREATE_IMAGE = new PaletteActionFactory("CREATE_IMAGE") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.IMAGE_CREATION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, null);
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(null);
			action.setText("이미지");
			
			return action;
		}
	};
	
	public static final PaletteActionFactory CREATE_LABEL = new PaletteActionFactory("CREATE_LABEL") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.LABEL_CREATION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, null);
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(null);
			action.setText("라벨");
			
			return action;
		}
	};
	
	public static final PaletteActionFactory CREATE_CONNECTION = new PaletteActionFactory("CREATE_CONNECTION") {
		public PaletteAction create(FreedrawingEditor editor) {
			AbstractTool tool = FDToolFactory.CONNECTION_CREATION_TOOL.createTool(editor);
			PaletteAction action = new PaletteAction(editor, ContextManager.getInstance().getIconManager().getConnectionImages());
			action.setId(getId());
			action.setTool(tool);
			action.setImageDescriptor(ContextManager.getInstance().getIconManager().getConnectionImageDescriptor(IconType.NORMAL));
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
