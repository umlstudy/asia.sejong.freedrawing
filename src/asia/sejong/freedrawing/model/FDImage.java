package asia.sejong.freedrawing.model;

import java.util.Arrays;


public class FDImage extends FDShape {
	
	private static final long serialVersionUID = -8859384940283994316L;
	
	private byte[] imageBytes;
	
	public byte[] getImageBytes() {
		return imageBytes;
	}


	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
	
	//============================================================
	// Clonable
	@Override
	public FDImage clone() {
		FDImage object = (FDImage)super.clone();
		
		object.imageBytes = Arrays.copyOf(imageBytes, imageBytes.length);
		
		return object;
	}
}
