package example2;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
 
public class draw2d_test {
 
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
 
		LightweightSystem lwSystem = new LightweightSystem(shell);
 
		IFigure contents = new org.eclipse.draw2d.Figure();
		LineBorder border = new LineBorder();
		border.setWidth(5);
		contents.setBorder(border);
 
		XYLayout layout = new XYLayout();
		contents.setLayoutManager(layout);
 
//		Label label = new Label("Hello world!!!");
//		contents.add(label);
		
		final RectangleFigure label1 = new RectangleFigure2("1");
		label1.setBounds(new Rectangle(10, 10, 200, 200) );
		LineBorder borderL1 = new LineBorder();
		label1.setBorder(borderL1);
		label1.setBackgroundColor(ColorConstants.black);
		contents.add(label1);
	 
		final RectangleFigure label2 = new RectangleFigure2("2");
		label2.setBounds(new Rectangle(160, 160, 100, 100));
		label2.setBackgroundColor(ColorConstants.green);
		contents.add(label2);
	 
 
		Button btn = new Button("Test");
		btn.setBounds(new Rectangle(10, 250, 50,20));
		contents.add(btn);
		btn.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClicked(MouseEvent arg0) {
				System.out.println("BBB");
				
				mousedbl(label1, label2);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				//System.out.println("AAAAAAAAAAAAAAAAA");
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.out.println("CC");
				
				mousepress(label1, label2);
			}

		});
		 
		shell.open();
 
		lwSystem.setContents(contents);
		while(!shell.isDisposed() )
		{
			if(!display.readAndDispatch() )
			{
				display.sleep();
			}
		}
 
		display.dispose();
	}
	
	private static void mousepress(RectangleFigure label1, RectangleFigure label2) {
		//label2.repaint(new Rectangle(160,160, 20,20));
		label1.repaint();
		//label2.revalidate();
		//label2.repaint();
	}
	
	private static void mousedbl(RectangleFigure label1, RectangleFigure label2) {
//		label2.erase();
//		label2.translate(-155, -155);
//		label2.repaint();
		//(new Rectangle(160,160, 20,20));
		//label2.revalidate();
		//label2.repaint();
		label1.invalidate();
		//label1.update();
		label2.invalidateTree();
		label2.intersects(new Rectangle(10,10,20,20));
	}
 
 
}