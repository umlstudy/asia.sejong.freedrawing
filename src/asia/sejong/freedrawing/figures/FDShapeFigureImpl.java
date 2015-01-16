package asia.sejong.freedrawing.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.context.ApplicationContext;
import asia.sejong.freedrawing.model.FDElement;
import asia.sejong.freedrawing.model.FDShape;

/**
 * Provides abstract support for a variety of shapes.
 * <p>
 * When customizing shapes, you shouldn't override paintFigure(). Override
 * fillShape() and outlineShape() methods instead.
 */
/**
 * @author fffffff
 *
 */
public abstract class FDShapeFigureImpl extends Figure implements FDShapeFigure {

	/**
	 * The width of this shape's outline.
	 * 
	 * @deprecated Use {@link #setLineWidth(int)} or
	 *             {@link #setLineWidthFloat(float)} instead.
	 */
	protected int lineWidth;

	/**
	 * Private copy of lineWidth field to track changes. We cannot compare to
	 * the float line width because rounding may make them seem equal when they
	 * have actually changed.
	 * 
	 * e.g. someone sets int line width to 5, when float line width was already
	 * 5.4, float line width should change to 5.0, but comparing them as ints
	 * would suggest there's no change to synchronize.
	 */
	private int lastLineWidth;

	/**
	 * The line style to be used for this shape's outline.
	 * 
	 * @deprecated Use {@link #setLineStyle(int)} instead.
	 */
	protected int lineStyle;

	/**
	 * Private copy of lineStyle field to track changes.
	 */
	private int lastLineStyle;

	private LineAttributes lineAttributes;

	private boolean fill;
	private boolean outline;
	private boolean xorFill;
	private boolean xorOutline;
	private Integer antialias;
	private Integer alpha;

	/**
	 * Default constructor.
	 * 
	 * @since 2.0
	 */
	public FDShapeFigureImpl() {
		lineAttributes = new LineAttributes(1.0f);
		fill = true;
		outline = true;
		xorFill = false;
		xorOutline = false;
		antialias = null;
		alpha = null;

		// synchronize parameters
		lineWidth = (int) lineAttributes.width;
		lineStyle = lineAttributes.style;
		lastLineWidth = lineWidth;
		lastLineStyle = lineStyle;
		antialias = SWT.ON;
	}

	/**
	 * Fills the interior of the shape with the background color.
	 * 
	 * @param graphics
	 *            the graphics object
	 */
	protected abstract void fillShape(Graphics graphics);

	/**
	 * Outlines this shape using the foreground color.
	 * 
	 * @param graphics
	 *            the graphics object
	 */
	protected abstract void outlineShape(Graphics graphics);
	
	/**
	 * Paints this Figure and its children.
	 * 
	 * @param graphics
	 *            The Graphics object used for painting
	 * @see #paintFigure(Graphics)
	 * @see #paintClientArea(Graphics)
	 * @see #paintBorder(Graphics)
	 */
	@SuppressWarnings("deprecation")
	public void paint(Graphics graphics) {
		if (getLocalBackgroundColor() != null)
			graphics.setBackgroundColor(getLocalBackgroundColor());
		if (getLocalForegroundColor() != null)
			graphics.setForegroundColor(getLocalForegroundColor());
		if (font != null)
			graphics.setFont(font);

		graphics.pushState();
		try {
			// sejong.lee
			Point centerPoint = positionToCenterZeroAndClip(graphics);

			paintFigure(graphics);
			
			// sejong.lee
			positionRestore(graphics, centerPoint);

			graphics.restoreState();
			paintClientArea(graphics);
			paintBorder(graphics);
		} finally {
			graphics.popState();
		}
	}
	
	/**
	 * Paints the shape. Each shape has an outline to draw, and a region to fill
	 * within that outline. Disabled shapes must visually depict the disabled
	 * state.
	 * 
	 * @see Figure#paintFigure(Graphics)
	 */
	public void paintFigure(Graphics graphics) {
		
		if (antialias != null) {
			graphics.setAntialias(antialias.intValue());
		}

		if (alpha != null) {
			graphics.setAlpha(alpha.intValue());
		}

		/*
		 * see bug #267397: paintFigure was historically not called, disabling
		 * setOpaque() behavior, and it was decided to defend the API's
		 * consistency.
		 */
		// paint background and border
		// super.paintFigure(graphics);
		
		if (!isEnabled()) {
			graphics.translate(1, 1);
			graphics.setBackgroundColor(ColorConstants.buttonLightest);
			graphics.setForegroundColor(ColorConstants.buttonLightest);

			if (fill) {
				paintFill(graphics);
			}

			if (outline) {
				paintOutline(graphics);
			}

			graphics.setBackgroundColor(ColorConstants.buttonDarker);
			graphics.setForegroundColor(ColorConstants.buttonDarker);
			graphics.translate(-1, -1);
		}

		if (fill) {
			paintFill(graphics);
		}

		if (outline) {
			paintOutline(graphics);
		}
	}
	
	private Point positionToCenterZeroAndClip(Graphics graphics) {
		// 드로잉역영을 일부러 크게 만듦 - 최적화 필요
		// 사각형 회전시 최대길이를 한변으로 하는 정사각형 영역을 
		// 드로잉 영역으로 설정함
		Point centerPoint = GeometryUtil.centerPoint(getBounds());
		double h = GeometryUtil.calculateHipotenuse(getBounds().width, getBounds().height);
		graphics.setClip(GeometryUtil.createSquare(centerPoint, (int)h));
		
		graphics.translate(centerPoint.x, centerPoint.y);
		if ( degree > 0 ) {
			graphics.rotate((float)degree);
		}
		System.out.println("DEGREE ? " + degree);
		
		return centerPoint;
	}
	
	private static void positionRestore(Graphics graphics, Point centerPoint) {
		graphics.rotate(0f);
		graphics.translate(-centerPoint.x, -centerPoint.y);
	}
	
	/**
	 * Edit By sejong.lee
	 * @return
	 */
	protected Rectangle getBoundsInZeroPoint() {
		return GeometryUtil.createRectangleCenterIsZero(getBounds());
	}
	
	
	/* (non-Javadoc)
	 * Edit By sejong.lee
	 * @see org.eclipse.draw2d.Figure#repaint()
	 */
	@Override
	public void repaint() {
		if ( getParent() != null ) {
			// 상위 피겨를 취하여
			// 상위 피겨의 특정부분를 리페이트인트 하도록 지정함
			getParent().repaint(GeometryUtil.createSquare(getBounds()));
		} else {
			super.repaint();
		}
	}
	
	/* (non-Javadoc)
	 * Edit By sejong.lee
	 * @see org.eclipse.draw2d.Figure#erase()
	 */
	@Override
	public void erase() {
		if (getParent() == null || !isVisible()) {
			return;
		}

		// 드로잉역영을 일부러 크게 만듦 - 최적화 필요
		// 사각형 회전시 최대길이를 한변으로 하는 정사각형 영역을 
		// 드로잉 영역으로 설정함
		Rectangle r = GeometryUtil.createSquare(getBounds());
		getParent().translateToParent(r);
		getParent().repaint(r.x, r.y, r.width, r.height);
	}

	private void paintOutline(Graphics graphics) {
		
		Color oriforegroundColor = graphics.getForegroundColor();
		if ( lineColor != null ) {
			graphics.setForegroundColor(lineColor);
		}
		
		// synchronize the line width and style attributes to the
		// public fields which may have been assigned
		// to without our knowledge
		lineAttributes.width = getLineWidthFloat();
		lineAttributes.style = getLineStyle();

		graphics.setLineAttributes(lineAttributes);

		if (xorOutline) {
			/*
			 * XORMode is a non-advanced only feature (GDI, not in GDI+ on
			 * windows)
			 * 
			 * Also, XORMode is deprecated in SWT, so this should really be
			 * removed completely at some point. XORMode isn't supported on Mac
			 * OSX at all.
			 */
			boolean oldAdv = graphics.getAdvanced();
			graphics.setAdvanced(false);
			graphics.setXORMode(true);
			outlineShape(graphics);
			graphics.setAdvanced(oldAdv);
		} else {
			outlineShape(graphics);
		}
		
		graphics.setForegroundColor(oriforegroundColor);
	}

	private void paintFill(Graphics graphics) {
		if (xorFill) {
			/*
			 * XORMode is a non-advanced only feature (GDI, not in GDI+ on
			 * windows)
			 * 
			 * Also, XORMode is deprecated in SWT, so this should really be
			 * removed completely at some point. XORMode isn't supported on Mac
			 * OSX at all.
			 */
			boolean oldAdv = graphics.getAdvanced();
			graphics.setAdvanced(false);
			graphics.setXORMode(true);
			fillShape(graphics);
			graphics.setAdvanced(oldAdv);
		} else {
			fillShape(graphics);
		}
	}

	/**
	 * Sets whether this shape should fill its region or not. It repaints this
	 * figure.
	 * 
	 * @param b
	 *            fill state
	 * @since 2.0
	 */
	public void setFill(boolean b) {
		if (fill != b) {
			fill = b;
			repaint();
		}
	}

	/**
	 * Sets whether XOR based fill should be used by the shape. It repaints this
	 * figure.
	 * 
	 * @param b
	 *            XOR fill state
	 * @since 2.0
	 */
	public void setFillXOR(boolean b) {
		if (xorFill != b) {
			xorFill = b;
			repaint();
		}
	}

	/**
	 * Sets whether the outline should be drawn for this shape.
	 * 
	 * @param b
	 *            <code>true</code> if the shape should be outlined
	 * @since 2.0
	 */
	public void setOutline(boolean b) {
		if (outline != b) {
			outline = b;
			repaint();
		}
	}

	/**
	 * Sets whether XOR based outline should be used for this shape.
	 * 
	 * @param b
	 *            <code>true</code> if the outline should be XOR'ed
	 * @since 2.0
	 */
	public void setOutlineXOR(boolean b) {
		if (xorOutline != b) {
			xorOutline = b;
			repaint();
		}
	}

	/**
	 * Sets whether XOR based fill and XOR based outline should be used for this
	 * shape.
	 * 
	 * @param b
	 *            <code>true</code> if the outline and fill should be XOR'ed
	 * @since 2.0
	 */
	public void setXOR(boolean b) {
		xorOutline = xorFill = b;
		repaint();
	}

	/**
	 * @since 3.5
	 */
	public Integer getAlpha() {
		return alpha;
	}

	/**
	 * @since 3.5
	 */
	public Integer getAntialias() {
		return antialias;
	}

	/**
	 * Returns line attributes used when drawing this shape.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes
	 * 
	 *      Performance note: creates and returns a clone.
	 * 
	 * @return current line attributes
	 * @since 3.5
	 */
	public LineAttributes getLineAttributes() {
		return SWTGraphics.clone(lineAttributes);
	}

	/**
	 * Returns the line width of this shape's outline.
	 * 
	 * @return the line width
	 */
	public int getLineWidth() {
		// synchronize lineWidth field for
		// backwards compatibility
		if (lineWidth != lastLineWidth) {
			lineAttributes.width = lineWidth;
			lastLineWidth = lineWidth;
		}

		return (int) lineAttributes.width;
	}

	/**
	 * Returns the line width of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#width
	 * 
	 * @since 3.5
	 */
	public float getLineWidthFloat() {
		// synchronize lineWidth field for
		// backwards compatibility
		if (lineWidth != lastLineWidth) {
			lineAttributes.width = lineWidth;
			lastLineWidth = lineWidth;
		}

		return lineAttributes.width;
	}

	/**
	 * Returns the line join style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#join
	 * 
	 * @since 3.5
	 */
	public int getLineJoin() {
		return lineAttributes.join;
	}

	/**
	 * Returns the line cap style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#cap
	 * 
	 * @since 3.5
	 */
	public int getLineCap() {
		return lineAttributes.cap;
	}

	/**
	 * Returns the line style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#style
	 * 
	 * @return the line style
	 */
	public int getLineStyle() {
		// synchronize line style which may have been assigned
		// to lineStyle field for backwards compatibility
		if (lineStyle != lastLineStyle) {
			lineAttributes.style = lineStyle;
			lastLineStyle = lineStyle;
		}

		return lineAttributes.style;
	}

	/**
	 * Returns the line dash style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#dash
	 * 
	 * @since 3.5
	 */
	public float[] getLineDash() {
		if (lineAttributes.dash != null) {
			return (float[]) lineAttributes.dash.clone();
		} else {
			return null;
		}
	}

	/**
	 * Returns the line dash offset of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#dashOffset
	 * 
	 * @since 3.5
	 */
	public float getLineDashOffset() {
		return lineAttributes.dashOffset;
	}

	/**
	 * Returns the line dash miter limit of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#miterLimit
	 * 
	 * @since 3.5
	 */
	public float getLineMiterLimit() {
		return lineAttributes.miterLimit;
	}

	/**
	 * @since 3.5
	 */
	public void setAlpha(Integer value) {
		if (alpha != null) {
			if (!alpha.equals(value)) {
				alpha = value;
				repaint();
			}
		} else if (value != null) {
			alpha = value;
			repaint();
		}
	}

	/**
	 * @since 3.5
	 */
	public void setAlpha(int value) {
		if (alpha != null) {
			if (alpha.intValue() != value) {
				alpha = new Integer(value);
				repaint();
			}
		} else {
			alpha = new Integer(value);
			repaint();
		}
	}

	/**
	 * @see org.eclipse.swt.graphics.GC#setAntialias(int)
	 * @param value
	 * @since 3.5
	 */
	public void setAntialias(Integer value) {
		if (antialias != null) {
			if (!antialias.equals(value)) {
				antialias = value;
				repaint();
			}
		} else if (value != null) {
			antialias = value;
			repaint();
		}
	}

	/**
	 * @since 3.5
	 */
	public void setAntialias(int value) {
		if (antialias != null) {
			if (antialias.intValue() != value) {
				antialias = new Integer(value);
				repaint();
			}
		} else {
			antialias = new Integer(value);
			repaint();
		}
	}

	/**
	 * Sets all line attributes at once.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes
	 * 
	 * @param la
	 * @since 3.5
	 */
	public void setLineAttributes(LineAttributes la) {
		if (!lineAttributes.equals(la)) {
			SWTGraphics.copyLineAttributes(lineAttributes, la);
			repaint();
		}
	}

	/**
	 * Sets the line width to be used to outline the shape.
	 * 
	 * @param w
	 *            the new width
	 * @since 2.0
	 */
	public void setLineWidth(int w) {
		float _w = w;

		if (lineAttributes.width != _w) {
			lineAttributes.width = _w;

			// synchronize lineWidth fields for
			// backwards compatibility
			lineWidth = w;
			lastLineWidth = w;

			repaint();
		}
	}

	/**
	 * Sets the line width of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#width
	 * 
	 * @param value
	 * @since 3.5
	 */
	public void setLineWidthFloat(float value) {
		if (lineAttributes.width != value) {
			lineAttributes.width = value;

			// synchronize lineWidth fields for
			// backwards compatibility
			lineWidth = (int) value;
			lastLineWidth = (int) value;

			repaint();
		}
	}

	/**
	 * Sets the line join style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#join
	 * 
	 * @param join
	 * @since 3.5
	 */
	public void setLineJoin(int join) {
		if (lineAttributes.join != join) {
			lineAttributes.join = join;
			repaint();
		}
	}

	/**
	 * Sets the line cap style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#cap
	 * 
	 * @param cap
	 * @since 3.5
	 */
	public void setLineCap(int cap) {
		if (lineAttributes.cap != cap) {
			lineAttributes.cap = cap;
			repaint();
		}
	}

	/**
	 * Sets the line style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#style
	 * 
	 * @param style
	 *            the new line style
	 * @since 2.0
	 */
	public void setLineStyle(int style) {
		if (lineAttributes.style != style) {
			lineAttributes.style = style;

			// synchronize the lineStyle field
			// to the lineStyle we actually use
			lineStyle = style;
			lastLineStyle = style;

			repaint();
		}
	}

	/**
	 * Sets the line dash style of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#dash
	 * 
	 * @param dash
	 * @since 3.5
	 */
	public void setLineDash(float[] dash) {
		if ((dash != null) && !dash.equals(lineAttributes.dash)) {
			lineAttributes.dash = (float[]) dash.clone();
			repaint();
		} else if ((dash == null) && (lineAttributes.dash != null)) {
			lineAttributes.dash = null;
			repaint();
		}
	}

	/**
	 * Sets the line dash offset of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#dashOffset
	 * 
	 * @param dashOffset
	 * @since 3.5
	 */
	public void setLineDashOffset(float dashOffset) {
		if (lineAttributes.dashOffset != dashOffset) {
			lineAttributes.dashOffset = dashOffset;
			repaint();
		}
	}

	/**
	 * Sets the line dash miter limit of this shape's outline.
	 * 
	 * @see org.eclipse.swt.graphics.LineAttributes#miterLimit
	 * 
	 * @param miterLimit
	 * @since 3.5
	 */
	public void setLineMiterLimit(float miterLimit) {
		if (lineAttributes.miterLimit != miterLimit) {
			lineAttributes.miterLimit = miterLimit;
			repaint();
		}
	}

	// -----------------------------------------------------------
	// FDShapeFigure Implementation

	private double degree = 0;
	
	@Override
	public final void setAlphaEx(int alpha) {
		this.alpha = alpha;
	}
	
	@Override
	public final void setBackgroundColorEx(RGB rgbColor) {
		Color color = null;
		if ( rgbColor != null ) {
			color = ApplicationContext.getInstance().getColorManager().get(rgbColor);
			setBackgroundColor(color);
		}	
	}

	@Override
	public final void setLineWidthEx(float lineWidth) {
		setLineWidth((int)lineWidth);
	}

	@Override
	public final void setLineStyleEx(int lineStyle) {
		setLineStyle(lineStyle);
	}
	
	private Color lineColor = null;

	@Override
	public final void setLineColorEx(RGB rgbColor) {
		if ( rgbColor != null ) {
			Color color = ApplicationContext.getInstance().getColorManager().get(rgbColor);
			if ( color != lineColor ) {
				lineColor = color;
				repaint();
			}
		}	
	}

	@Override
	public void setModelAttributes(FDElement model_) {
		FDShape model = (FDShape)model_;
		
		setBackgroundColorEx(model.getBackgroundColor());
		setLineWidthEx(model.getLineWidth());
		setLineStyleEx(model.getLineStyle());
		setLineColorEx(model.getLineColor());
		setAlphaEx(model.getAlpha());
		setDegreeEx(model.getDegree());
		setLocationEx(model.getLocation());
		setSizeEx(model.getWidth(), model.getHeight());
	}
	
	@Override
	public final void setLocationEx(Point point) {
		setLocation(point);
	}

	@Override
	public final void setSizeEx(int width, int height) {
		setSize(width, height);
	}

	@Override
	public final void setDegreeEx(double degree) {
		this.degree = degree;
		System.out.println("DE " + degree);
	}

	@Override
	public final double getDegreeEx() {
		return degree;
	}

}