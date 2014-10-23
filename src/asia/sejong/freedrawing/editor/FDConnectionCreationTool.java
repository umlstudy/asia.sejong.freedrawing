package asia.sejong.freedrawing.editor;

import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.gef.ui.palette.PaletteViewer;

public class FDConnectionCreationTool extends ConnectionCreationTool {

	protected boolean handleButtonDown(int button) {
		System.out.println("llllllllllllllllll");
		
		if ( button == 3 ) {
			PaletteViewer paletteViewer = getDomain().getPaletteViewer();
			paletteViewer.setActiveTool(paletteViewer.getPaletteRoot().getDefaultEntry());
		}
		return super.handleButtonDown(button);
	}
}
