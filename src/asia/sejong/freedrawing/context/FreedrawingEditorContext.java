package asia.sejong.freedrawing.context;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FontInfo;

public class FreedrawingEditorContext implements Serializable {
	
	private static final long serialVersionUID = 857831127795002358L;
	
	private transient boolean init;
	private transient boolean newCreated;
	
	// 에디터 종속 속성들
	private FDRoot nodeRoot = null;
	private boolean snapToGeometryEnabled;
	private boolean rulerVisibility;
	private boolean gridEnabled;
	private int scaleIndex;
	
	private int defaultWidth;
	private int defaultHeight;
	private int alpha;
	private double degree;
	private RGB backgroundColor;
	private RGB lineColor;
	private int lineStyle;
	private float lineWidth;
	private FontInfo fontInfo;
	private RGB fontColor;
	
	public FreedrawingEditorContext() {
		nodeRoot = new FDRoot();
		newCreated = true;
	}
	
	public void init(FreedrawingEditor editor) {
		// 어플리케이션 컨텍스트의 참조카운트 증가
		ApplicationContext.newInstance(editor.getSite().getShell().getDisplay());
		init = true;
		
		if ( newCreated ) {
			// 역직열화에 의해 생성되지 않고, new 키워드에 의해 생성된 경우,
			// 각 값을 초기화 함
			setSnapToGeometryEnabled(true);
			setRulerVisibility(false);
			setGridEnabled(true);
			setScaleIndex(2);

			setDefaultWidth(100);
			setDefaultHeight(100);
			setAlpha(256);
			setDegree(0);
			setBackgroundColor(new RGB(255,255,255));
			setLineColor(new RGB(0,0,0));
			setLineStyle(SWT.LINE_SOLID);
			setLineWidth(2);
			setFontInfo(new FontInfo("", 12, 0));
			setFontColor(new RGB(0,0,0));
		}
	}
	
	public void dispose() {
		// 어플리케이션 컨텍스트의 참조카운트 감소
		ApplicationContext.getInstance().dispose();
	}

	public FDRoot getNodeRoot() {
		if ( !init ) {
			throw new RuntimeException();
		}
		return nodeRoot;
	}

	public boolean isSnapToGeometryEnabled() {
		return snapToGeometryEnabled;
	}

	public void setSnapToGeometryEnabled(boolean snapToGeometryEnabled) {
		this.snapToGeometryEnabled = snapToGeometryEnabled;
	}

	public boolean isRulerVisibility() {
		return rulerVisibility;
	}

	public void setRulerVisibility(boolean rulerVisibility) {
		this.rulerVisibility = rulerVisibility;
	}

	public boolean isGridEnabled() {
		return gridEnabled;
	}

	public void setGridEnabled(boolean gridEnabled) {
		this.gridEnabled = gridEnabled;
	}

	public int getScaleIndex() {
		return scaleIndex;
	}

	public void setScaleIndex(int scaleIndex) {
		this.scaleIndex = scaleIndex;
	}
	
	public int getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public int getDefaultHeight() {
		return defaultHeight;
	}

	public void setDefaultHeight(int defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public double getDegree() {
		return degree;
	}

	public void setDegree(double degree) {
		this.degree = degree;
	}

	public RGB getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(RGB backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public RGB getLineColor() {
		return lineColor;
	}

	public void setLineColor(RGB lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
	}

	public RGB getFontColor() {
		return fontColor;
	}

	public void setFontColor(RGB fontColor) {
		this.fontColor = fontColor;
	}
}
