package asia.sejong.freedrawing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

public class FDWire extends FDElement {

	private FDRect source;
	private FDRect target;
	private final List<Point> bendpoints = new ArrayList<Point>();
	
	private FDWire() {}

	public static FDWire newInstance(FDRect source, FDRect target) {
		return new FDWire(source, target);
	}
	
	private FDWire(FDRect source, FDRect target) {
		setSource(source);
		setTarget(target);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof FDWire))
			return false;
		FDWire wire = (FDWire) obj;
		return wire.source == source && wire.target == target;
	}

	public int hashCode() {
		int hash = 0;
		if (source != null)
			hash += "SRC".hashCode() + source.hashCode();
		if (target != null)
			hash += "TAR".hashCode() + target.hashCode();
		return hash;
	}

	public FDRect getSource() {
		return source;
	}

	public void setSource(FDRect source) {
		this.source = source;
	}

	public FDRect getTarget() {
		return target;
	}

	public void setTarget(FDRect target) {
		this.target = target;
	}
	
	public void add(int index, Point point) {
		bendpoints.add(index, point);
	}

	public Point remove(int locationIndex) {
		return bendpoints.remove(locationIndex);
	}
	
	public Point set(int locationIndex, Point newPoint) {
		return bendpoints.set(locationIndex, newPoint);
	}
	
	public List<Point> getBendpoints() {
		return Collections.unmodifiableList(bendpoints);
	}
	
	@Override
	protected void fireBorderColorChanged(RGB borderColor) {
		
	}

}
