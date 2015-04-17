package asia.sejong.freedrawing.context;

import java.io.Serializable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.RGB;

import asia.sejong.freedrawing.Activator;
import asia.sejong.freedrawing.code.LineStyle;
import asia.sejong.freedrawing.editor.FreedrawingEditor;
import asia.sejong.freedrawing.model.FDRoot;
import asia.sejong.freedrawing.model.FontInfo;

public class FreedrawingEditorContext implements Serializable {
	
	private static final long serialVersionUID = 857831127795002358L;
	
	private transient boolean initFinished;
	
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
		
		// 역직열화에 의해 생성되지 않고, new 키워드에 의해 생성된 경우,
		// 각 값을 초기화 함
		nodeRoot = new FDRoot();
		
		setSnapToGeometryEnabled(true);
		setRulerVisibility(false);
		setGridEnabled(true);
		setScaleIndex(2);

		setDefaultWidth(150);
		setDefaultHeight(150);
		setAlpha(0xff);
		setDegree(0f);
		setBackgroundColor(new RGB(0xff,0xff,0xff));
		setLineColor(new RGB(0,0,0));
		
		// DEBUG TODO
//		IStatus status = new org.eclipse.core.runtime.Status(IStatus.ERROR, Activator.PLUGIN_ID, "start");
//		Activator.getDefault().getLog().log(status);
//		status = new org.eclipse.core.runtime.Status(IStatus.ERROR, Activator.PLUGIN_ID, ">> " + LineStyle.SOLID);
//		Activator.getDefault().getLog().log(status);
		
		setLineStyle(LineStyle.SOLID.getStyle());
//		status = new org.eclipse.core.runtime.Status(IStatus.ERROR, Activator.PLUGIN_ID, ">> END");
//		Activator.getDefault().getLog().log(status);
		
		setLineWidth(2);
		FontInfo fontInfo = FontInfo.create(JFaceResources.getDefaultFont().getFontData()[0]);
		fontInfo.setHeight(12);
		setFontInfo(fontInfo);
		setFontColor(new RGB(0,0,0));
	}
	
	public void init(FreedrawingEditor editor) {
		// 어플리케이션 컨텍스트의 참조카운트 증가
		ApplicationContext.newInstance(editor.getSite().getShell().getDisplay());
		initFinished = true;
	}
	
	public void dispose() {
		// 어플리케이션 컨텍스트의 참조카운트 감소
		ApplicationContext.getInstance().dispose();
	}

	public FDRoot getNodeRoot() {
		if ( !initFinished ) {
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
