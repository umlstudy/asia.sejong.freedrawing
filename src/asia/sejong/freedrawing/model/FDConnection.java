package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

public class FDConnection {

	private FDNode source;
	private FDNode target;
	private final List<Point> bendpoints = new ArrayList<Point>();
	
	public FDConnection() {
	}
	
//	public FDConnection(FDNode source, FDNode target) {
//		setSource(source);
//		setTarget(target);
//	}
	
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
}
