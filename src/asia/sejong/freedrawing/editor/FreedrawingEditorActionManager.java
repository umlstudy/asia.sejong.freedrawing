package asia.sejong.freedrawing.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import asia.sejong.freedrawing.editor.actions.FreedrawingActionFactory;
import asia.sejong.freedrawing.editor.actions.selection.CopyToClipboardAction;
import asia.sejong.freedrawing.editor.actions.selection.PasteFromClipboardAction;
import asia.sejong.freedrawing.editor.actions.toggle.ToggleActionGroup;
import asia.sejong.freedrawing.editor.tools.FDPanningSelectionTool;
import asia.sejong.freedrawing.editor.tools.FreedrawingToolFactory;
import asia.sejong.freedrawing.parts.FDNodeEditPart.FDNodeEditPart;

public class FreedrawingEditorActionManager {
	
	// Editor Properties - Do not dispose
	private ActionRegistry actionRegistry;
	private FreedrawingEditDomain editDomain;

	// ActionManager Properties
	private ToolBarManager toolbarManager;
	private MenuManager contextMenuManger;
	
	private EditPart targetEditPart;
	
	private FreedrawingEditorActionManager(ActionRegistry actionRegistry, FreedrawingEditDomain editDomain) {
		this.setActionRegistry(actionRegistry);
		this.editDomain = editDomain;
	}

	static FreedrawingEditorActionManager newInstance(ActionRegistry actionRegistry, FreedrawingEditDomain editDomain) {
		return new FreedrawingEditorActionManager(actionRegistry, editDomain);
	}

	ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	void setActionRegistry(ActionRegistry actionRegistry) {
		this.actionRegistry = actionRegistry;
	}

	void createActions(FreedrawingEditor editor, List<Object> selectionActions) {
		ActionRegistry registry = getActionRegistry();
		IAction action;
		
		// Create ToolBar Actions
		ToggleActionGroup actionGroup = new ToggleActionGroup(editDomain);
		editDomain.setToggleActionGroup(actionGroup);
		
		// EDIT_TEXT
		action = FreedrawingActionFactory.EDIT_TEXT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// PALETTE TOOLS
		AbstractTool tool;
		// PANNING_SELECTION
		tool = FreedrawingToolFactory.PANNING_SELECTION_TOOL.createTool(editor);
		action = FreedrawingActionFactory.TOGGLE_PANNING.create(actionGroup, tool);
		registry.registerAction(action);
		// set default tool
		editDomain.setDefaultTool(tool);
		
		// MARQUEE_SELECTION
		tool = FreedrawingToolFactory.MARQUEE_SELECTION_TOOL.createTool(editor);
		action = FreedrawingActionFactory.TOGGLE_MARQUEE.create(actionGroup, tool);
		registry.registerAction(action);
		
		// RECTANGLE_SELECTION
		tool = FreedrawingToolFactory.NODE_CREATION_TOOL.createTool(editor);
		action = FreedrawingActionFactory.TOGGLE_RECTANGLE.create(actionGroup, tool);
		registry.registerAction(action);
		
		// CONNECTION_SELECTION
		tool = FreedrawingToolFactory.CONNECTION_CREATION_TOOL.createTool(editor);
		action = FreedrawingActionFactory.TOGGLE_CONNECTION.create(actionGroup, tool);
		registry.registerAction(action);
		
		// FONT_PICK
		action = FreedrawingActionFactory.FONT_PICK.create(editor, editDomain);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// COLOR_PICK
		action = FreedrawingActionFactory.COLOR_PICK.create(editor, editDomain);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// COPY
		action = new CopyToClipboardAction(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// PASTE
		action = new PasteFromClipboardAction(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_LEFT
		action = FreedrawingActionFactory.MOVE_LEFT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_RIGHT
		action = FreedrawingActionFactory.MOVE_RIGHT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_DOWN
		action = FreedrawingActionFactory.MOVE_DOWN.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_UP
		action = FreedrawingActionFactory.MOVE_UP.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// ZORDER_TO_FRONT
		action = FreedrawingActionFactory.ZORDER_TO_FRONT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// ZORDER_TO_BACK
		action = FreedrawingActionFactory.ZORDER_TO_BACK.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
//		// MOVE_LEFT_PRESSED
//		action = FreedrawingActionFactory.MOVE_LEFT_PRESSED.create(this);
//		registry.registerAction(action);
//		getSelectionActions().add(action.getId());
	}
	
	void createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).create());
		
		// add actions to ToolBar
		toolbarManager = new ToolBarManager(toolbar);
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.TOGGLE_PANNING.getId()));
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.TOGGLE_MARQUEE.getId()));
		toolbarManager.add(new Separator());
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.TOGGLE_RECTANGLE.getId()));
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.TOGGLE_CONNECTION.getId()));
		toolbarManager.add(new Separator());
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.FONT_PICK.getId()));
		toolbarManager.add(getActionRegistry().getAction(FreedrawingActionFactory.COLOR_PICK.getId()));
		
		toolbarManager.add(new Separator());
		SubToolBarManager subMenuManager = new SubToolBarManager(toolbarManager);

		toolbarManager.add(new Separator());
		subMenuManager.add(getActionRegistry().getAction(FreedrawingActionFactory.COLOR_PICK.getId()));
		subMenuManager.add(getActionRegistry().getAction(FreedrawingActionFactory.COLOR_PICK.getId()));
		subMenuManager.add(getActionRegistry().getAction(FreedrawingActionFactory.COLOR_PICK.getId()));
		toolbarManager.update(true);
		subMenuManager.setVisible(true);
	}
	
	void dispose() {
		if ( toolbarManager != null ) {
			toolbarManager.dispose();
		}
		
		if ( contextMenuManger != null ) {
			contextMenuManger.dispose();
		}
	}

	void createContextMenuManager(GraphicalViewer viewer) {
		contextMenuManger = new MenuManager("FreedrawingEditorPopup");
		contextMenuManger.setRemoveAllWhenShown(true);
		viewer.setContextMenu(contextMenuManger);
		contextMenuManger.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				resetContextMenu();
			}
		});
	}
	
	/**
	 * reset context menu
	 */
	private void resetContextMenu() {
		if ( editDomain.getActiveTool() instanceof FDPanningSelectionTool ) {
			if ( targetEditPart instanceof FDNodeEditPart ) {
				contextMenuManger.add(getActionRegistry().getAction(FreedrawingActionFactory.FONT_PICK.getId()));
				contextMenuManger.add(getActionRegistry().getAction(FreedrawingActionFactory.ZORDER_TO_FRONT.getId()));
				contextMenuManger.add(getActionRegistry().getAction(FreedrawingActionFactory.ZORDER_TO_BACK.getId()));
			} else {
				contextMenuManger.add(getActionRegistry().getAction(FreedrawingActionFactory.TOGGLE_RECTANGLE.getId()));
			}
		}
	}
	
	void setTargetEditPart(EditPart targetEditPart) {
		this.targetEditPart = targetEditPart;
	}

	void initializeKeyHandler(GraphicalViewer viewer) {
		
		GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer) {
			protected List<?> getNavigationSiblings() {
				// disable default key arrow action
				return new ArrayList<Object>();
			}
		};
		viewer.setKeyHandler(keyHandler);
		
		ActionRegistry registry = getActionRegistry();
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_LEFT, 0), registry.getAction(FreedrawingActionFactory.MOVE_LEFT.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_RIGHT, 0), registry.getAction(FreedrawingActionFactory.MOVE_RIGHT.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_DOWN, 0), registry.getAction(FreedrawingActionFactory.MOVE_DOWN.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_UP, 0), registry.getAction(FreedrawingActionFactory.MOVE_UP.getId()));
	}
}
