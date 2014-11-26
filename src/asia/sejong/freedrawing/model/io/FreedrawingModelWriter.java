package asia.sejong.freedrawing.model.io;

import java.io.PrintWriter;

import asia.sejong.freedrawing.model.FDRoot;

public class FreedrawingModelWriter {

	private FDRoot nodeRoot;

	private FreedrawingModelWriter(FDRoot nodeRoot) {
		this.setNodeRoot(nodeRoot);
	}

	public static FreedrawingModelWriter newInstance( FDRoot nodeRoot) {
		return new FreedrawingModelWriter(nodeRoot);
	}

	public FDRoot getNodeRoot() {
		return nodeRoot;
	}

	public void setNodeRoot(FDRoot nodeRoot) {
		this.nodeRoot = nodeRoot;
	}

	public void write(PrintWriter printWriter) {
		
	}
}
