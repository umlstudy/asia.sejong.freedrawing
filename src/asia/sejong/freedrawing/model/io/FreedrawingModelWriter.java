package asia.sejong.freedrawing.model.io;

import java.io.PrintWriter;

import asia.sejong.freedrawing.model.FDRoot;

public class FreedrawingModelWriter {

	private FDRoot root;

	private FreedrawingModelWriter(FDRoot root) {
		this.setNodeRoot(root);
	}

	public static FreedrawingModelWriter newInstance( FDRoot root) {
		return new FreedrawingModelWriter(root);
	}

	public FDRoot getNodeRoot() {
		return root;
	}

	private void setNodeRoot(FDRoot root) {
		this.root = root;
	}

	public void write(PrintWriter printWriter) {
		
	}
}
