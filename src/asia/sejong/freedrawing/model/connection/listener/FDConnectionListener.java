package asia.sejong.freedrawing.model.connection.listener;

import asia.sejong.freedrawing.model.area.AbstractFDElement;

public interface FDConnectionListener {

	void sourceChanged(AbstractFDElement source);
	void targetChanged(AbstractFDElement target);
}
