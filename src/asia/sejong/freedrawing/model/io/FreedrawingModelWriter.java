package asia.sejong.freedrawing.model.io;

import java.io.PrintWriter;

import asia.sejong.freedrawing.model.FDNodeRoot;

public class FreedrawingModelWriter {

	private FDNodeRoot nodeRoot;

	private FreedrawingModelWriter(FDNodeRoot nodeRoot) {
		this.setNodeRoot(nodeRoot);
	}

	public static FreedrawingModelWriter newInstance( FDNodeRoot nodeRoot) {
		return new FreedrawingModelWriter(nodeRoot);
	}

	public FDNodeRoot getNodeRoot() {
		return nodeRoot;
	}

	public void setNodeRoot(FDNodeRoot nodeRoot) {
		this.nodeRoot = nodeRoot;
	}

	public void write(PrintWriter printWriter) {
		
	}
}
