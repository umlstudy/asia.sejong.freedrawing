package asia.sejong.freedrawing.editor.actions.palette;

import org.eclipse.gef.Tool;

import asia.sejong.freedrawing.resources.IconManager.IconType;

public interface PaletteChangeListener {

	void iconChange(Tool tool, IconType type);
	Tool getTool();
}
