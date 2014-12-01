package asia.sejong.freedrawing.model;

public interface FDContainer {

	void addNode(FDRect target);
	void removeNode(FDRect target);
	
	int changePosition(int position, FDRect target);
}
