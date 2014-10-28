package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

public class FDConnection extends BaseObject {

	private FDNode source;
	private FDNode target;
	private final List<Point> bendpoints = new ArrayList<Point>();
	
	private FDConnection() {}

	public static FDConnection newInstance(FDNode source, FDNode target) {
		return new FDConnection(source, target);
	}
	
	private FDConnection(FDNode source, FDNode target) {
		setSource(source);
		setTarget(target);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof FDConnection))
			return false;
		FDConnection conn = (FDConnection) obj;
		return conn.source == source && conn.target == target;
	}

	public int hashCode() {
		int hash = 0;
		if (source != null)
			hash += "SRC".hashCode() + source.hashCode();
		if (target != null)
			hash += "TAR".hashCode() + target.hashCode();
		return hash;
	}

	public FDNode getSource() {
		return source;
	}

	public void setSource(FDNode source) {
		this.source = source;
	}

	public FDNode getTarget() {
		return target;
	}

	public void setTarget(FDNode target) {
		this.target = target;
	}
	
	public void add(int index, Point point) {
		bendpoints.add(index, point);
	}

	@Override
	protected void fireBorderColorChanged(RGB borderColor) {
		
	}
}
