package asia.sejong.freedrawing.editor.actions.clickable;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.ui.IWorkbenchPart;

import asia.sejong.freedrawing.resources.ContextManager;

public class ClickableActionFactory {

	public static ColorPickAction createColorPickAction(ContextManager cm, EditDomain domain, IWorkbenchPart part) {
		return (ColorPickAction)AbstractClickableAction.build(ColorPickAction.class, "Ä®¶ó¼±ÅÃ", cm.getImageManager().getColorPickImageDescriptor(), domain, part);
	}

	public static FontPickAction createFontPickAction(ContextManager cm, DefaultEditDomain domain, IWorkbenchPart part) {
		return (FontPickAction)AbstractClickableAction.build(FontPickAction.class, "Ä®¶ó¼±ÅÃ", cm.getImageManager().getFontPickImageDescriptor(), domain, part);
	}
}
