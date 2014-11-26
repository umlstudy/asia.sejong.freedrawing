package asia.sejong.freedrawing.parts.FDNodeEditPart.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.model.FDTextShape;

public class FontChangeCommand extends Command {

	private List<FDTextShape> textObjects;
	private FontInfo newFontInfo;
	private Map<FDTextShape, FontInfo> oldFonts;
	
	public FontChangeCommand(List<FDTextShape> textObjects, FontInfo newFontInfo ) {
		this.textObjects = textObjects;
		this.newFontInfo = newFontInfo;
		this.oldFonts = new HashMap<FDTextShape, FontInfo>();
	}
	
	public void execute() {
		oldFonts.clear();
		for ( FDTextShape item : textObjects ) {
			oldFonts.put(item, item.getFontInfo());
			if ( !newFontInfo.equals(item.getFontInfo()) ) {
				item.setFontInfo(newFontInfo);
			}
		}
	}
	
	public void undo() {
		for ( FDTextShape item : textObjects ) {
			FontInfo oldFont = oldFonts.get(item);
			if ( !item.getFontInfo().equals(oldFont) ) {
				item.setFontInfo(oldFont);
			}
		}
	}
}
