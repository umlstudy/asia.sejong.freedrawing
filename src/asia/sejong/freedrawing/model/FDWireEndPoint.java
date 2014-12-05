package asia.sejong.freedrawing.model;

import java.util.ArrayList;

public interface FDWireEndPoint {

	ArrayList<FDWire> getIncommingWires();

	boolean containsTarget(FDWireEndPoint target);

	void addWire(FDWire wire);

	void removeWire(FDWire wire);
}
