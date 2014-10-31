package asia.sejong.freedrawing.editor.tools;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.tools.MarqueeSelectionTool;

public class FDMarqueeSelectionTool extends MarqueeSelectionTool {

	/**
	 * 오버라이드 하지 않으면 마우스 커서가 뷰 영역을 벗어났다 복귀할 경우,
	 * 툴이 선택되지 않는다. 버그인듯?
	 * @see org.eclipse.gef.tools.MarqueeSelectionTool#isViewerImportant(org.eclipse.gef.EditPartViewer)
	 */
	protected boolean isViewerImportant(EditPartViewer viewer) {
		if ( viewer instanceof GraphicalViewer ) {
			if ( viewer != getCurrentViewer() ) {
				setViewer(viewer);
			}
			return true;
		}
		return false;
	}
}
