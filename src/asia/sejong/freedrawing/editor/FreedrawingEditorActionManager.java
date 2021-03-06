package asia.sejong.freedrawing.editor;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import asia.sejong.freedrawing.code.Item;
import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.editor.actions.common.ChangableDropDownAction;
import asia.sejong.freedrawing.editor.actions.contributions.ColorDisplayItem;
import asia.sejong.freedrawing.editor.actions.contributions.ComboSelectionItem;
import asia.sejong.freedrawing.editor.actions.contributions.DirectEditItem;
import asia.sejong.freedrawing.editor.actions.contributions.DropDownToolItemContribution;
import asia.sejong.freedrawing.editor.actions.contributions.TextDisplayItem;
import asia.sejong.freedrawing.editor.actions.etc.ChangeEditorScaleAction;
import asia.sejong.freedrawing.editor.actions.etc.factory.EtcActionFactory;
import asia.sejong.freedrawing.editor.actions.palette.PaletteAction;
import asia.sejong.freedrawing.editor.actions.palette.PaletteChangeListener;
import asia.sejong.freedrawing.editor.actions.palette.PaletteDropDownAction;
import asia.sejong.freedrawing.editor.actions.palette.factory.PaletteActionFactory;
import asia.sejong.freedrawing.editor.actions.selection.ChangeAlphaAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeBackgroundColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeFontAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeFontColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeLineColorAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeLineStyleAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeLineThickAction;
import asia.sejong.freedrawing.editor.actions.selection.ChangeRotateAction;
import asia.sejong.freedrawing.editor.actions.selection.CopyToClipboardAction;
import asia.sejong.freedrawing.editor.actions.selection.PasteFromClipboardAction;
import asia.sejong.freedrawing.editor.actions.selection.factory.SelectionActionFactory;
import asia.sejong.freedrawing.editor.dialog.DialogUtil;
import asia.sejong.freedrawing.editor.tools.FDPanningSelectionTool;
import asia.sejong.freedrawing.model.FontInfo;
import asia.sejong.freedrawing.parts.FDShapeEditPart.FDShapeEditPart;
import asia.sejong.freedrawing.resources.IconManager.IconType;

public class FreedrawingEditorActionManager implements FreedrawingEditDomainListener {

	private FreedrawingEditor editor;
	
	// ActionManager Properties
	private List<PaletteChangeListener> paletteChangeListeners;
	private ToolBarManager toolbarManagerMain;
	private ToolBarManager toolbarManagerSub;
	private MenuManager contextMenuManger;
	
	private EditPart targetEditPart;
	
	private Scale scale;
	private ComboSelectionItem<Item<Integer>> editorScaleSelectionItem;
	
	private FreedrawingEditorActionManager(FreedrawingEditor editor, List<Object> selectionActions) {
		this.editor = editor;
		this.paletteChangeListeners = new ArrayList<PaletteChangeListener>();
		
		// 1.
		FreedrawingEditDomain editDomain = editor.getEditDomain();
		editDomain.setEditDomainListener(this);
		createPaletteActions(editDomain);
		// 2.
		createSelectionActions(selectionActions);
		// 3.
		createEtcActions();
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

	private void createPaletteActions(FreedrawingEditDomain editDomain) {
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		PaletteAction action;
		
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
		
		// CREATE_POLYGON
		action = PaletteActionFactory.CREATE_POLYGON.create(editor);
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
		
		// GROUPING
		action = SelectionActionFactory.GROUPING.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// UNGROUPING
		action = SelectionActionFactory.UNGROUPING.create(editor);
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
		
		// CHANGE_LINE_STYLE
		action = SelectionActionFactory.CHANGE_LINE_STYLE.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_ROTATION
		action = SelectionActionFactory.CHANGE_ROTATION.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		// CHANGE_ALPHA
		action = SelectionActionFactory.CHANGE_ALPHA.create(editor);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
//		// CHANGE_LINE_STYLE_DASH
//		action = SelectionActionFactory.CHANGE_LINE_STYLE_DASH.create(editor);
//		registry.registerAction(action);
//		selectionActions.add(action.getId());
//		
//		// CHANGE_LINE_STYLE_DASHDOT
//		action = SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOT.create(editor);
//		registry.registerAction(action);
//		selectionActions.add(action.getId());
//		
//		// CHANGE_LINE_STYLE_DASHDOTDOT
//		action = SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOTDOT.create(editor);
//		registry.registerAction(action);
//		selectionActions.add(action.getId());
//		
//		// CHANGE_LINE_STYLE_DOT
//		action = SelectionActionFactory.CHANGE_LINE_STYLE_DOT.create(editor);
//		registry.registerAction(action);
//		selectionActions.add(action.getId());
//		
//		// CHANGE_LINE_STYLE_SOLID
//		action = SelectionActionFactory.CHANGE_LINE_STYLE_SOLID.create(editor);
//		registry.registerAction(action);
//		selectionActions.add(action.getId());
	}
	
	private void createEtcActions() {
		ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		Action action;
		
		// --------------------
		// EtcActionFactory
		
		// CHANGE_EDITOR_SCALE
		action = EtcActionFactory.CHANGE_EDITOR_SCALE.create(editor);
		registry.registerAction(action);
		
		// CHANGE_ROUTER
		action = EtcActionFactory.CHANGE_ROUTER.create(editor);
		registry.registerAction(action);
	}
	
	void createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).create());
	
		toolbarManagerMain = createToolbarManagerMain(toolbar);
		toolbarManagerMain.update(true);

		toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).create());
		toolbarManagerSub = createToolbarManagerSub(toolbar);
		toolbarManagerSub.update(true);

	}
	
	private ToolBarManager createToolbarManagerSub(ToolBar toolbar) {
		
		final ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		
		// add actions to ToolBar
		ToolBarManager toolbarManager = new ToolBarManager(toolbar);
		
		RGB rgb = null;
		// 글자 칼라 선택
		rgb = editor.getEditorContext().getFontColor();
		addColorChangeTool(toolbarManager,(ChangeFontColorAction)registry.getAction(SelectionActionFactory.CHANGE_FONT_COLOR.getId()), rgb);
		// 배경 칼라 선택
		rgb = editor.getEditorContext().getBackgroundColor();
		addColorChangeTool(toolbarManager, (ChangeBackgroundColorAction)registry.getAction(SelectionActionFactory.CHANGE_BACKGROUND_COLOR.getId()), rgb);
		// 테두리 칼라 선택
		rgb = editor.getEditorContext().getLineColor();
		addColorChangeTool(toolbarManager, (ChangeLineColorAction)registry.getAction(SelectionActionFactory.CHANGE_LINE_COLOR.getId()), rgb);
		// 구분자
		toolbarManager.add(new Separator());
		// 폰트 선택
		FontInfo fontInfo = editor.getEditorContext().getFontInfo();
		addFontChangeTool(toolbarManager, (ChangeFontAction)registry.getAction(SelectionActionFactory.CHANGE_FONT.getId()), fontInfo);
		// 구분자
		toolbarManager.add(new Separator());
		// 선두께
		Float lineWidth = editor.getEditorContext().getLineWidth();
		addChangeLineThickTool(toolbarManager, (ChangeLineThickAction)registry.getAction(SelectionActionFactory.CHANGE_LINE_WIDTH.getId()), lineWidth);
		// 구분자
		toolbarManager.add(new Separator());
		// 선두께#2
		//addChangeLineThickTool2(toolbarManager, (ChangeLineThickAction)registry.getAction(SelectionActionFactory.CHANGE_LINE_WIDTH.getId()));
//		toolbarManager.add(new Separator());
		// 선종류
		LineStyle lineStyle = LineStyle.getLineStyle(editor.getEditorContext().getLineStyle());
		addChangeLineStyleTool(toolbarManager, (ChangeLineStyleAction)registry.getAction(SelectionActionFactory.CHANGE_LINE_STYLE.getId()), lineStyle);
		// 구분자
		toolbarManager.add(new Separator());
		// 투명도
		Integer alpah = editor.getEditorContext().getAlpha();
		addChangeAlphaTool(toolbarManager,(ChangeAlphaAction)registry.getAction(SelectionActionFactory.CHANGE_ALPHA.getId()), alpah);
		// 구분자
		toolbarManager.add(new Separator());
		// 피처 각도
		Double degree = editor.getEditorContext().getDegree();
		addChangeRotationTool(toolbarManager, (ChangeRotateAction)registry.getAction(SelectionActionFactory.CHANGE_ROTATION.getId()), degree);
		// 구분자
		toolbarManager.add(new Separator());
		// 에디터 스케일
		addChangeEditorScaleTool(toolbarManager, (ChangeEditorScaleAction)registry.getAction(EtcActionFactory.CHANGE_EDITOR_SCALE.getId()));
						
		return toolbarManager;
	}

	private ToolBarManager createToolbarManagerMain(ToolBar toolbar) {
		
		final ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
		
		// add actions to ToolBar
		ToolBarManager toolbarManager = new ToolBarManager(toolbar);
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
			addToPaletteDropDownAction(paletteDropDownAction, PaletteActionFactory.CREATE_POLYGON.getId(), false);
			addToPaletteDropDownAction(paletteDropDownAction, PaletteActionFactory.CREATE_IMAGE.getId(), false);
			
			toolbarManager.add(paletteDropDownAction);
		}
		
		toolbarManager.add(new Separator());
		{
			ChangableDropDownAction<SelectionAction> dropDownAction = new ChangableDropDownAction<SelectionAction>();
			
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_FONT.getId(), true);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_FONT_COLOR.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_BACKGROUND_COLOR.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_COLOR.getId(), false);
			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_WIDTH.getId(), false);
			
			toolbarManager.add(dropDownAction);
		}
		
//		toolbarManager.add(new Separator());
//		{
//			ChangableDropDownAction<SelectionAction> dropDownAction = new ChangableDropDownAction<SelectionAction>();
//			
//			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DASH.getId(), true);
//			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOT.getId(), false);
//			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DASHDOTDOT.getId(), false);
//			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_DOT.getId(), false);
//			addToDropDownAction(dropDownAction, SelectionActionFactory.CHANGE_LINE_STYLE_SOLID.getId(), false);
//			
//			toolbarManager.add(dropDownAction);
//		}
		
		
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
		
		return toolbarManager;
	}
	
	private static void addColorChangeTool(ToolBarManager toolbarManager, final ChangeColorAction<?> action, RGB defaultRgb) {
		final ColorDisplayItem colorDisplayItem = new ColorDisplayItem(action, defaultRgb);
		toolbarManager.add(action);
		toolbarManager.add(colorDisplayItem);
		toolbarManager.add(new DropDownToolItemContribution(action.getId()) {
			@Override
			protected void dropDownSelected(Event e) {
				ToolItem ti = (ToolItem)e.widget;
				Point loc = ti.getParent().toDisplay( new Point(e.x, e.y));
				RGB rgb = DialogUtil.openColorSelectDialog(ti.getDisplay(), loc);
				
				if ( rgb != null ) {
					colorDisplayItem.setRgb(rgb);
					action.setRgb(rgb);
					action.run();
				}
			}
		});
		
		action.setRgb(defaultRgb);
	}
	
	private static void addFontChangeTool(ToolBarManager toolbarManager, final ChangeFontAction action, FontInfo defaultFontInfo) {
		final String formatString = "%s(%d) - %d";
		final String defaultText = String.format(formatString, defaultFontInfo.getName(), defaultFontInfo.getHeight(), defaultFontInfo.getStyle());
		final TextDisplayItem textDisplayItem = new TextDisplayItem(action, defaultText);
		toolbarManager.add(action);
		toolbarManager.add(textDisplayItem);
		toolbarManager.add(new DropDownToolItemContribution(action.getId()) {
			@Override
			protected void dropDownSelected(Event e) {
				ToolItem ti = (ToolItem)e.widget;
				Point loc = ti.getParent().toDisplay( new Point(e.x, e.y));
				FontData fontData = DialogUtil.openFontSelectDialog(ti.getDisplay(), loc, action.getFontInfo());
				
				if ( fontData != null ) {
					FontInfo fontInfo = FontInfo.create(fontData);
					String text = String.format(formatString, fontInfo.getName(), fontInfo.getHeight(), fontInfo.getStyle());
					textDisplayItem.setText(text);
					action.setFontInfo(fontInfo);
					action.run();
				}
			}
		});
		
		action.setFontInfo(defaultFontInfo);
	}
	
	private static void addChangeLineThickTool(ToolBarManager toolbarManager, final ChangeLineThickAction action, Float defaultLineWidth) {
		final ComboSelectionItem<Float> comboSelectionItem = new ComboSelectionItem<Float>(action);
		toolbarManager.add(action);
		toolbarManager.add(comboSelectionItem);
		comboSelectionItem.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if ( sel.getFirstElement() instanceof Float ) {
					action.setLineWidth((Float)sel.getFirstElement());
					action.run();
				}
			}
		});
		List<Float> data = Arrays.asList(1f,2f,3f,4f,5f,6f,7f);
		comboSelectionItem.setInput(data);
		comboSelectionItem.setSelection(defaultLineWidth);
	}
	
	private void addChangeEditorScaleTool(ToolBarManager toolbarManager, final ChangeEditorScaleAction action) {
		editorScaleSelectionItem = new ComboSelectionItem<Item<Integer>>(action);
		toolbarManager.add(action);
		toolbarManager.add(editorScaleSelectionItem);
		editorScaleSelectionItem.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if ( sel.getFirstElement() instanceof Item ) {
					@SuppressWarnings("unchecked")
					Item<Integer> item = (Item<Integer>)sel.getFirstElement();
					action.setZoomLevelIndex(item.getValue());
					action.run();
				}
			}
		});
	}

	private static void addChangeLineStyleTool(ToolBarManager toolbarManager, final ChangeLineStyleAction action, LineStyle defaultLineStyle) {
		final ComboSelectionItem<LineStyle> comboSelectionItem = new ComboSelectionItem<LineStyle>(action);
		toolbarManager.add(action);
		toolbarManager.add(comboSelectionItem);
		comboSelectionItem.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if ( sel.getFirstElement() instanceof LineStyle ) {
					action.setLineStyle((LineStyle)sel.getFirstElement());
					action.run();
				}
			}
		});
		List<LineStyle> data = Arrays.asList(LineStyle.SOLID,LineStyle.DASH,LineStyle.DASHDOT,LineStyle.DASHDOTDOT,LineStyle.DOT);
		comboSelectionItem.setInput(data);
		comboSelectionItem.setSelection(defaultLineStyle);
		action.setLineStyle(defaultLineStyle);
	}
	
	private static void addChangeAlphaTool(ToolBarManager toolbarManager, final ChangeAlphaAction action, Integer defaultAlpha) {
		final DirectEditItem directEditItem = new DirectEditItem(action) {
			@Override
			protected boolean checkValidValue(String value) {
				try {
					Integer iValue = Integer.valueOf(value);
					if ( iValue>=0x00 && 0xff>=iValue) {
						return true;
					}
				} catch ( Exception e ) {
				}
				
				return false;
			}
			
			protected void valueChanged(String value) {
				Integer iValue = Integer.valueOf(value);
				action.setAlpha(iValue);
				action.run();
			}
		};
		toolbarManager.add(action);
		toolbarManager.add(directEditItem);
	}
	
	private static void addChangeRotationTool(ToolBarManager toolbarManager, final ChangeRotateAction action, Double defaultDegree) {
		final DirectEditItem directEditItem = new DirectEditItem(action) {
			@Override
			protected boolean checkValidValue(String value) {
				try {
					Double dValue = Double.valueOf(value);
					if ( dValue>=0f && 360f>dValue) {
						return true;
					}
				} catch ( Exception e ) {
				}
				
				return false;
			}
			
			protected void valueChanged(String value) {
				Double dValue = Double.valueOf(value);
				action.setDegree(dValue);
				action.run();
			}
		};
		toolbarManager.add(action);
		toolbarManager.add(directEditItem);
	}

	private void addToDropDownAction(ChangableDropDownAction<SelectionAction> dropDownAction, String actionId, boolean defaultAction) {
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
		
		@SuppressWarnings("unchecked")
		ComboSelectionItem<Item<Integer>> changeEditorScaleItem = (ComboSelectionItem<Item<Integer>>)toolbarManagerSub.find(EtcActionFactory.CHANGE_EDITOR_SCALE.getId()+"_CSI");
		List<Item<Integer>> data = new ArrayList<Item<Integer>>();
		for ( int i=0; i<zoomManager.getZoomLevels().length; i++ ) {
			data.add(new Item<Integer>("" + zoomManager.getZoomLevels()[i], i));
		}
		changeEditorScaleItem.setInput(data);
		int defaultScaleIndex = editor.getEditorContext().getScaleIndex();
		changeEditorScaleItem.setSelection(defaultScaleIndex);
		
		//
		DirectEditItem dei = (DirectEditItem)toolbarManagerSub.find(SelectionActionFactory.CHANGE_ALPHA.getId()+"_DEI");
		Integer alpah = editor.getEditorContext().getAlpha();
		dei.changeValue(alpah.toString());
		
		//
		dei = (DirectEditItem)toolbarManagerSub.find(SelectionActionFactory.CHANGE_ROTATION.getId()+"_DEI");
		Double degree = editor.getEditorContext().getDegree();
		dei.changeValue(degree.toString());
	}
	
	void dispose() {
		if ( toolbarManagerMain != null ) {
			toolbarManagerMain.dispose();
		}
		
		if ( toolbarManagerSub != null ) {
			toolbarManagerSub.dispose();
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
				contextMenuManger.add(registry.getAction(SelectionActionFactory.GROUPING.getId()));
				contextMenuManger.add(registry.getAction(SelectionActionFactory.UNGROUPING.getId()));
			} else {
				contextMenuManger.add(registry.getAction(PaletteActionFactory.CREATE_RECTANGLE.getId()));
			}
			
			contextMenuManger.add(registry.getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
			contextMenuManger.add(registry.getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
			contextMenuManger.add(registry.getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
			contextMenuManger.add(registry.getAction(EtcActionFactory.CHANGE_ROUTER.getId()));
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
			editorScaleSelectionItem.setSelection(currentLocation);
			editor.scaleChanged(scale.getSelection());
		}
	}
	
	void setScaleNext() {
		int next = scale.getSelection() + 1;
		if ( next > scale.getMaximum() ) {
			next = scale.getMaximum();
		}
		setScaleSelection(next);
	}
	
	void setScalePrevious() {
		int prev = scale.getSelection() - 1;
		if ( prev < scale.getMinimum() ) {
			prev = scale.getMinimum();
		}
		setScaleSelection(prev);
	}
}
