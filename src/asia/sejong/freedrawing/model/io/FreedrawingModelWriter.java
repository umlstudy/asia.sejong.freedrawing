package asia.sejong.freedrawing.model.io;

import java.io.PrintWriter;

import asia.sejong.freedrawing.model.FDNodeRoot;

public class FreedrawingModelWriter {

	private FDNodeRoot freedrawingData;

	private FreedrawingModelWriter(FDNodeRoot freedrawingData) {
		this.setFreedrawingData(freedrawingData);
	}

	public static FreedrawingModelWriter newInstance(
			FDNodeRoot freedrawingData) {
		return new FreedrawingModelWriter(freedrawingData);
	}

	public FDNodeRoot getFreedrawingData() {
		return freedrawingData;
	}

	public void setFreedrawingData(FDNodeRoot freedrawingData) {
		this.freedrawingData = freedrawingData;
	}

	public void write(PrintWriter printWriter) {
		
	}
}
