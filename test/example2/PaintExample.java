package example2;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.accessibility.*;

import java.io.*;
import java.text.*;
import java.util.*;

public class PaintExample {
	private Composite mainComposite;
	private Canvas activeForegroundColorCanvas;
	private Canvas activeBackgroundColorCanvas;
	private Color paintColorBlack, paintColorWhite; // alias for paintColors[0]
													// and [1]
	private Color[] paintColors;
	private Font paintDefaultFont; // do not free
	private static final int numPaletteRows = 3;
	private static final int numPaletteCols = 50;
	private ToolSettings toolSettings; // current active settings
	private PaintSurface paintSurface; // paint surface for drawing

	static final int Pencil_tool = 0;
	static final int Airbrush_tool = 1;
	static final int Line_tool = 2;
	static final int PolyLine_tool = 3;
	static final int Rectangle_tool = 4;
	static final int RoundedRectangle_tool = 5;
	static final int Ellipse_tool = 6;
	static final int Text_tool = 7;
	static final int None_fill = 8;
	static final int Outline_fill = 9;
	static final int Solid_fill = 10;
	static final int Solid_linestyle = 11;
	static final int Dash_linestyle = 12;
	static final int Dot_linestyle = 13;
	static final int DashDot_linestyle = 14;
	static final int Font_options = 15;

	static final int Default_tool = Pencil_tool;
	static final int Default_fill = None_fill;
	static final int Default_linestyle = Solid_linestyle;

	public static final Tool[] tools = {
			new Tool(Pencil_tool, "Pencil", "tool", SWT.RADIO),
			new Tool(Airbrush_tool, "Airbrush", "tool", SWT.RADIO),
			new Tool(Line_tool, "Line", "tool", SWT.RADIO),
			new Tool(PolyLine_tool, "PolyLine", "tool", SWT.RADIO),
			new Tool(Rectangle_tool, "Rectangle", "tool", SWT.RADIO),
			new Tool(RoundedRectangle_tool, "RoundedRectangle", "tool",
					SWT.RADIO),
			new Tool(Ellipse_tool, "Ellipse", "tool", SWT.RADIO),
			new Tool(Text_tool, "Text", "tool", SWT.RADIO),
			new Tool(None_fill, "None", "fill", SWT.RADIO, new Integer(
					ToolSettings.ftNone)),
			new Tool(Outline_fill, "Outline", "fill", SWT.RADIO, new Integer(
					ToolSettings.ftOutline)),
			new Tool(Solid_fill, "Solid", "fill", SWT.RADIO, new Integer(
					ToolSettings.ftSolid)),
			new Tool(Solid_linestyle, "Solid", "linestyle", SWT.RADIO,
					new Integer(SWT.LINE_SOLID)),
			new Tool(Dash_linestyle, "Dash", "linestyle", SWT.RADIO,
					new Integer(SWT.LINE_DASH)),
			new Tool(Dot_linestyle, "Dot", "linestyle", SWT.RADIO, new Integer(
					SWT.LINE_DOT)),
			new Tool(DashDot_linestyle, "DashDot", "linestyle", SWT.RADIO,
					new Integer(SWT.LINE_DASHDOT)),
			new Tool(Font_options, "Font", "options", SWT.PUSH) };

	/**
	 * Creates an instance of a PaintExample embedded inside the supplied parent
	 * Composite.
	 * 
	 * @param parent
	 *            the container of the example
	 */
	public PaintExample(Composite parent) {
		mainComposite = parent;
		initResources();
		initActions();
		init();
	}

	/**
	 * Invokes as a standalone program.
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText(getResourceString("window.title"));
		shell.setLayout(new GridLayout());
		PaintExample instance = new PaintExample(shell);
		instance.createToolBar(shell);
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		instance.createGUI(composite);
		instance.setDefaults();
		setShellSize(display, shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		instance.dispose();
	}

	/**
	 * Creates the toolbar. Note: Only called by standalone.
	 */
	private void createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.NONE);
		String group = null;
		for (int i = 0; i < tools.length; i++) {
			Tool tool = tools[i];
			if (group != null && !tool.group.equals(group)) {
				new ToolItem(toolbar, SWT.SEPARATOR);
			}
			group = tool.group;
			ToolItem item = addToolItem(toolbar, tool);
			if (i == Default_tool || i == Default_fill
					|| i == Default_linestyle)
				item.setSelection(true);
		}
	}

	/**
	 * Adds a tool item to the toolbar. Note: Only called by standalone.
	 */
	private ToolItem addToolItem(final ToolBar toolbar, final Tool tool) {
		final String id = tool.group + '.' + tool.name;
		ToolItem item = new ToolItem(toolbar, tool.type);
		item.setText(getResourceString(id + ".label"));
		item.setToolTipText(getResourceString(id + ".tooltip"));
		item.setImage(tool.image);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tool.action.run();
			}
		});
		final int childID = toolbar.indexOf(item);
		toolbar.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getName(org.eclipse.swt.accessibility.AccessibleEvent e) {
				if (e.childID == childID) {
					e.result = getResourceString(id + ".description");
				}
			}
		});
		return item;
	}

	/**
	 * Sets the default tool item states.
	 */
	public void setDefaults() {
		setPaintTool(Default_tool);
		setFillType(Default_fill);
		setLineStyle(Default_linestyle);
		setForegroundColor(paintColorBlack);
		setBackgroundColor(paintColorWhite);
	}

	/**
	 * Creates the GUI.
	 */
	public void createGUI(Composite parent) {
		GridLayout gridLayout;
		GridData gridData;

		/*** Create principal GUI layout elements ***/
		Composite displayArea = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		displayArea.setLayout(gridLayout);

		// Creating these elements here avoids the need to instantiate the GUI
		// elements
		// in strict layout order. The natural layout ordering is an artifact of
		// using
		// SWT layouts, but unfortunately it is not the same order as that
		// required to
		// instantiate all of the non-GUI application elements to satisfy
		// referential
		// dependencies. It is possible to reorder the initialization to some
		// extent, but
		// this can be very tedious.

		// paint canvas
		final Canvas paintCanvas = new Canvas(displayArea, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL | SWT.NO_REDRAW_RESIZE
				| SWT.NO_BACKGROUND);
		gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		paintCanvas.setLayoutData(gridData);
		paintCanvas.setBackground(paintColorWhite);

		// color selector frame
		final Composite colorFrame = new Composite(displayArea, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		colorFrame.setLayoutData(gridData);

		// tool settings frame
		final Composite toolSettingsFrame = new Composite(displayArea, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		toolSettingsFrame.setLayoutData(gridData);

		// status text
		final Text statusText = new Text(displayArea, SWT.BORDER | SWT.SINGLE
				| SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		statusText.setLayoutData(gridData);

		/***
		 * Create the remaining application elements inside the principal GUI
		 * layout elements
		 ***/
		// paintSurface
		paintSurface = new PaintSurface(paintCanvas, statusText,
				paintColorWhite);

		// finish initializing the tool data
		tools[Pencil_tool].data = new PencilTool(toolSettings, paintSurface);
		tools[Airbrush_tool].data = new AirbrushTool(toolSettings, paintSurface);
		tools[Line_tool].data = new LineTool(toolSettings, paintSurface);
		tools[PolyLine_tool].data = new PolyLineTool(toolSettings, paintSurface);
		tools[Rectangle_tool].data = new RectangleTool(toolSettings,
				paintSurface);
		tools[RoundedRectangle_tool].data = new RoundedRectangleTool(
				toolSettings, paintSurface);
		tools[Ellipse_tool].data = new EllipseTool(toolSettings, paintSurface);
		tools[Text_tool].data = new TextTool(toolSettings, paintSurface);

		// colorFrame
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		colorFrame.setLayout(gridLayout);

		// activeForegroundColorCanvas, activeBackgroundColorCanvas
		activeForegroundColorCanvas = new Canvas(colorFrame, SWT.BORDER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.heightHint = 24;
		gridData.widthHint = 24;
		activeForegroundColorCanvas.setLayoutData(gridData);

		activeBackgroundColorCanvas = new Canvas(colorFrame, SWT.BORDER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.heightHint = 24;
		gridData.widthHint = 24;
		activeBackgroundColorCanvas.setLayoutData(gridData);

		// paletteCanvas
		final Canvas paletteCanvas = new Canvas(colorFrame, SWT.BORDER
				| SWT.NO_BACKGROUND);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 24;
		paletteCanvas.setLayoutData(gridData);
		paletteCanvas.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event e) {
				Rectangle bounds = paletteCanvas.getClientArea();
				Color color = getColorAt(bounds, e.x, e.y);

				if (e.button == 1)
					setForegroundColor(color);
				else
					setBackgroundColor(color);
			}

			private Color getColorAt(Rectangle bounds, int x, int y) {
				if (bounds.height <= 1 && bounds.width <= 1)
					return paintColorWhite;
				final int row = (y - bounds.y) * numPaletteRows / bounds.height;
				final int col = (x - bounds.x) * numPaletteCols / bounds.width;
				return paintColors[Math.min(
						Math.max(row * numPaletteCols + col, 0),
						paintColors.length - 1)];
			}
		});
		Listener refreshListener = new Listener() {
			public void handleEvent(Event e) {
				if (e.gc == null)
					return;
				Rectangle bounds = paletteCanvas.getClientArea();
				for (int row = 0; row < numPaletteRows; ++row) {
					for (int col = 0; col < numPaletteCols; ++col) {
						final int x = bounds.width * col / numPaletteCols;
						final int y = bounds.height * row / numPaletteRows;
						final int width = Math.max(bounds.width * (col + 1)
								/ numPaletteCols - x, 1);
						final int height = Math.max(bounds.height * (row + 1)
								/ numPaletteRows - y, 1);
						e.gc.setBackground(paintColors[row * numPaletteCols
								+ col]);
						e.gc.fillRectangle(bounds.x + x, bounds.y + y, width,
								height);
					}
				}
			}
		};
		paletteCanvas.addListener(SWT.Resize, refreshListener);
		paletteCanvas.addListener(SWT.Paint, refreshListener);
		// paletteCanvas.redraw();

		// toolSettingsFrame
		gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		toolSettingsFrame.setLayout(gridLayout);

		Label label = new Label(toolSettingsFrame, SWT.NONE);
		label.setText(getResourceString("settings.AirbrushRadius.text"));

		final Scale airbrushRadiusScale = new Scale(toolSettingsFrame,
				SWT.HORIZONTAL);
		airbrushRadiusScale.setMinimum(5);
		airbrushRadiusScale.setMaximum(50);
		airbrushRadiusScale.setSelection(toolSettings.airbrushRadius);
		airbrushRadiusScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL));
		airbrushRadiusScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toolSettings.airbrushRadius = airbrushRadiusScale
						.getSelection();
				updateToolSettings();
			}
		});

		label = new Label(toolSettingsFrame, SWT.NONE);
		label.setText(getResourceString("settings.AirbrushIntensity.text"));

		final Scale airbrushIntensityScale = new Scale(toolSettingsFrame,
				SWT.HORIZONTAL);
		airbrushIntensityScale.setMinimum(1);
		airbrushIntensityScale.setMaximum(100);
		airbrushIntensityScale.setSelection(toolSettings.airbrushIntensity);
		airbrushIntensityScale.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
		airbrushIntensityScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toolSettings.airbrushIntensity = airbrushIntensityScale
						.getSelection();
				updateToolSettings();
			}
		});
	}

	/**
	 * Disposes of all resources associated with a particular instance of the
	 * PaintExample.
	 */
	public void dispose() {
		if (paintSurface != null)
			paintSurface.dispose();
		if (paintColors != null) {
			for (int i = 0; i < paintColors.length; ++i) {
				final Color color = paintColors[i];
				if (color != null)
					color.dispose();
			}
		}
		paintDefaultFont = null;
		paintColors = null;
		paintSurface = null;
		freeResources();
	}

	/**
	 * Frees the resource bundle resources.
	 */
	public void freeResources() {
		for (int i = 0; i < tools.length; ++i) {
			Tool tool = tools[i];
			final Image image = tool.image;
			if (image != null)
				image.dispose();
			tool.image = null;
		}
	}

	/**
	 * Returns the Display.
	 * 
	 * @return the display we're using
	 */
	public Display getDisplay() {
		return mainComposite.getDisplay();
	}

	/**
	 * Gets a string from the resource bundle. We don't want to crash because of
	 * a missing String. Returns the key if not found.
	 */
	public static String getResourceString(String key) {
		return key;
	}

	/**
	 * Gets a string from the resource bundle and binds it with the given
	 * arguments. If the key is not found, return the key.
	 */
	public static String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Initialize colors, fonts, and tool settings.
	 */
	private void init() {
		Display display = mainComposite.getDisplay();

		paintColorWhite = new Color(display, 255, 255, 255);
		paintColorBlack = new Color(display, 0, 0, 0);

		paintDefaultFont = display.getSystemFont();

		paintColors = new Color[numPaletteCols * numPaletteRows];
		paintColors[0] = paintColorBlack;
		paintColors[1] = paintColorWhite;
		for (int i = 2; i < paintColors.length; i++) {
			paintColors[i] = new Color(display, ((i * 7) % 255),
					((i * 23) % 255), ((i * 51) % 255));
		}

		toolSettings = new ToolSettings();
		toolSettings.commonForegroundColor = paintColorBlack;
		toolSettings.commonBackgroundColor = paintColorWhite;
		toolSettings.commonFont = paintDefaultFont;
	}

	/**
	 * Sets the action field of the tools
	 */
	private void initActions() {
		for (int i = 0; i < tools.length; ++i) {
			final Tool tool = tools[i];
			String group = tool.group;
			if (group.equals("tool")) {
				tool.action = new Runnable() {
					public void run() {
						setPaintTool(tool.id);
					}
				};
			} else if (group.equals("fill")) {
				tool.action = new Runnable() {
					public void run() {
						setFillType(tool.id);
					}
				};
			} else if (group.equals("linestyle")) {
				tool.action = new Runnable() {
					public void run() {
						setLineStyle(tool.id);
					}
				};
			} else if (group.equals("options")) {
				tool.action = new Runnable() {
					public void run() {
						FontDialog fontDialog = new FontDialog(
								paintSurface.getShell(), SWT.PRIMARY_MODAL);
						FontData[] fontDatum = toolSettings.commonFont
								.getFontData();
						if (fontDatum != null && fontDatum.length > 0) {
							fontDialog.setFontList(fontDatum);
						}
						fontDialog
								.setText(getResourceString("options.Font.dialog.title"));

						paintSurface.hideRubberband();
						FontData fontData = fontDialog.open();
						paintSurface.showRubberband();
						if (fontData != null) {
							try {
								Font font = new Font(
										mainComposite.getDisplay(), fontData);
								toolSettings.commonFont = font;
								updateToolSettings();
							} catch (SWTException ex) {
							}
						}
					}
				};
			}
		}
	}

	/**
	 * Loads the image resources.
	 */
	public void initResources() {
		final Class clazz = PaintExample.class;
		try {
			for (int i = 0; i < tools.length; ++i) {
				Tool tool = tools[i];
				String id = tool.group + '.' + tool.name;
				InputStream sourceStream = clazz
						.getResourceAsStream(getResourceString(id + ".image"));
				ImageData source = new ImageData(sourceStream);
				ImageData mask = source.getTransparencyMask();
				tool.image = new Image(null, source, mask);
				try {
					sourceStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return;
		} catch (Throwable t) {
		}

		String error = "Unable to load resources";
		freeResources();
		throw new RuntimeException(error);
	}

	/**
	 * Grabs input focus.
	 */
	public void setFocus() {
		mainComposite.setFocus();
	}

	/**
	 * Sets the tool foreground color.
	 * 
	 * @param color
	 *            the new color to use
	 */
	public void setForegroundColor(Color color) {
		if (activeForegroundColorCanvas != null)
			activeForegroundColorCanvas.setBackground(color);
		toolSettings.commonForegroundColor = color;
		updateToolSettings();
	}

	/**
	 * Set the tool background color.
	 * 
	 * @param color
	 *            the new color to use
	 */
	public void setBackgroundColor(Color color) {
		if (activeBackgroundColorCanvas != null)
			activeBackgroundColorCanvas.setBackground(color);
		toolSettings.commonBackgroundColor = color;
		updateToolSettings();
	}

	/**
	 * Selects a tool given its ID.
	 */
	public void setPaintTool(int id) {
		PaintTool paintTool = (PaintTool) tools[id].data;
		paintSurface.setPaintSession(paintTool);
		updateToolSettings();
	}

	/**
	 * Selects a filltype given its ID.
	 */
	public void setFillType(int id) {
		Integer fillType = (Integer) tools[id].data;
		toolSettings.commonFillType = fillType.intValue();
		updateToolSettings();
	}

	/**
	 * Selects line type given its ID.
	 */
	public void setLineStyle(int id) {
		Integer lineType = (Integer) tools[id].data;
		toolSettings.commonLineStyle = lineType.intValue();
		updateToolSettings();
	}

	/**
	 * Sets the size of the shell to it's "packed" size, unless that makes it
	 * bigger than the display, in which case set it to 9/10 of display size.
	 */
	private static void setShellSize(Display display, Shell shell) {
		Rectangle bounds = display.getBounds();
		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		if (size.x > bounds.width)
			size.x = bounds.width * 9 / 10;
		if (size.y > bounds.height)
			size.y = bounds.height * 9 / 10;
		shell.setSize(size);
	}

	/**
	 * Notifies the tool that its settings have changed.
	 */
	private void updateToolSettings() {
		final PaintTool activePaintTool = paintSurface.getPaintTool();
		if (activePaintTool == null)
			return;

		activePaintTool.endSession();
		activePaintTool.set(toolSettings);
		activePaintTool.beginSession();
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * Tool Settings objects group tool-related configuration information.
 */
class ToolSettings {
	public static final int ftNone = 0, ftOutline = 1, ftSolid = 2;

	/**
	 * commonForegroundColor: current tool foreground colour
	 */
	public Color commonForegroundColor;

	/**
	 * commonBackgroundColor: current tool background colour
	 */
	public Color commonBackgroundColor;

	/**
	 * commonFont: current font
	 */
	public Font commonFont;

	/**
	 * commonFillType: current fill type
	 * <p>
	 * One of ftNone, ftOutline, ftSolid.
	 * </p>
	 */
	public int commonFillType = ftNone;

	/**
	 * commonLineStyle: current line type
	 */
	public int commonLineStyle = SWT.LINE_SOLID;

	/**
	 * airbrushRadius: coverage radius in pixels
	 */
	public int airbrushRadius = 10;

	/**
	 * airbrushIntensity: average surface area coverage in region defined by
	 * radius per "jot"
	 */
	public int airbrushIntensity = 30;

	/**
	 * roundedRectangleCornerDiameter: the diameter of curvature of corners in a
	 * rounded rectangle
	 */
	public int roundedRectangleCornerDiameter = 16;
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
class Tool {
	public int id;
	public String name;
	public String group;
	public int type;
	public Runnable action;
	public Image image = null;
	public Object data;

	public Tool(int id, String name, String group, int type) {
		super();
		this.id = id;
		this.name = name;
		this.group = group;
		this.type = type;
	}

	public Tool(int id, String name, String group, int type, Object data) {
		this(id, name, group, type);
		this.data = data;
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * A text drawing tool.
 */
class TextTool extends BasicPaintSession implements PaintTool {
	private ToolSettings settings;
	private String drawText = PaintExample
			.getResourceString("tool.Text.settings.defaulttext");

	/**
	 * Constructs a PaintTool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public TextTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Text.label");
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().setStatusMessage(
				PaintExample.getResourceString("session.Text.message"));
	}

	/**
	 * Deactivates the tool.
	 */
	public void endSession() {
		getPaintSurface().clearRubberbandSelection();
	}

	/**
	 * Aborts the current operation.
	 */
	public void resetSession() {
		getPaintSurface().clearRubberbandSelection();
	}

	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseDown(MouseEvent event) {
		if (event.button == 1) {
			// draw with left mouse button
			getPaintSurface().commitRubberbandSelection();
		} else {
			// set text with right mouse button
			getPaintSurface().clearRubberbandSelection();
			Shell shell = getPaintSurface().getShell();
			final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
					| SWT.APPLICATION_MODAL);
			dialog.setText(PaintExample
					.getResourceString("tool.Text.dialog.title"));
			dialog.setLayout(new GridLayout());
			Label label = new Label(dialog, SWT.NONE);
			label.setText(PaintExample
					.getResourceString("tool.Text.dialog.message"));
			label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			final Text field = new Text(dialog, SWT.SINGLE | SWT.BORDER);
			field.setText(drawText);
			field.selectAll();
			field.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			Composite buttons = new Composite(dialog, SWT.NONE);
			GridLayout layout = new GridLayout(2, true);
			layout.marginWidth = 0;
			buttons.setLayout(layout);
			buttons.setLayoutData(new GridData(SWT.END, SWT.CENTER, false,
					false));
			Button ok = new Button(buttons, SWT.PUSH);
			ok.setText(PaintExample.getResourceString("OK"));
			ok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					drawText = field.getText();
					dialog.dispose();
				}
			});
			Button cancel = new Button(buttons, SWT.PUSH);
			cancel.setText(PaintExample.getResourceString("Cancel"));
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					dialog.dispose();
				}
			});
			dialog.setDefaultButton(ok);
			dialog.pack();
			dialog.open();
			Display display = dialog.getDisplay();
			while (!shell.isDisposed() && !dialog.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseUp(MouseEvent event) {
	}

	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		ps.setStatusCoord(ps.getCurrentPosition());
		ps.clearRubberbandSelection();
		ps.addRubberbandSelection(new TextFigure(
				settings.commonForegroundColor, settings.commonFont, drawText,
				event.x, event.y));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * 2D Rectangle object
 */
class TextFigure extends Figure {
	private Color color;
	private Font font;
	private String text;
	private int x, y;

	/**
	 * Constructs a TextFigure
	 * 
	 * @param color
	 *            the color for this object
	 * @param font
	 *            the font for this object
	 * @param text
	 *            the text to draw, tab and new-line expansion is performed
	 * @param x
	 *            the virtual X coordinate of the top-left corner of the text
	 *            bounding box
	 * @param y
	 *            the virtual Y coordinate of the top-left corner of the text
	 *            bounding box
	 */
	public TextFigure(Color color, Font font, String text, int x, int y) {
		this.color = color;
		this.font = font;
		this.text = text;
		this.x = x;
		this.y = y;
	}

	public void draw(FigureDrawContext fdc) {
		Point p = fdc.toClientPoint(x, y);
		fdc.gc.setFont(font);
		fdc.gc.setForeground(color);
		fdc.gc.drawText(text, p.x, p.y, true);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		Font oldFont = fdc.gc.getFont();
		fdc.gc.setFont(font);
		Point textExtent = fdc.gc.textExtent(text);
		fdc.gc.setFont(oldFont);
		region.add(fdc.toClientRectangle(x, y, x + textExtent.x, y
				+ textExtent.y));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * 2D SolidRectangle object
 */
class SolidRoundedRectangleFigure extends Figure {
	private Color color;
	private int x1, y1, x2, y2, diameter;

	/**
	 * Constructs a SolidRectangle These objects are defined by any two
	 * diametrically opposing corners.
	 * 
	 * @param color
	 *            the color for this object
	 * @param x1
	 *            the virtual X coordinate of the first corner
	 * @param y1
	 *            the virtual Y coordinate of the first corner
	 * @param x2
	 *            the virtual X coordinate of the second corner
	 * @param y2
	 *            the virtual Y coordinate of the second corner
	 * @param diameter
	 *            the diameter of curvature of all four corners
	 */
	public SolidRoundedRectangleFigure(Color color, int x1, int y1, int x2,
			int y2, int diameter) {
		this.color = color;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.diameter = diameter;
	}

	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setBackground(color);
		fdc.gc.fillRoundRectangle(r.x, r.y, r.width, r.height, diameter,
				diameter);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 2D SolidRectangle object
 */
class SolidRectangleFigure extends Figure {
	private Color color;
	private int x1, y1, x2, y2;

	/**
	 * Constructs a SolidRectangle These objects are defined by any two
	 * diametrically opposing corners.
	 * 
	 * @param color
	 *            the color for this object
	 * @param x1
	 *            the virtual X coordinate of the first corner
	 * @param y1
	 *            the virtual Y coordinate of the first corner
	 * @param x2
	 *            the virtual X coordinate of the second corner
	 * @param y2
	 *            the virtual Y coordinate of the second corner
	 */
	public SolidRectangleFigure(Color color, int x1, int y1, int x2, int y2) {
		this.color = color;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setBackground(color);
		fdc.gc.fillRectangle(r.x, r.y, r.width, r.height);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 2D Line object
 */
class SolidPolygonFigure extends Figure {
	private Color color;
	private int[] points;

	/**
	 * Constructs a SolidPolygon These objects are defined by a sequence of
	 * vertices.
	 * 
	 * @param color
	 *            the color for this object
	 * @param vertices
	 *            the array of vertices making up the polygon
	 * @param numPoint
	 *            the number of valid points in the array (n >= 3)
	 */
	public SolidPolygonFigure(Color color, Point[] vertices, int numPoints) {
		this.color = color;
		this.points = new int[numPoints * 2];
		for (int i = 0; i < numPoints; ++i) {
			points[i * 2] = vertices[i].x;
			points[i * 2 + 1] = vertices[i].y;
		}
	}

	public void draw(FigureDrawContext fdc) {
		int[] drawPoints = new int[points.length];
		for (int i = 0; i < points.length; i += 2) {
			drawPoints[i] = points[i] * fdc.xScale - fdc.xOffset;
			drawPoints[i + 1] = points[i + 1] * fdc.yScale - fdc.yOffset;
		}
		fdc.gc.setBackground(color);
		fdc.gc.fillPolygon(drawPoints);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		int xmin = Integer.MAX_VALUE, ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE, ymax = Integer.MIN_VALUE;

		for (int i = 0; i < points.length; i += 2) {
			if (points[i] < xmin)
				xmin = points[i];
			if (points[i] > xmax)
				xmax = points[i];
			if (points[i + 1] < ymin)
				ymin = points[i + 1];
			if (points[i + 1] > ymax)
				ymax = points[i + 1];
		}
		region.add(fdc.toClientRectangle(xmin, ymin, xmax, ymax));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 2D Solid Ellipse object
 */
class SolidEllipseFigure extends Figure {
	private Color color;
	private int x1, y1, x2, y2;

	/**
	 * Constructs a SolidEllipse These objects are defined by any two
	 * diametrically opposing corners of a box bounding the ellipse.
	 * 
	 * @param color
	 *            the color for this object
	 * @param x1
	 *            the virtual X coordinate of the first corner
	 * @param y1
	 *            the virtual Y coordinate of the first corner
	 * @param x2
	 *            the virtual X coordinate of the second corner
	 * @param y2
	 *            the virtual Y coordinate of the second corner
	 */
	public SolidEllipseFigure(Color color, int x1, int y1, int x2, int y2) {
		this.color = color;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setBackground(color);
		fdc.gc.fillOval(r.x, r.y, r.width, r.height);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * The superclass for paint tools that contruct objects from individually picked
 * segments.
 */
abstract class SegmentedPaintSession extends BasicPaintSession {
	/**
	 * The set of control points making up the segmented selection
	 */
	private Vector /* of Point */controlPoints = new Vector();

	/**
	 * The previous figure (so that we can abort with right-button)
	 */
	private Figure previousFigure = null;

	/**
	 * The current figure (so that we can abort with right-button)
	 */
	private Figure currentFigure = null;

	/**
	 * Constructs a PaintSession.
	 * 
	 * @param paintSurface
	 *            the drawing surface to use
	 */
	protected SegmentedPaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface()
				.setStatusMessage(
						PaintExample
								.getResourceString("session.SegmentedInteractivePaint.message.anchorMode"));
		previousFigure = null;
		currentFigure = null;
		controlPoints.clear();
	}

	/**
	 * Deactivates the tool.
	 */
	public void endSession() {
		getPaintSurface().clearRubberbandSelection();
		if (previousFigure != null)
			getPaintSurface().drawFigure(previousFigure);
	}

	/**
	 * Resets the tool. Aborts any operation in progress.
	 */
	public void resetSession() {
		getPaintSurface().clearRubberbandSelection();
		if (previousFigure != null)
			getPaintSurface().drawFigure(previousFigure);

		getPaintSurface()
				.setStatusMessage(
						PaintExample
								.getResourceString("session.SegmentedInteractivePaint.message.anchorMode"));
		previousFigure = null;
		currentFigure = null;
		controlPoints.clear();
	}

	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseDown(MouseEvent event) {
		if (event.button != 1)
			return;

		getPaintSurface()
				.setStatusMessage(
						PaintExample
								.getResourceString("session.SegmentedInteractivePaint.message.interactiveMode"));
		previousFigure = currentFigure;

		if (controlPoints.size() > 0) {
			final Point lastPoint = (Point) controlPoints
					.elementAt(controlPoints.size() - 1);
			if (lastPoint.x == event.x || lastPoint.y == event.y)
				return; // spurious event
		}
		controlPoints.add(new Point(event.x, event.y));
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
		if (event.button != 1)
			return;
		if (controlPoints.size() >= 2) {
			getPaintSurface().clearRubberbandSelection();
			previousFigure = createFigure(
					(Point[]) controlPoints.toArray(new Point[controlPoints
							.size()]), controlPoints.size(), true);
		}
		resetSession();
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseUp(MouseEvent event) {
		if (event.button != 1) {
			resetSession(); // abort if right or middle mouse button pressed
			return;
		}
	}

	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		if (controlPoints.size() == 0) {
			ps.setStatusCoord(ps.getCurrentPosition());
			return; // spurious event
		} else {
			ps.setStatusCoordRange(
					(Point) controlPoints.elementAt(controlPoints.size() - 1),
					ps.getCurrentPosition());
		}

		ps.clearRubberbandSelection();
		Point[] points = (Point[]) controlPoints
				.toArray(new Point[controlPoints.size() + 1]);
		points[controlPoints.size()] = ps.getCurrentPosition();
		currentFigure = createFigure(points, points.length, false);
		ps.addRubberbandSelection(currentFigure);
	}

	/**
	 * Template Method: Creates a Figure for drawing rubberband entities and the
	 * final product
	 * 
	 * @param points
	 *            the array of control points
	 * @param numPoints
	 *            the number of valid points in the array (n >= 2)
	 * @param closed
	 *            true if the user double-clicked on the final control point
	 */
	protected abstract Figure createFigure(Point[] points, int numPoints,
			boolean closed);
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * A drawing tool.
 */
class RoundedRectangleTool extends DragPaintSession implements PaintTool {
	private ToolSettings settings;

	/**
	 * Constructs a RoundedRectangleTool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public RoundedRectangleTool(ToolSettings toolSettings,
			PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.RoundedRectangle.label");
	}

	/*
	 * Template methods for drawing
	 */
	protected Figure createFigure(Point a, Point b) {
		ContainerFigure container = new ContainerFigure();
		if (settings.commonFillType != ToolSettings.ftNone)
			container.add(new SolidRoundedRectangleFigure(
					settings.commonBackgroundColor, a.x, a.y, b.x, b.y,
					settings.roundedRectangleCornerDiameter));
		if (settings.commonFillType != ToolSettings.ftSolid)
			container
					.add(new RoundedRectangleFigure(
							settings.commonForegroundColor,
							settings.commonBackgroundColor,
							settings.commonLineStyle, a.x, a.y, b.x, b.y,
							settings.roundedRectangleCornerDiameter));
		return container;
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * 2D Rectangle object
 */
class RoundedRectangleFigure extends Figure {
	private Color foregroundColor, backgroundColor;
	private int lineStyle, x1, y1, x2, y2, diameter;

	/**
	 * Constructs a Rectangle These objects are defined by any two diametrically
	 * opposing corners.
	 * 
	 * @param color
	 *            the color for this object
	 * @param lineStyle
	 *            the line style for this object
	 * @param x1
	 *            the virtual X coordinate of the first corner
	 * @param y1
	 *            the virtual Y coordinate of the first corner
	 * @param x2
	 *            the virtual X coordinate of the second corner
	 * @param y2
	 *            the virtual Y coordinate of the second corner
	 * @param diameter
	 *            the diameter of curvature of all four corners
	 */
	public RoundedRectangleFigure(Color foregroundColor, Color backgroundColor,
			int lineStyle, int x1, int y1, int x2, int y2, int diameter) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.lineStyle = lineStyle;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.diameter = diameter;
	}

	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setForeground(foregroundColor);
		fdc.gc.setBackground(backgroundColor);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawRoundRectangle(r.x, r.y, r.width - 1, r.height - 1,
				diameter, diameter);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * A drawing tool.
 */
class RectangleTool extends DragPaintSession implements PaintTool {
	private ToolSettings settings;

	/**
	 * Constructs a RectangleTool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public RectangleTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Rectangle.label");
	}

	/*
	 * Template method for drawing
	 */
	protected Figure createFigure(Point a, Point b) {
		switch (settings.commonFillType) {
		default:
		case ToolSettings.ftNone:
			return new RectangleFigure(settings.commonForegroundColor,
					settings.commonBackgroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y);
		case ToolSettings.ftSolid:
			return new SolidRectangleFigure(settings.commonBackgroundColor,
					a.x, a.y, b.x, b.y);
		case ToolSettings.ftOutline: {
			ContainerFigure container = new ContainerFigure();
			container.add(new SolidRectangleFigure(
					settings.commonBackgroundColor, a.x, a.y, b.x, b.y));
			container.add(new RectangleFigure(settings.commonForegroundColor,
					settings.commonBackgroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y));
			return container;
		}
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 2D Rectangle object
 */
class RectangleFigure extends Figure {
	private Color foregroundColor, backgroundColor;
	private int lineStyle, x1, y1, x2, y2;

	/**
	 * Constructs a Rectangle These objects are defined by any two diametrically
	 * opposing corners.
	 * 
	 * @param color
	 *            the color for this object
	 * @param lineStyle
	 *            the line style for this object
	 * @param x1
	 *            the virtual X coordinate of the first corner
	 * @param y1
	 *            the virtual Y coordinate of the first corner
	 * @param x2
	 *            the virtual X coordinate of the second corner
	 * @param y2
	 *            the virtual Y coordinate of the second corner
	 */
	public RectangleFigure(Color foregroundColor, Color backgroundColor,
			int lineStyle, int x1, int y1, int x2, int y2) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.lineStyle = lineStyle;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setForeground(foregroundColor);
		fdc.gc.setBackground(backgroundColor);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * A polyline drawing tool.
 */
class PolyLineTool extends SegmentedPaintSession implements PaintTool {
	private ToolSettings settings;

	/**
	 * Constructs a PolyLineTool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public PolyLineTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.PolyLine.label");
	}

	/*
	 * Template methods for drawing
	 */
	protected Figure createFigure(Point[] points, int numPoints, boolean closed) {
		ContainerFigure container = new ContainerFigure();
		if (closed && settings.commonFillType != ToolSettings.ftNone
				&& numPoints >= 3) {
			container.add(new SolidPolygonFigure(
					settings.commonBackgroundColor, points, numPoints));
		}
		if (!closed || settings.commonFillType != ToolSettings.ftSolid
				|| numPoints < 3) {
			for (int i = 0; i < numPoints - 1; ++i) {
				final Point a = points[i];
				final Point b = points[i + 1];
				container.add(new LineFigure(settings.commonForegroundColor,
						settings.commonBackgroundColor,
						settings.commonLineStyle, a.x, a.y, b.x, b.y));
			}
			if (closed) {
				final Point a = points[points.length - 1];
				final Point b = points[0];
				container.add(new LineFigure(settings.commonForegroundColor,
						settings.commonBackgroundColor,
						settings.commonLineStyle, a.x, a.y, b.x, b.y));
			}
		}
		return container;
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 2D Point object
 */
class PointFigure extends Figure {
	private Color color;
	private int x, y;

	/**
	 * Constructs a Point
	 * 
	 * @param color
	 *            the color for this object
	 * @param x
	 *            the virtual X coordinate of the first end-point
	 * @param y
	 *            the virtual Y coordinate of the first end-point
	 */
	public PointFigure(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}

	public void draw(FigureDrawContext fdc) {
		Point p = fdc.toClientPoint(x, y);
		fdc.gc.setBackground(color);
		fdc.gc.fillRectangle(p.x, p.y, 1, 1);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x, y, x, y));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * A pencil tool.
 */
class PencilTool extends ContinuousPaintSession implements PaintTool {
	private ToolSettings settings;

	/**
	 * Constructs a pencil tool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param getPaintSurface
	 *            () the PaintSurface we will render on.
	 */
	public PencilTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Pencil.label");
	}

	/*
	 * Template method for drawing
	 */
	public void render(final Point point) {
		final PaintSurface ps = getPaintSurface();
		ps.drawFigure(new PointFigure(settings.commonForegroundColor, point.x,
				point.y));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
interface PaintTool extends PaintSession {
	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings);
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * Manages a simple drawing surface.
 */
class PaintSurface {
	private Point currentPosition = new Point(0, 0);
	private Canvas paintCanvas;

	private PaintSession paintSession;
	private Image image;
	private Image paintImage; // buffer for refresh blits
	private int imageWidth, imageHeight;
	private int visibleWidth, visibleHeight;

	private FigureDrawContext displayFDC = new FigureDrawContext();
	private FigureDrawContext imageFDC = new FigureDrawContext();
	private FigureDrawContext paintFDC = new FigureDrawContext();

	/* Rubberband */
	private ContainerFigure rubberband = new ContainerFigure();
	// the active rubberband selection
	private int rubberbandHiddenNestingCount = 0;
	// always >= 0, if > 0 rubberband has been hidden

	/* Status */
	private Text statusText;
	private String statusActionInfo, statusMessageInfo, statusCoordInfo;

	/**
	 * Constructs a PaintSurface.
	 * <p>
	 * paintCanvas must have SWT.NO_REDRAW_RESIZE and SWT.NO_BACKGROUND styles,
	 * and may have SWT.V_SCROLL and/or SWT.H_SCROLL.
	 * </p>
	 * 
	 * @param paintCanvas
	 *            the Canvas object in which to render
	 * @param paintStatus
	 *            the PaintStatus object to use for providing user feedback
	 * @param fillColor
	 *            the color to fill the canvas with initially
	 */
	public PaintSurface(Canvas paintCanvas, Text statusText, Color fillColor) {
		this.paintCanvas = paintCanvas;
		this.statusText = statusText;
		clearStatus();

		/* Set up the drawing surface */
		Rectangle displayRect = paintCanvas.getDisplay().getClientArea();
		imageWidth = displayRect.width;
		imageHeight = displayRect.height;
		image = new Image(paintCanvas.getDisplay(), imageWidth, imageHeight);

		imageFDC.gc = new GC(image);
		imageFDC.gc.setBackground(fillColor);
		imageFDC.gc.fillRectangle(0, 0, imageWidth, imageHeight);
		displayFDC.gc = new GC(paintCanvas);

		/* Initialize the session */
		setPaintSession(null);

		/* Add our listeners */
		paintCanvas.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				displayFDC.gc.dispose();
			}
		});
		paintCanvas.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null)
					paintSession.mouseDown(event);
			}

			public void mouseUp(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null)
					paintSession.mouseUp(event);
			}

			public void mouseDoubleClick(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null)
					paintSession.mouseDoubleClick(event);
			}
		});
		paintCanvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null)
					paintSession.mouseMove(event);
			}
		});
		paintCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if (rubberband.isEmpty()) {
					// Nothing to merge, so we just refresh
					event.gc.drawImage(image, displayFDC.xOffset + event.x,
							displayFDC.yOffset + event.y, event.width,
							event.height, event.x, event.y, event.width,
							event.height);
				} else {
					/*
					 * Avoid flicker when merging overlayed objects by
					 * constructing the image on a backbuffer first, then
					 * blitting it to the screen.
					 */
					// Check that the backbuffer is large enough
					if (paintImage != null) {
						Rectangle rect = paintImage.getBounds();
						if ((event.width + event.x > rect.width)
								|| (event.height + event.y > rect.height)) {
							paintFDC.gc.dispose();
							paintImage.dispose();
							paintImage = null;
						}
					}
					if (paintImage == null) {
						Display display = getDisplay();
						Rectangle rect = display.getClientArea();
						paintImage = new Image(display, Math.max(rect.width,
								event.width + event.x), Math.max(rect.height,
								event.height + event.y));
						paintFDC.gc = new GC(paintImage);
					}
					// Setup clipping and the FDC
					Region clipRegion = new Region();
					event.gc.getClipping(clipRegion);
					paintFDC.gc.setClipping(clipRegion);
					clipRegion.dispose();

					paintFDC.xOffset = displayFDC.xOffset;
					paintFDC.yOffset = displayFDC.yOffset;
					paintFDC.xScale = displayFDC.xScale;
					paintFDC.yScale = displayFDC.yScale;

					// Merge the overlayed objects into the image, then blit
					paintFDC.gc.drawImage(image, displayFDC.xOffset + event.x,
							displayFDC.yOffset + event.y, event.width,
							event.height, event.x, event.y, event.width,
							event.height);
					rubberband.draw(paintFDC);
					event.gc.drawImage(paintImage, event.x, event.y,
							event.width, event.height, event.x, event.y,
							event.width, event.height);
				}
			}
		});
		paintCanvas.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				handleResize();
			}
		});

		/* Set up the paint canvas scroll bars */
		ScrollBar horizontal = paintCanvas.getHorizontalBar();
		horizontal.setVisible(true);
		horizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollHorizontally((ScrollBar) event.widget);
			}
		});
		ScrollBar vertical = paintCanvas.getVerticalBar();
		vertical.setVisible(true);
		vertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollVertically((ScrollBar) event.widget);
			}
		});
		handleResize();
	}

	/**
	 * Disposes of the PaintSurface's resources.
	 */
	public void dispose() {
		imageFDC.gc.dispose();
		image.dispose();
		if (paintImage != null) {
			paintImage.dispose();
			paintFDC.gc.dispose();
		}

		currentPosition = null;
		paintCanvas = null;
		paintSession = null;
		image = null;
		paintImage = null;
		displayFDC = null;
		imageFDC = null;
		paintFDC = null;
		rubberband = null;
		statusText = null;
		statusActionInfo = null;
		statusMessageInfo = null;
		statusCoordInfo = null;
	}

	/**
	 * Called when we must grab focus.
	 */
	public void setFocus() {
		paintCanvas.setFocus();
	}

	/**
	 * Returns the Display on which the PaintSurface resides.
	 * 
	 * @return the Display
	 */
	public Display getDisplay() {
		return paintCanvas.getDisplay();
	}

	/**
	 * Returns the Shell in which the PaintSurface resides.
	 * 
	 * @return the Shell
	 */
	public Shell getShell() {
		return paintCanvas.getShell();
	}

	/**
	 * Sets the current paint session.
	 * <p>
	 * If oldPaintSession != paintSession calls oldPaintSession.end() and
	 * paintSession.begin()
	 * </p>
	 * 
	 * @param paintSession
	 *            the paint session to activate; null to disable all sessions
	 */
	public void setPaintSession(PaintSession paintSession) {
		if (this.paintSession != null) {
			if (this.paintSession == paintSession)
				return;
			this.paintSession.endSession();
		}
		this.paintSession = paintSession;
		clearStatus();
		if (paintSession != null) {
			setStatusAction(paintSession.getDisplayName());
			paintSession.beginSession();
		} else {
			setStatusAction(PaintExample.getResourceString("tool.Null.label"));
			setStatusMessage(PaintExample
					.getResourceString("session.Null.message"));
		}
	}

	/**
	 * Returns the current paint session.
	 * 
	 * @return the current paint session, null if none is active
	 */
	public PaintSession getPaintSession() {
		return paintSession;
	}

	/**
	 * Returns the current paint tool.
	 * 
	 * @return the current paint tool, null if none is active (though some other
	 *         session might be)
	 */
	public PaintTool getPaintTool() {
		return (paintSession != null && paintSession instanceof PaintTool) ? (PaintTool) paintSession
				: null;
	}

	/**
	 * Returns the current position in an interactive operation.
	 * 
	 * @return the last known position of the pointer
	 */
	public Point getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Draws a Figure object to the screen and to the backing store permanently.
	 * 
	 * @param object
	 *            the object to draw onscreen
	 */
	public void drawFigure(Figure object) {
		object.draw(imageFDC);
		object.draw(displayFDC);
	}

	/**
	 * Adds a Figure object to the active rubberband selection.
	 * <p>
	 * This object will be drawn to the screen as a preview and refreshed
	 * appropriately until the selection is either cleared or committed.
	 * </p>
	 * 
	 * @param object
	 *            the object to add to the selection
	 */
	public void addRubberbandSelection(Figure object) {
		rubberband.add(object);
		if (!isRubberbandHidden())
			object.draw(displayFDC);
	}

	/**
	 * Clears the active rubberband selection.
	 * <p>
	 * Erases any rubberband objects on the screen then clears the selection.
	 * </p>
	 */
	public void clearRubberbandSelection() {
		if (!isRubberbandHidden()) {
			Region region = new Region();
			rubberband.addDamagedRegion(displayFDC, region);
			Rectangle r = region.getBounds();
			paintCanvas.redraw(r.x, r.y, r.width, r.height, true);
			region.dispose();
		}
		rubberband.clear();

	}

	/**
	 * Commits the active rubberband selection.
	 * <p>
	 * Redraws any rubberband objects on the screen as permanent objects then
	 * clears the selection.
	 * </p>
	 */
	public void commitRubberbandSelection() {
		rubberband.draw(imageFDC);
		if (isRubberbandHidden())
			rubberband.draw(displayFDC);
		rubberband.clear();
	}

	/**
	 * Hides the rubberband (but does not eliminate it).
	 * <p>
	 * Increments by one the rubberband "hide" nesting count. The rubberband is
	 * hidden from view (but remains active) if it wasn't already hidden.
	 * </p>
	 */
	public void hideRubberband() {
		if (rubberbandHiddenNestingCount++ <= 0) {
			Region region = new Region();
			rubberband.addDamagedRegion(displayFDC, region);
			Rectangle r = region.getBounds();
			paintCanvas.redraw(r.x, r.y, r.width, r.height, true);
			region.dispose();
		}
	}

	/**
	 * Shows (un-hides) the rubberband.
	 * <p>
	 * Decrements by one the rubberband "hide" nesting count. The rubberband is
	 * only made visible when showRubberband() has been called once for each
	 * previous hideRubberband(). It is not permitted to call showRubberband()
	 * if the rubber band is not presently hidden.
	 * </p>
	 */
	public void showRubberband() {
		if (rubberbandHiddenNestingCount <= 0)
			throw new IllegalStateException("rubberbandHiddenNestingCount > 0");
		if (--rubberbandHiddenNestingCount == 0) {
			rubberband.draw(displayFDC);
		}
	}

	/**
	 * Determines if the rubberband is hidden.
	 * 
	 * @return true iff the rubber is hidden
	 */
	public boolean isRubberbandHidden() {
		return rubberbandHiddenNestingCount > 0;
	}

	/**
	 * Handles a horizontal scroll event
	 * 
	 * @param scrollbar
	 *            the horizontal scroll bar that posted this event
	 */
	public void scrollHorizontally(ScrollBar scrollBar) {
		if (image == null)
			return;
		if (imageWidth > visibleWidth) {
			final int oldOffset = displayFDC.xOffset;
			final int newOffset = Math.min(scrollBar.getSelection(), imageWidth
					- visibleWidth);
			if (oldOffset != newOffset) {
				paintCanvas.update();
				displayFDC.xOffset = newOffset;
				paintCanvas.scroll(Math.max(oldOffset - newOffset, 0), 0,
						Math.max(newOffset - oldOffset, 0), 0, visibleWidth,
						visibleHeight, false);
			}
		}
	}

	/**
	 * Handles a vertical scroll event
	 * 
	 * @param scrollbar
	 *            the vertical scroll bar that posted this event
	 */
	public void scrollVertically(ScrollBar scrollBar) {
		if (image == null)
			return;
		if (imageHeight > visibleHeight) {
			final int oldOffset = displayFDC.yOffset;
			final int newOffset = Math.min(scrollBar.getSelection(),
					imageHeight - visibleHeight);
			if (oldOffset != newOffset) {
				paintCanvas.update();
				displayFDC.yOffset = newOffset;
				paintCanvas.scroll(0, Math.max(oldOffset - newOffset, 0), 0,
						Math.max(newOffset - oldOffset, 0), visibleWidth,
						visibleHeight, false);
			}
		}
	}

	/**
	 * Handles resize events
	 */
	private void handleResize() {
		paintCanvas.update();

		Rectangle visibleRect = paintCanvas.getClientArea();
		visibleWidth = visibleRect.width;
		visibleHeight = visibleRect.height;

		ScrollBar horizontal = paintCanvas.getHorizontalBar();
		if (horizontal != null) {
			displayFDC.xOffset = Math.min(horizontal.getSelection(), imageWidth
					- visibleWidth);
			if (imageWidth <= visibleWidth) {
				horizontal.setEnabled(false);
				horizontal.setSelection(0);
			} else {
				horizontal.setEnabled(true);
				horizontal.setValues(displayFDC.xOffset, 0, imageWidth,
						visibleWidth, 8, visibleWidth);
			}
		}

		ScrollBar vertical = paintCanvas.getVerticalBar();
		if (vertical != null) {
			displayFDC.yOffset = Math.min(vertical.getSelection(), imageHeight
					- visibleHeight);
			if (imageHeight <= visibleHeight) {
				vertical.setEnabled(false);
				vertical.setSelection(0);
			} else {
				vertical.setEnabled(true);
				vertical.setValues(displayFDC.yOffset, 0, imageHeight,
						visibleHeight, 8, visibleHeight);
			}
		}
	}

	/**
	 * Virtualizes MouseEvent coordinates and stores the current position.
	 */
	private void processMouseEventCoordinates(MouseEvent event) {
		currentPosition.x = event.x = Math.min(Math.max(event.x, 0),
				visibleWidth - 1) + displayFDC.xOffset;
		currentPosition.y = event.y = Math.min(Math.max(event.y, 0),
				visibleHeight - 1) + displayFDC.yOffset;
	}

	/**
	 * Clears the status bar.
	 */
	public void clearStatus() {
		statusActionInfo = "";
		statusMessageInfo = "";
		statusCoordInfo = "";
		updateStatus();
	}

	/**
	 * Sets the status bar action text.
	 * 
	 * @param action
	 *            the action in progress, null to clear
	 */
	public void setStatusAction(String action) {
		statusActionInfo = (action != null) ? action : "";
		updateStatus();
	}

	/**
	 * Sets the status bar message text.
	 * 
	 * @param message
	 *            the message to display, null to clear
	 */
	public void setStatusMessage(String message) {
		statusMessageInfo = (message != null) ? message : "";
		updateStatus();
	}

	/**
	 * Sets the coordinates in the status bar.
	 * 
	 * @param coord
	 *            the coordinates to display, null to clear
	 */
	public void setStatusCoord(Point coord) {
		statusCoordInfo = (coord != null) ? PaintExample.getResourceString(
				"status.Coord.format", new Object[] { new Integer(coord.x),
						new Integer(coord.y) }) : "";
		updateStatus();
	}

	/**
	 * Sets the coordinate range in the status bar.
	 * 
	 * @param a
	 *            the "from" coordinate, must not be null
	 * @param b
	 *            the "to" coordinate, must not be null
	 */
	public void setStatusCoordRange(Point a, Point b) {
		statusCoordInfo = PaintExample.getResourceString(
				"status.CoordRange.format", new Object[] { new Integer(a.x),
						new Integer(a.y), new Integer(b.x), new Integer(b.y) });
		updateStatus();
	}

	/**
	 * Updates the display.
	 */
	private void updateStatus() {
		statusText.setText(PaintExample.getResourceString("status.Bar.format",
				new Object[] { statusActionInfo, statusMessageInfo,
						statusCoordInfo }));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * Manages an interactive paint session. Note that the coordinates received via
 * the listener interfaces are virtualized to zero-origin relative to the
 * painting surface.
 */
interface PaintSession extends MouseListener, MouseMoveListener {
	/**
	 * Returns the paint surface associated with this paint session
	 * 
	 * @return the associated PaintSurface
	 */
	public PaintSurface getPaintSurface();

	/**
	 * Activates the session.
	 * 
	 * Note: When overriding this method, call super.beginSession() at method
	 * start.
	 */
	public abstract void beginSession();

	/**
	 * Deactivates the session.
	 * 
	 * Note: When overriding this method, call super.endSession() at method
	 * exit.
	 */
	public abstract void endSession();

	/**
	 * Resets the session. Aborts any operation in progress.
	 * 
	 * Note: When overriding this method, call super.resetSession() at method
	 * exit.
	 */
	public abstract void resetSession();

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName();
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * A line drawing tool
 */
class LineTool extends DragPaintSession implements PaintTool {
	private ToolSettings settings;

	/**
	 * Constructs a LineTool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public LineTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Line.label");
	}

	/*
	 * Template methods for drawing
	 */
	protected Figure createFigure(Point a, Point b) {
		return new LineFigure(settings.commonForegroundColor,
				settings.commonBackgroundColor, settings.commonLineStyle, a.x,
				a.y, b.x, b.y);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * 2D Line object
 */
class LineFigure extends Figure {
	private Color foregroundColor, backgroundColor;
	private int lineStyle, x1, y1, x2, y2;

	/**
	 * Constructs a Line These objects are defined by their two end-points.
	 * 
	 * @param color
	 *            the color for this object
	 * @param lineStyle
	 *            the line style for this object
	 * @param x1
	 *            the virtual X coordinate of the first end-point
	 * @param y1
	 *            the virtual Y coordinate of the first end-point
	 * @param x2
	 *            the virtual X coordinate of the second end-point
	 * @param y2
	 *            the virtual Y coordinate of the second end-point
	 */
	public LineFigure(Color foregroundColor, Color backgroundColor,
			int lineStyle, int x1, int y1, int x2, int y2) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.lineStyle = lineStyle;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void draw(FigureDrawContext fdc) {
		Point p1 = fdc.toClientPoint(x1, y1);
		Point p2 = fdc.toClientPoint(x2, y2);
		fdc.gc.setForeground(foregroundColor);
		fdc.gc.setBackground(backgroundColor);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
class FigureDrawContext {
	/*
	 * <p> The GC must be set up as follows (it will be returned to this state
	 * upon completion of drawing operations) <ul> <li>setXORMode(false) </ul>
	 * </p>
	 */
	public GC gc = null;
	public int xOffset = 0, yOffset = 0; // substract to get GC coords
	public int xScale = 1, yScale = 1;

	public Rectangle toClientRectangle(int x1, int y1, int x2, int y2) {
		return new Rectangle(Math.min(x1, x2) * xScale - xOffset, Math.min(y1,
				y2) * yScale - yOffset, (Math.abs(x2 - x1) + 1) * xScale,
				(Math.abs(y2 - y1) + 1) * yScale);
	}

	public Point toClientPoint(int x, int y) {
		return new Point(x * xScale - xOffset, y * yScale - yOffset);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * Superinterface for all drawing objects. All drawing objects know how to
 * render themselved to the screen and can draw a temporary version of
 * themselves for previewing the general appearance of the object onscreen
 * before it gets committed.
 */
abstract class Figure {
	/**
	 * Draws this object.
	 * 
	 * @param fdc
	 *            a parameter block specifying drawing-related information
	 */
	public abstract void draw(FigureDrawContext fdc);

	/**
	 * Computes the damaged screen region caused by drawing this object
	 * (imprecise), then appends it to the supplied region.
	 * 
	 * @param fdc
	 *            a parameter block specifying drawing-related information
	 * @param region
	 *            a region to which additional damage areas will be added
	 */
	public abstract void addDamagedRegion(FigureDrawContext fdc, Region region);
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * A drawing tool.
 */
class EllipseTool extends DragPaintSession implements PaintTool {
	private ToolSettings settings;

	/**
	 * Constructs a EllipseTool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public EllipseTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Ellipse.label");
	}

	/*
	 * Template methods for drawing
	 */
	protected Figure createFigure(Point a, Point b) {
		ContainerFigure container = new ContainerFigure();
		if (settings.commonFillType != ToolSettings.ftNone)
			container.add(new SolidEllipseFigure(
					settings.commonBackgroundColor, a.x, a.y, b.x, b.y));
		if (settings.commonFillType != ToolSettings.ftSolid)
			container.add(new EllipseFigure(settings.commonForegroundColor,
					settings.commonBackgroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y));
		return container;
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * 2D Ellipse object
 */
class EllipseFigure extends Figure {
	private Color foregroundColor, backgroundColor;
	private int lineStyle, x1, y1, x2, y2;

	/**
	 * Constructs an Ellipse These objects are defined by any two diametrically
	 * opposing corners of a box bounding the ellipse.
	 * 
	 * @param color
	 *            the color for this object
	 * @param lineStyle
	 *            the line style for this object
	 * @param x1
	 *            the virtual X coordinate of the first corner
	 * @param y1
	 *            the virtual Y coordinate of the first corner
	 * @param x2
	 *            the virtual X coordinate of the second corner
	 * @param y2
	 *            the virtual Y coordinate of the second corner
	 */
	public EllipseFigure(Color foregroundColor, Color backgroundColor,
			int lineStyle, int x1, int y1, int x2, int y2) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.lineStyle = lineStyle;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setForeground(foregroundColor);
		fdc.gc.setBackground(backgroundColor);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawOval(r.x, r.y, r.width - 1, r.height - 1);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * The superclass for paint tools that use click-drag-release motions to draw
 * objects.
 */
abstract class DragPaintSession extends BasicPaintSession {
	/**
	 * True if a click-drag is in progress
	 */
	private boolean dragInProgress = false;

	/**
	 * The position of the first click in a click-drag
	 */
	private Point anchorPosition = new Point(-1, -1);

	/**
	 * A temporary point
	 */
	private Point tempPosition = new Point(-1, -1);

	/**
	 * Constructs a PaintSession.
	 * 
	 * @param getPaintSurface
	 *            () the drawing surface to use
	 */
	protected DragPaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface()
				.setStatusMessage(
						PaintExample
								.getResourceString("session.DragInteractivePaint.message"));
		anchorPosition.x = -1;
		dragInProgress = false;
	}

	/**
	 * Deactivates the tool.
	 */
	public void endSession() {
	}

	/**
	 * Resets the tool. Aborts any operation in progress.
	 */
	public void resetSession() {
		getPaintSurface().clearRubberbandSelection();
		anchorPosition.x = -1;
		dragInProgress = false;
	}

	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseDown(MouseEvent event) {
		if (event.button != 1)
			return;
		if (dragInProgress)
			return; // spurious event
		dragInProgress = true;

		anchorPosition.x = event.x;
		anchorPosition.y = event.y;
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseUp(MouseEvent event) {
		if (event.button != 1) {
			resetSession(); // abort if right or middle mouse button pressed
			return;
		}
		if (!dragInProgress)
			return; // spurious event
		dragInProgress = false;
		if (anchorPosition.x == -1)
			return; // spurious event

		getPaintSurface().commitRubberbandSelection();
	}

	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		if (!dragInProgress) {
			ps.setStatusCoord(ps.getCurrentPosition());
			return;
		}
		ps.setStatusCoordRange(anchorPosition, ps.getCurrentPosition());
		ps.clearRubberbandSelection();
		tempPosition.x = event.x;
		tempPosition.y = event.y;
		ps.addRubberbandSelection(createFigure(anchorPosition, tempPosition));
	}

	/**
	 * Template Method: Creates a Figure for drawing rubberband entities and the
	 * final product
	 * 
	 * @param anchor
	 *            the anchor point
	 * @param cursor
	 *            the point marking the current pointer location
	 */
	protected abstract Figure createFigure(Point anchor, Point cursor);
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * The superclass for paint tools that draw continuously along the path traced
 * by the mouse's movement while the button is depressed
 */
abstract class ContinuousPaintSession extends BasicPaintSession {
	/**
	 * True if a click-drag is in progress.
	 */
	private boolean dragInProgress = false;

	/**
	 * A cached Point array for drawing.
	 */
	private Point[] points = new Point[] { new Point(-1, -1), new Point(-1, -1) };

	/**
	 * The time to wait between retriggers in milliseconds.
	 */
	private int retriggerInterval = 0;

	/**
	 * The currently valid RetriggerHandler
	 */
	protected Runnable retriggerHandler = null;

	/**
	 * Constructs a ContinuousPaintSession.
	 * 
	 * @param paintSurface
	 *            the drawing surface to use
	 */
	protected ContinuousPaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Sets the retrigger timer.
	 * <p>
	 * After the timer elapses, if the mouse is still hovering over the same
	 * point with the drag button pressed, a new render order is issued and the
	 * timer is restarted.
	 * </p>
	 * 
	 * @param interval
	 *            the time in milliseconds to wait between retriggers, 0 to
	 *            disable
	 */
	public void setRetriggerTimer(int interval) {
		retriggerInterval = interval;
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().setStatusMessage(
				PaintExample
						.getResourceString("session.ContinuousPaint.message"));
		dragInProgress = false;
	}

	/**
	 * Deactivates the tool.
	 */
	public void endSession() {
		abortRetrigger();
	}

	/**
	 * Aborts the current operation.
	 */
	public void resetSession() {
		abortRetrigger();
	}

	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public final void mouseDown(MouseEvent event) {
		if (event.button != 1)
			return;
		if (dragInProgress)
			return; // spurious event
		dragInProgress = true;

		points[0].x = event.x;
		points[0].y = event.y;
		render(points[0]);
		prepareRetrigger();
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public final void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public final void mouseUp(MouseEvent event) {
		if (event.button != 1)
			return;
		if (!dragInProgress)
			return; // spurious event
		abortRetrigger();
		mouseSegmentFinished(event);
		dragInProgress = false;
	}

	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	public final void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		ps.setStatusCoord(ps.getCurrentPosition());
		if (!dragInProgress)
			return;
		mouseSegmentFinished(event);
		prepareRetrigger();
	}

	/**
	 * Handle a rendering segment
	 * 
	 * @param event
	 *            the mouse event detail information
	 */
	private final void mouseSegmentFinished(MouseEvent event) {
		if (points[0].x == -1)
			return; // spurious event
		if (points[0].x != event.x || points[0].y != event.y) {
			// draw new segment
			points[1].x = event.x;
			points[1].y = event.y;
			renderContinuousSegment();
		}
	}

	/**
	 * Draws a continuous segment from points[0] to points[1]. Assumes points[0]
	 * has been drawn already.
	 * 
	 * @post points[0] will refer to the same point as points[1]
	 */
	protected void renderContinuousSegment() {
		/* A lazy but effective line drawing algorithm */
		final int dX = points[1].x - points[0].x;
		final int dY = points[1].y - points[0].y;
		int absdX = Math.abs(dX);
		int absdY = Math.abs(dY);

		if ((dX == 0) && (dY == 0))
			return;

		if (absdY > absdX) {
			final int incfpX = (dX << 16) / absdY;
			final int incY = (dY > 0) ? 1 : -1;
			int fpX = points[0].x << 16; // X in fixedpoint format

			while (--absdY >= 0) {
				points[0].y += incY;
				points[0].x = (fpX += incfpX) >> 16;
				render(points[0]);
			}
			if (points[0].x == points[1].x)
				return;
			points[0].x = points[1].x;
		} else {
			final int incfpY = (dY << 16) / absdX;
			final int incX = (dX > 0) ? 1 : -1;
			int fpY = points[0].y << 16; // Y in fixedpoint format

			while (--absdX >= 0) {
				points[0].x += incX;
				points[0].y = (fpY += incfpY) >> 16;
				render(points[0]);
			}
			if (points[0].y == points[1].y)
				return;
			points[0].y = points[1].y;
		}
		render(points[0]);
	}

	/**
	 * Prepare the retrigger timer
	 */
	private final void prepareRetrigger() {
		if (retriggerInterval > 0) {
			/*
			 * timerExec() provides a lightweight mechanism for running code at
			 * intervals from within the event loop when timing accuracy is not
			 * important.
			 * 
			 * Since it is not possible to cancel a timerExec(), we remember the
			 * Runnable that is active in order to distinguish the valid one
			 * from the stale ones. In practice, if the interval is 1/100th of a
			 * second, then creating a few hundred new RetriggerHandlers each
			 * second will not cause a significant performance hit.
			 */
			Display display = getPaintSurface().getDisplay();
			retriggerHandler = new Runnable() {
				public void run() {
					if (retriggerHandler == this) {
						render(points[0]);
						prepareRetrigger();
					}
				}
			};
			display.timerExec(retriggerInterval, retriggerHandler);
		}
	}

	/**
	 * Aborts the retrigger timer
	 */
	private final void abortRetrigger() {
		retriggerHandler = null;
	}

	/**
	 * Template method: Renders a point.
	 * 
	 * @param point
	 *            , the point to render
	 */
	protected abstract void render(Point point);
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * Container for Figure objects with stacking preview mechanism.
 */
class ContainerFigure extends Figure {
	private static final int INITIAL_ARRAY_SIZE = 16;

	Figure[] objectStack = null;
	int nextIndex = 0;

	/**
	 * Constructs an empty Container
	 */
	public ContainerFigure() {
	}

	/**
	 * Adds an object to the container for later drawing.
	 * 
	 * @param object
	 *            the object to add to the drawing list
	 */
	public void add(Figure object) {
		if (objectStack == null) {
			objectStack = new Figure[INITIAL_ARRAY_SIZE];
		} else if (objectStack.length <= nextIndex) {
			Figure[] newObjectStack = new Figure[objectStack.length * 2];
			System.arraycopy(objectStack, 0, newObjectStack, 0,
					objectStack.length);
			objectStack = newObjectStack;
		}
		objectStack[nextIndex] = object;
		++nextIndex;
	}

	/**
	 * Determines if the container is empty.
	 * 
	 * @return true if the container is empty
	 */
	public boolean isEmpty() {
		return nextIndex == 0;
	}

	/**
	 * Adds an object to the container and draws its preview then updates the
	 * supplied preview state.
	 * 
	 * @param object
	 *            the object to add to the drawing list
	 * @param gc
	 *            the GC to draw on
	 * @param offset
	 *            the offset to add to virtual coordinates to get display
	 *            coordinates
	 * @param rememberedState
	 *            the state returned by a previous drawPreview() or
	 *            addAndPreview() using this Container, may be null if there was
	 *            no such previous call
	 * @return object state that must be passed to erasePreview() later to erase
	 *         this object
	 */
	// public Object addAndPreview(Figure object, GC gc, Point offset, Object
	// rememberedState) {
	// Object[] stateStack = (Object[]) rememberedState;
	// if (stateStack == null) {
	// stateStack = new Object[INITIAL_ARRAY_SIZE];
	// } else if (stateStack.length <= nextIndex) {
	// Object[] newStateStack = new Object[stateStack.length * 2];
	// System.arraycopy(stateStack, 0, newStateStack, 0, stateStack.length);
	// stateStack = newStateStack;
	// }
	// add(object);
	// stateStack[nextIndex - 1] = object.drawPreview(gc, offset);
	// return stateStack;
	// }
	/**
	 * Clears the container.
	 * <p>
	 * Note that erasePreview() cannot be called after this point to erase any
	 * previous drawPreview()'s.
	 * </p>
	 */
	public void clear() {
		while (--nextIndex > 0)
			objectStack[nextIndex] = null;
		nextIndex = 0;
	}

	public void draw(FigureDrawContext fdc) {
		for (int i = 0; i < nextIndex; ++i)
			objectStack[i].draw(fdc);
	}

	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		for (int i = 0; i < nextIndex; ++i)
			objectStack[i].addDamagedRegion(fdc, region);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
abstract class BasicPaintSession implements PaintSession {
	/**
	 * The paint surface
	 */
	private PaintSurface paintSurface;

	/**
	 * Constructs a PaintSession.
	 * 
	 * @param paintSurface
	 *            the drawing surface to use
	 */
	protected BasicPaintSession(PaintSurface paintSurface) {
		this.paintSurface = paintSurface;
	}

	/**
	 * Returns the paint surface associated with this paint session.
	 * 
	 * @return the associated PaintSurface
	 */
	public PaintSurface getPaintSurface() {
		return paintSurface;
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * An airbrush tool.
 */
class AirbrushTool extends ContinuousPaintSession implements PaintTool {
	private ToolSettings settings;
	private Random random;
	private int cachedRadiusSquared;
	private int cachedNumPoints;

	/**
	 * Constructs a Tool.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 * @param paintSurface
	 *            the PaintSurface we will render on.
	 */
	public AirbrushTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		random = new Random();
		setRetriggerTimer(10);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings
	 *            the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		// compute things we need to know for drawing
		settings = toolSettings;
		cachedRadiusSquared = settings.airbrushRadius * settings.airbrushRadius;
		cachedNumPoints = 314 * settings.airbrushIntensity
				* cachedRadiusSquared / 250000;
		if (cachedNumPoints == 0 && settings.airbrushIntensity != 0)
			cachedNumPoints = 1;
	}

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Airbrush.label");
	}

	/*
	 * Template method for drawing
	 */
	protected void render(Point point) {
		// Draws a bunch (cachedNumPoints) of random pixels within a specified
		// circle (cachedRadiusSquared).
		ContainerFigure cfig = new ContainerFigure();

		for (int i = 0; i < cachedNumPoints; ++i) {
			int randX, randY;
			do {
				randX = (int) ((random.nextDouble() - 0.5)
						* settings.airbrushRadius * 2.0);
				randY = (int) ((random.nextDouble() - 0.5)
						* settings.airbrushRadius * 2.0);
			} while (randX * randX + randY * randY > cachedRadiusSquared);
			cfig.add(new PointFigure(settings.commonForegroundColor, point.x
					+ randX, point.y + randY));
		}
		getPaintSurface().drawFigure(cfig);
	}
}
