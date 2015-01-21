package asia.sejong.freedrawing.code;

public class Item<T> {

	private String name;
	private T value;
	private Object extra;

	public Item(String name, T value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
	
	public String toString() {
		return name;
	}

}
