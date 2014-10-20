package asia.sejong.freedrawing.model.io;

import java.io.PrintWriter;

import asia.sejong.freedrawing.model.area.FreedrawingData;

public class FreedrawingDataWriter {

	private FreedrawingData freedrawingData;

	private FreedrawingDataWriter(FreedrawingData freedrawingData) {
		this.setFreedrawingData(freedrawingData);
	}

	public static FreedrawingDataWriter newInstance(
			FreedrawingData freedrawingData) {
		return new FreedrawingDataWriter(freedrawingData);
	}

	public FreedrawingData getFreedrawingData() {
		return freedrawingData;
	}

	public void setFreedrawingData(FreedrawingData freedrawingData) {
		this.freedrawingData = freedrawingData;
	}

	public void write(PrintWriter printWriter) {
		
	}
}
