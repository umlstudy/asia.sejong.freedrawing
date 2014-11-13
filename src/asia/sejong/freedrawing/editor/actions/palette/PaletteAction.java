package asia.sejong.freedrawing.editor.actions.palette;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Tool;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public class PaletteAction extends Action implements PaletteIconChangable {
	
	private AbstractTool tool;
	
	private PaletteActionChangeListener paletteActionChangeListener;
	
	private FreedrawingEditor editor;
	
	private Image[] icons;
	private IconType type;
	
	public PaletteAction(FreedrawingEditor editor, Image[] icons) {
		super("", AS_PUSH_BUTTON);
		this.setEditor(editor);
		this.icons = icons;
	}
	
	public void run() {
		GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
		EditDomain editDomain = viewer.getEditDomain();
		
		// 툴 선택
		editDomain.setActiveTool(getTool());
		
		// 토글러액션
		if ( paletteActionChangeListener != null ) {
			paletteActionChangeListener.actionChanged(this);
		}
	}
	
	public AbstractTool getTool() {
		return tool;
	}

	public void setTool(AbstractTool tool) {
		this.tool = tool;
	}

	private void setEditor(FreedrawingEditor editor) {
		this.editor = editor;
	}

	public void setPaletteActionChangeListener(PaletteActionChangeListener paletteActionChangeListener) {
		this.paletteActionChangeListener = paletteActionChangeListener;
	}

	@Override
	public void iconChange(Tool tool, IconType newType) {
		if ( type != newType ) {
			type = newType;
			setImageDescriptor(ImageDescriptor.createFromImage(getIcon(type)));
		}
	}

	Image getIcon(IconType type) {
		return icons[type.ordinal()];
	}
}