package asia.sejong.freedrawing.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.Tool;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleRulerVisibilityAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.ToolBar;

import asia.sejong.freedrawing.editor.actions.common.DropDownAction;
import asia.sejong.freedrawing.editor.actions.palette.PaletteAction;
import asia.sejong.freedrawing.editor.actions.palette.PaletteChangeListener;
import asia.sejong.freedrawing.editor.actions.palette.PaletteDropDownAction;
import asia.sejong.freedrawing.editor.actions.palette.factory.PaletteActionFactory;
import asia.sejong.freedrawing.editor.actions.selection.CopyToClipboardAction;
import asia.sejong.freedrawing.editor.actions.selection.PasteFromClipboardAction;
import asia.sejong.freedrawing.editor.actions.selection.factory.SelectionActionFactory;
import asia.sejong.freedrawing.editor.tools.FDPanningSelectionTool;
import asia.sejong.freedrawing.parts.FDShapeEditPart.FDShapeEditPart;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public class FreedrawingEditorActionManager implements FreedrawingEditDomainListener {

	private FreedrawingEditor editor;
	
	// ActionManager Properties
	private List<PaletteChangeListener> paletteChangeListeners;
	private ToolBarManager toolbarManager;
	private MenuManager contextMenuManger;
	
	private EditPart targetEditPart;
	
	private Scale scale;
	
	private FreedrawingEditorActionManager(FreedrawingEditor editor, List<Object> selectionActions) {
		this.editor = editor;
		this.paletteChangeListeners = new ArrayList<PaletteChangeListener>();
		
		createPaletteActions();
		createSelectionActions(selectionActions);
	}

	static FreedrawingEditorActionManager newInstance(FreedrawingEditor editor, List<Object> selectionActions) {
		return new FreedrawingEditorActionManager(editor, selectionActions);
	}
	
	void createViewerRelatedActions() {
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
		
		// Actions
		IAction showRulers = new ToggleRulerVisibilityAction(viewer);
		registry.registerAction(showRulers);

		IAction snapAction = new ToggleSnapToGeometryAction(viewer);
		registry.registerAction(snapAction);

		IAction showGrid = new ToggleGridAction(viewer);
		registry.registerAction(showGrid);
	}

	private void createPaletteActions() {
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		FreedrawingEditDomain editDomain = editor.getEditDomain();
		PaletteAction action;
		
		editDomain.setEditDomainListener(this);
		// --------------------
		// PaletteActionFactory
		
		// PANNING_SELECTION
		action = PaletteActionFactory.SELECT_PANNING.create(editor);
		registry.registerAction(action);
		// set default tool
		editDomain.setDefaultTool(action.getTool());
		paletteChangeListeners.add(action);
		
		// MARQUEE_SELECTION
		action = PaletteActionFactory.SELECT_MARQUEE.create(editor);
		registry.registerAction(action);
		paletteChangeListeners.add(action);

		// RECTANGLE_SELECTION
		action = PaletteActionFactory.CREATE_RECTANGLE.create(editor);
		registry.registerAction(action);
		paletteChangeListeners.add(action);
		
		// CREATE_ELLIPSE
		action = PaletteActionFactory.CREATE_ELLIPSE.create(editor);
		registry.registerAction(action);
		paletteChangeListeners.add(action);
		
		// CREATE_LABEL
		action = PaletteActionFactory.CREATE_LABEL.create(editor);
		registry.registerAction(action);
		paletteChangeListeners.add(action);
		
		// CREATE_IMAGE
		action = PaletteActionFactory.CREATE_IMAGE.create(editor);
		registry.registerAction(action);
		paletteChangeListeners.add(action);

		// CONNECTION_SELECTION
		action = PaletteActionFactory.CREATE_CONNECTION.create(editor);
		registry.registerAction(action);
		paletteChangeListeners.add(action);
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

		// CHANGE_FONT
		action = SelectionActionFactory.CHANGE_FONT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_FONT_COLOR
		action = SelectionActionFactory.CHANGE_FONT_COLOR.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_LINE_COLOR
		action = SelectionActionFactory.CHANGE_LINE_COLOR.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_LINE_WIDTH
		action = SelectionActionFactory.CHANGE_LINE_WIDTH.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_BACKGROUND_COLOR
		action = SelectionActionFactory.CHANGE_BACKGROUND_COLOR.create(editor);
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
		
		// MOVE_LEFT_FAST
		action = SelectionActionFactory.MOVE_LEFT_FAST.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_RIGHT_FAST
		action = SelectionActionFactory.MOVE_RIGHT_FAST.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_DOWN_FAST
		action = SelectionActionFactory.MOVE_DOWN_FAST.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// MOVE_UP_FAST
		action = SelectionActionFactory.MOVE_UP_FAST.create(editor);
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
		
		// CHANGE_ROUTER
		action = SelectionActionFactory.CHANGE_ROUTER.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_LINE_STYLE_DASH
		action = SelectionActionFactory.CHANGE_LINE_STYLE_DASH.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_LINE_STYLE_DASHDOT
		action = SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_LINE_STYLE_DASHDOTDOT
		action = SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOTDOT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_LINE_STYLE_DOT
		action = SelectionActionFactory.CHANGE_LINE_STYLE_DOT.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_LINE_STYLE_SOLID
		action = SelectionActionFactory.CHANGE_LINE_STYLE_SOLID.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}
	
	void createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).create());
	
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		
		// add actions to ToolBar
		toolbarManager = new ToolBarManager(toolbar);
		toolbarManager.add(registry.getAction(PaletteActionFactory.SELECT_PANNING.getId()));
		toolbarManager.add(registry.getAction(PaletteActionFactory.SELECT_MARQUEE.getId()));
		toolbarManager.add(new Separator());
		toolbarManager.add(registry.getAction(PaletteActionFactory.CREATE_CONNECTION.getId()));
		// TODO TEST
		{
			PaletteDropDownAction paletteDropDownAction = new PaletteDropDownAction();
			paletteChangeListeners.add(paletteDropDownAction);
			
			addToPaletteDropDownAction(paletteDropDownAction, PaletteActionFactory.CREATE_RECTANGLE.getId(), true);
			addToPaletteDropDownAction(paletteDropDownAction, PaletteActionFactory.CREATE_ELLIPSE.getId(), false);
			addToPaletteDropDownAction(paletteDropDownAction, PaletteActionFactory.CREATE_LABEL.getId(), false);
			addToPaletteDropDownAction(paletteDropDownAction, PaletteActionFactory.CREATE_IMAGE.getId(), false);
			
			toolbarManager.add(paletteDropDownAction);
		}
		
		toolbarManager.add(new Separator());
		{
			DropDownAction<SelectionAction> dropDownAction = new DropDownAction<SelectionAction>();
			
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_FONT.getId(), true);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_FONT_COLOR.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_BACKGROUND_COLOR.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_COLOR.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_WIDTH.getId(), false);
			
			toolbarManager.add(dropDownAction);
		}
		
		toolbarManager.add(new Separator());
		{
			DropDownAction<SelectionAction> dropDownAction = new DropDownAction<SelectionAction>();
			
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DASH.getId(), true);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOT.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOTDOT.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DOT.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_SOLID.getId(), false);
			
			toolbarManager.add(dropDownAction);
		}
		
		
		// TODO
		toolbarManager.add(new ControlContribution("XXXX") {

			@Override
			protected Control createControl(Composite parent) {
//				final Scale scale = new Scale (parent, SWT.NONE);
//				GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
//				ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart)viewer.getRootEditPart();
//				final ZoomManager zoomManager = rootEditPart.getZoomManager();
//				scale.setMaximum (zoomManager.getZoomLevels().length-1);
//				scale.setPageIncrement (1);
//				scale.pack();
//				int currentLocation = 0;
//				for ( int index = 0; index < zoomManager.getZoomLevels().length; index++ ) {
//					if ( zoomManager.getZoom() == zoomManager.getZoomLevels()[index] ) {
//						currentLocation = index;
//					}
//				}
//				scale.setSelection(currentLocation);
//				scale.addSelectionListener(new SelectionAdapter() {
//					@Override
//					public void widgetSelected(SelectionEvent e) {
//						zoomManager.setZoom(zoomManager.getZoomLevels()[scale.getSelection()]);
//					}
//				});
//				return scale;
				
				scale = new Scale (parent, SWT.NONE);
				scale.pack();
				scale.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						editor.scaleChanged(scale.getSelection());
					}
				});
				return scale;
			}
			
		});
		toolbarManager.update(true);
	}
	
	private void addToDropDownAction(DropDownAction<SelectionAction> dropDownAction, String actionId, boolean defaultAction) {
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		SelectionAction action = (SelectionAction)registry.getAction(actionId);
		dropDownAction.addAction(action, defaultAction);
	}
	
	private void addToPaletteDropDownAction(PaletteDropDownAction dropDownAction, String actionId, boolean defaultAction) {
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		PaletteAction action = (PaletteAction)registry.getAction(actionId);
		dropDownAction.addAction(action, true);
		action.setPaletteActionChangeListener(dropDownAction);
	}

	void initializeScale(ZoomManager zoomManager) {
		scale.setMaximum (zoomManager.getZoomLevels().length-1);
		scale.setPageIncrement (1);
		int currentLocation = 0;
		for ( int index = 0; index < zoomManager.getZoomLevels().length; index++ ) {
			if ( zoomManager.getZoom() == zoomManager.getZoomLevels()[index] ) {
				currentLocation = index;
			}
		}
		setScaleSelection(currentLocation);
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
			if ( targetEditPart instanceof FDShapeEditPart ) {
				contextMenuManger.add(registry.getAction(SelectionActionFactory.CHANGE_FONT.getId()));
				contextMenuManger.add(registry.getAction(SelectionActionFactory.CHANGE_FONT_COLOR.getId()));
				contextMenuManger.add(registry.getAction(SelectionActionFactory.CHANGE_LINE_WIDTH.getId()));
				contextMenuManger.add(registry.getAction(SelectionActionFactory.ZORDER_TO_FRONT.getId()));
				contextMenuManger.add(registry.getAction(SelectionActionFactory.ZORDER_TO_BACK.getId()));
			} else {
				contextMenuManger.add(registry.getAction(PaletteActionFactory.CREATE_RECTANGLE.getId()));
			}
			
			contextMenuManger.add(registry.getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
			contextMenuManger.add(registry.getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
			contextMenuManger.add(registry.getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
			contextMenuManger.add(registry.getAction(SelectionActionFactory.CHANGE_ROUTER.getId()));
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
		
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_LEFT, SWT.CTRL), registry.getAction(SelectionActionFactory.MOVE_LEFT_FAST.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_RIGHT, SWT.CTRL), registry.getAction(SelectionActionFactory.MOVE_RIGHT_FAST.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_DOWN, SWT.CTRL), registry.getAction(SelectionActionFactory.MOVE_DOWN_FAST.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_UP, SWT.CTRL), registry.getAction(SelectionActionFactory.MOVE_UP_FAST.getId()));
	}
	
	@Override
	public void activeToolChanged(Tool tool) {
		for ( PaletteChangeListener changable : paletteChangeListeners ) {
			if ( changable.getTool() == tool ) {
				changable.iconChange(tool, IconType.SELECTED);
			} else {
				changable.iconChange(tool, IconType.NORMAL);
			}
		}
		
		// 툴에 뷰어 설정
		GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
		if ( tool != null ) {
			tool.setViewer(viewer);
		}
	}

	private void setScaleSelection(int currentLocation) {
		if ( currentLocation != scale.getSelection() ) {
			scale.setSelection(currentLocation);
			editor.scaleChanged(scale.getSelection());
		}
	}
	
	void setScaleNext() {
		int next = scale.getSelection() + 1;
		if ( next <= scale.getMaximum() ) {
			setScaleSelection(next);
		}
	}
	
	void setScalePrevious() {
		int prev = scale.getSelection() -1;
		if ( prev <= scale.getMaximum() ) {
			setScaleSelection(prev);
		}
	}
}
