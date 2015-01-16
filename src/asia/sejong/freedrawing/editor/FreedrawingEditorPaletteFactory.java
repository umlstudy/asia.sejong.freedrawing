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

import asia.sejong.freedrawing.editor.tools.FDConnectionCreationTool;
import asia.sejong.freedrawing.editor.tools.FDPanningSelectionTool;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDModelFactory;
import asia.sejong.freedrawing.model.FDRect;
import asia.sejong.freedrawing.resources.IconManager;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public class FreedrawingEditorPaletteFactory {

	public static PaletteRoot createPalette(IconManager imageManager) {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createElementsDrawer(imageManager));
		return palette;
	}

	/**
	 * 커몬툴을 포함하는 툴바 생성(팔레트의 최 상단에 표시됨)
	 * 디폴트 툴로 셀렉션 툴 선택
	 */
	private static PaletteEntry createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
	
		// 셀렉션툴을 그룹에 추가하고 디톨트 선택으로 함
		ToolEntry selectionToolEntry = new PanningSelectionToolEntry();
		selectionToolEntry.setToolClass(FDPanningSelectionTool.class);
		toolbar.add(selectionToolEntry);
		palette.setDefaultEntry(selectionToolEntry);
	
		// 범위 선택툴을 추가
		toolbar.add(new MarqueeToolEntry());
		
		return toolbar;
	}

	/**
	 * Create a drawer containing tools to add the various model elements
	 */
	private static PaletteEntry createElementsDrawer(IconManager imageManager) {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Elements");
		
		// 사각형 생성을 위한 팩토리 및 툴 생성
		{
			SimpleFactory factory = new SimpleFactory(FDRect.class) {
				public Object getNewObject() {
					FDElement node = FDModelFactory.createModel(FDRect.class);
					return node;
				}
			};
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
				"사각형",
				"사각형 추가",
				factory, 
				imageManager.getRectangleImageDescriptor(IconType.NORMAL), 
				imageManager.getRectangleImageDescriptor(IconType.NORMAL));
			componentsDrawer.add(component);
		}

		// 커넥션 생성을 위한 팩토리 및 툴 생성
		{
			ToolEntry connection = new ConnectionCreationToolEntry(
					"Connection",
					"Create a connection", 
					null,
					imageManager.getConnectionImageDescriptor(IconType.NORMAL),
					imageManager.getConnectionImageDescriptor(IconType.NORMAL));
			connection.setToolClass(FDConnectionCreationTool.class);
			componentsDrawer.add(connection);
		}
		
		return componentsDrawer;
	}
}
