package asia.sejong.freedrawing.model;

public interface FDContainer {

	void addNode(FDNode target);
	void removeNode(FDNode target);
	int changePosition(int position, FDNode target);
}
