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
import asia.sejong.freedrawing.model.FDNode;
import asia.sejong.freedrawing.resources.ImageManager;

public class FreedrawingEditorPaletteFactory {

	public static PaletteRoot createPalette(ImageManager imageManager) {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createElementsDrawer(imageManager));
		return palette;
	}

	/**
	 * Ŀ������ �����ϴ� ���� ����(�ȷ�Ʈ�� �� ��ܿ� ǥ�õ�)
	 * ����Ʈ ���� ������ �� ����
	 */
	private static PaletteEntry createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
	
		// ���������� �׷쿡 �߰��ϰ� ����Ʈ �������� ��
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);
	
		// ���� �������� �߰�
		toolbar.add(new MarqueeToolEntry());
		
		return toolbar;
	}

	/**
	 * Create a drawer containing tools to add the various genealogy model elements
	 */
	private static PaletteEntry createElementsDrawer(ImageManager imageManager) {
		PaletteDrawer componentsDrawer = new PaletteDrawer("Elements");
		
		// �簢�� ������ ���� ���丮 �� �� ����
		{
			SimpleFactory factory = new SimpleFactory(FDNode.class) {
				public Object getNewObject() {
					FDNode node = new FDNode();
					return node;
				}
			};
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
				"�簢��",
				"�簢�� �߰�",
				factory, 
				imageManager.getRectangleImageDescriptor(), 
				imageManager.getRectangleImageDescriptor());
			componentsDrawer.add(component);
		}

		// Ŀ�ؼ� ������ ���� ���丮 �� �� ����
		{
			ToolEntry connection = new ConnectionCreationToolEntry(
					"Connection",
					"Create a connection", 
					null,
					imageManager.getConnectionImageDescriptor(),
					imageManager.getConnectionImageDescriptor());
			connection.setToolClass(FDConnectionCreationTool.class);
			componentsDrawer.add(connection);
		}
		
		return componentsDrawer;
	}
}
