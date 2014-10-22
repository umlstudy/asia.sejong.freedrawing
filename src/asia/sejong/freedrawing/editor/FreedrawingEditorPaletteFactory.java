package asia.sejong.freedrawing.editor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import asia.sejong.freedrawing.figures.FDConnectionFigure;
import asia.sejong.freedrawing.figures.FDRectangleFigure;
import asia.sejong.freedrawing.model.FDConnection;
import asia.sejong.freedrawing.model.FDNode;

public class FreedrawingEditorPaletteFactory {

	private static final ImageDescriptor RECTANGLE_IMAGE_DESCRIPTOR = ImageDescriptor.createFromImage(FDRectangleFigure.RECTANGLE_IMAGE);
	private static final ImageDescriptor CONNECTION_IMAGE_DESCRIPTOR = ImageDescriptor.createFromImage(FDConnectionFigure.CONNECTION_IMAGE);
	
	public static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createElementsDrawer());
		return palette;
	}

	/**
	 * 커몬툴을 포함하는 툴바 생성(팔레트의 최 상단에 표시됨)
	 * 디폴트 툴로 셀렉션 툴 선택
	 */
	private static PaletteEntry createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
	
		// 셀렉션툴을 그룹에 추가하고 디톨트 선택으로 함
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);
	
		// 범위 선택툴을 추가
		toolbar.add(new MarqueeToolEntry());
		
		return toolbar;
	}

	/**
	 * Create a drawer containing tools to add the various genealogy model elements
	 */
	private static PaletteEntry createElementsDrawer() {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Elements");
		
		// 사각형 생성을 위한 팩토리 및 툴 생성
		{
			SimpleFactory factory = new SimpleFactory(FDNode.class) {
				public Object getNewObject() {
					FDNode node = new FDNode();
					return node;
				}
			};
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
				"사각형",
				"사각형 추가",
				factory, 
				RECTANGLE_IMAGE_DESCRIPTOR, 
				RECTANGLE_IMAGE_DESCRIPTOR);
			componentsDrawer.add(component);
		}

		// 커넥션 생성을 위한 팩토리 및 툴 생성
		{
			SimpleFactory factory = new SimpleFactory(FDConnection.class) {
				public Object getNewObject() {
					FDConnection connection = new FDConnection();
					return connection;
				}
			};
			ToolEntry connection = new ConnectionCreationToolEntry(
					"Connection",
					"Create a connection", 
					factory,
					CONNECTION_IMAGE_DESCRIPTOR,
					CONNECTION_IMAGE_DESCRIPTOR);
			connection.setToolClass(FDConnectionCreationTool.class);
			componentsDrawer.add(connection);
		}
		
		return componentsDrawer;
	}
}
