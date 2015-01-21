package example2;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

public class RectangleFigure2 extends RectangleFigure {
	
	String id;
	
	public RectangleFigure2(String id) {
		this.id = id;
	}

	public void paintFigure(Graphics g) {
		Rectangle bounds = new Rectangle();
		g.getClip(bounds);
		System.out.println(String.format("- Clip id(%s):%s", id, bounds.toString()));
		super.paintFigure(g);
	}
	
	public void erase() {
		System.out.println(String.format("- Erase id(%s):%s", id, bounds.toString()));
		super.erase();
	}
	
//	public void translate(int x, int y ) {
//		super.translate(x, y);
//	}
}
