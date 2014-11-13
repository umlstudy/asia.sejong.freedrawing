package asia.sejong.freedrawing.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.Tool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import asia.sejong.freedrawing.editor.actions.PaletteActionFactory;
import asia.sejong.freedrawing.editor.actions.PaletteDropDownActionFactory;
import asia.sejong.freedrawing.editor.actions.SelectionActionFactory;
import asia.sejong.freedrawing.editor.actions.palette.PaletteAction;
import asia.sejong.freedrawing.editor.actions.palette.PaletteDropDownAction;
import asia.sejong.freedrawing.editor.actions.selection.CopyToClipboardAction;
import asia.sejong.freedrawing.editor.actions.selection.PasteFromClipboardAction;
import asia.sejong.freedrawing.editor.tools.FDPanningSelectionTool;
import asia.sejong.freedrawing.parts.FDNodeEditPart.FDNodeEditPart;

public class FreedrawingEditorActionManager implements FreedrawingEditDomainListener {

	private FreedrawingEditor editor;
	
	// ActionManager Properties
	private List<PaletteAction> paletteActions;
	private ToolBarManager toolbarManager;
	private MenuManager contextMenuManger;
	
	private EditPart targetEditPart;
	
	private FreedrawingEditorActionManager(FreedrawingEditor editor, List<Object> selectionActions) {
		this.editor = editor;
		this.paletteActions = new ArrayList<PaletteAction>();
		createPaletteActions();
		createSelectionActions(selectionActions);
	}

	static FreedrawingEditorActionManager newInstance(FreedrawingEditor editor, List<Object> selectionActions) {
		return new FreedrawingEditorActionManager(editor, selectionActions);
	}

	private void createPaletteActions() {
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		FreedrawingEditDomain editDomain = editor.getEditDomain();
		PaletteAction action;
		
		editDomain.setEditDomainListener(this);
		// --------------------
		// PaletteActionFactory
		
		// PANNING_SELECTION
		action = PaletteActionFactory.TOGGLE_PANNING.create(editor);
		registry.registerAction(action);
		// set default tool
		editDomain.setDefaultTool(action.getTool());
		paletteActions.add(action);
		
		// MARQUEE_SELECTION
		action = PaletteActionFactory.TOGGLE_MARQUEE.create(editor);
		registry.registerAction(action);
		paletteActions.add(action);

		// RECTANGLE_SELECTION
		action = PaletteActionFactory.TOGGLE_RECTANGLE.create(editor);
		registry.registerAction(action);
		paletteActions.add(action);

		// CONNECTION_SELECTION
		action = PaletteActionFactory.TOGGLE_CONNECTION.create(editor);
		registry.registerAction(action);
		paletteActions.add(action);
	}
	
	private void createSelectionActions(List<Object> selectionActions) {

		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		IAction action;
		
		// ----------------------
		// SelectionActionFactory

		// EDIT_TEXT
		action = SelectionActionFactory.EDIT_TEXT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// FONT_PICK
		action = SelectionActionFactory.FONT_PICK.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// COLOR_PICK
		action = SelectionActionFactory.COLOR_PICK.create(editor);
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
		action = SelectionActionFactory.MOVE_LEFT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_RIGHT
		action = SelectionActionFactory.MOVE_RIGHT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_DOWN
		action = SelectionActionFactory.MOVE_DOWN.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_UP
		action = SelectionActionFactory.MOVE_UP.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// ZORDER_TO_FRONT
		action = SelectionActionFactory.ZORDER_TO_FRONT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// ZORDER_TO_BACK
		action = SelectionActionFactory.ZORDER_TO_BACK.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}
	
	void createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).create());
		
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		// add actions to ToolBar
		toolbarManager = new ToolBarManager(toolbar);
		toolbarManager.add(registry.getAction(PaletteActionFactory.TOGGLE_PANNING.getId()));
		toolbarManager.add(registry.getAction(PaletteActionFactory.TOGGLE_MARQUEE.getId()));
		toolbarManager.add(new Separator());
		toolbarManager.add(registry.getAction(PaletteActionFactory.TOGGLE_RECTANGLE.getId()));
		toolbarManager.add(registry.getAction(PaletteActionFactory.TOGGLE_CONNECTION.getId()));
		toolbarManager.add(new Separator());
		toolbarManager.add(registry.getAction(SelectionActionFactory.FONT_PICK.getId()));
		toolbarManager.add(registry.getAction(SelectionActionFactory.COLOR_PICK.getId()));
		
		toolbarManager.add(new Separator());
		// TODO TEST
		{
			PaletteDropDownAction paletteDropDownAction = PaletteDropDownActionFactory.PALETTE_DROP_DOWN.create();
			
			PaletteAction action = (PaletteAction)registry.getAction(PaletteActionFactory.TOGGLE_RECTANGLE.getId());
			paletteDropDownAction.addAction(action, true);
			action.setPaletteActionChangeListener(paletteDropDownAction);

			action = (PaletteAction)registry.getAction(PaletteActionFactory.TOGGLE_CONNECTION.getId());
			paletteDropDownAction.addAction(action, false);
			action.setPaletteActionChangeListener(paletteDropDownAction);
			
			toolbarManager.add(paletteDropDownAction);
		}
		
		toolbarManager.add(new Separator());
		
		toolbarManager.update(true);
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
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		EditDomain editDomain = editor.getEditDomain();
		
		if ( editDomain.getActiveTool() instanceof FDPanningSelectionTool ) {
			if ( targetEditPart instanceof FDNodeEditPart ) {
				contextMenuManger.add(registry.getAction(SelectionActionFactory.FONT_PICK.getId()));
				contextMenuManger.add(registry.getAction(SelectionActionFactory.ZORDER_TO_FRONT.getId()));
				contextMenuManger.add(registry.getAction(SelectionActionFactory.ZORDER_TO_BACK.getId()));
			} else {
				contextMenuManger.add(registry.getAction(PaletteActionFactory.TOGGLE_RECTANGLE.getId()));
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
		
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_LEFT, 0), registry.getAction(SelectionActionFactory.MOVE_LEFT.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_RIGHT, 0), registry.getAction(SelectionActionFactory.MOVE_RIGHT.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_DOWN, 0), registry.getAction(SelectionActionFactory.MOVE_DOWN.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_UP, 0), registry.getAction(SelectionActionFactory.MOVE_UP.getId()));
	}
	
	@Override
	public void activeToolChanged(Tool tool) {
		for ( PaletteAction action : paletteActions ) {
			if ( action.getTool() == tool ) {
				action.setChecked(true);
			} else {
				action.setChecked(false);
			}
		}
		
		// 툴에 뷰어 설정
		GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
		tool.setViewer(viewer);
	}
}
