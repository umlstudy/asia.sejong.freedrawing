package asia.sejong.freedrawing.model;

public interface FDContainer {

	void addShape(FDShape target);
	void removeShape(FDShape target);
	
	int changePosition(int position, FDShape target);
}
