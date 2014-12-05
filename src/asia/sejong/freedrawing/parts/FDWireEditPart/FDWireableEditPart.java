package asia.sejong.freedrawing.parts.FDWireEditPart;

public interface FDWireableEditPart {
	
	void addSourceConnection(FDWireEditPart wireEditPart);

	void addTargetConnection(FDWireEditPart wireEditPart);
	
	void removeSourceConnection_(FDWireEditPart wireEditPart);

	void removeTargetConnection_(FDWireEditPart wireEditPart);
}
