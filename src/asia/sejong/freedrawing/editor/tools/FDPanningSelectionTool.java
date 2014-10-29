package asia.sejong.freedrawing.editor.tools;

import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.action.MenuManager;

public class FDPanningSelectionTool extends SelectionTool {
	protected boolean handleButtonDown(int button) {
		
		if ( button == 3 ) {
			//getCurrentViewer().getContextMenu()
			System.out.println("right button pressed...");
			
			MenuManager contextMenu = getCurrentViewer().getContextMenu();
//			contextMenu.update(false);
		}
		return super.handleButtonDown(button);
	}
}
