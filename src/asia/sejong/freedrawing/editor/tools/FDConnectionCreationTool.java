package asia.sejong.freedrawing.editor.tools;

import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.gef.ui.palette.PaletteViewer;

import asia.sejong.freedrawing.editor.FreedrawingEditDomain;

public class FDConnectionCreationTool extends ConnectionCreationTool {

	protected boolean handleButtonDown(int button) {
		System.out.println("llllllllllllllllll");
		
		if ( button == 3 ) {
			
			((FreedrawingEditDomain)getDomain()).getPaletteActionGroup().selectDefaultAction();

			PaletteViewer paletteViewer = getDomain().getPaletteViewer();
			paletteViewer.setActiveTool(paletteViewer.getPaletteRoot().getDefaultEntry());
		}
		return super.handleButtonDown(button);
	}
}
