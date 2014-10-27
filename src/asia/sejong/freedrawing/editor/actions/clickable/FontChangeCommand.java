package asia.sejong.freedrawing.editor.actions.clickable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.TextObject;

public class FontChangeCommand extends Command {

	private List<TextObject> textObjects;
	private FontInfo newFontInfo;
	private Map<TextObject, FontInfo> oldFonts;
	
	public FontChangeCommand(List<TextObject> textObjects, FontInfo newFontInfo ) {
		this.textObjects = textObjects;
		this.newFontInfo = newFontInfo;
		this.oldFonts = new HashMap<TextObject, FontInfo>();
	}
	
	public void execute() {
		oldFonts.clear();
		for ( TextObject item : textObjects ) {
			oldFonts.put(item, item.getFontInfo());
			if ( !newFontInfo.equals(item.getFontInfo()) ) {
				item.setFontInfo(newFontInfo);
			}
		}
	}
	
	public void undo() {
		for ( TextObject item : textObjects ) {
			FontInfo oldFont = oldFonts.get(item);
			if ( !item.getFontInfo().equals(oldFont) ) {
				item.setFontInfo(oldFont);
			}
		}
	}
}
