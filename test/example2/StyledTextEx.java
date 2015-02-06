package example2;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

public class StyledTextEx extends StyledText {
	public StyledTextEx(Composite parent, int style) {
		super(parent, style);
	}

	public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY) {
		gc.setXORMode(true);
		super.drawBackground(gc, offsetX, offsetY, width, height, offsetX, offsetY);
//		gc.setAlpha(255);
	}
}
