package asia.sejong.freedrawing.model;

import java.util.HashSet;
import java.util.Set;

import asia.sejong.freedrawing.model.listener.FDBaseListener;

public class FDBase {
	
	transient final protected Set<FDBaseListener> listeners = new HashSet<FDBaseListener>();

}
