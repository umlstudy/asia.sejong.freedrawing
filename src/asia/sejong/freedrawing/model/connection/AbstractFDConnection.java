package asia.sejong.freedrawing.model.connection;

import asia.sejong.freedrawing.model.area.AbstractFDElement;

public abstract class AbstractFDConnection {

	private AbstractFDElement source;
	private AbstractFDElement target;

	public AbstractFDElement getSource() {
		return source;
	}

	public void setSource(AbstractFDElement source) {
		this.source = source;
	}

	public AbstractFDElement getTarget() {
		return target;
	}

	public void setTarget(AbstractFDElement target) {
		this.target = target;
	}
}
