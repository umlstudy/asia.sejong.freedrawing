package asia.sejong.freedrawing.resources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Resource;

public abstract class ResourceManager<KEY, RESOURCE extends Resource> {
	
	private Map<KEY, RESOURCE> resources;
	private Device device;

	public ResourceManager(Device device) {
		this.device = device;
		this.resources = new HashMap<KEY, RESOURCE>();
	}
	
	protected Device getDevice() {
		return device;
	}
	
	protected Map<KEY, RESOURCE> getResources() {
		return resources;
	}
	
	public void add(KEY key) {
		if ( !resources.containsKey(key) ) {
			resources.put(key, createWith(key));
		}
	}

	abstract protected RESOURCE createWith(KEY key);

	public RESOURCE get(KEY key) {
		RESOURCE value = resources.get(key);
		if ( value == null ) {
			add(key);
		}
		return resources.get(key);
	}

	public void dispose() {
		for ( RESOURCE resource : resources.values() ) {
			resource.dispose();
		}
		
		resources = null;
	}
}
