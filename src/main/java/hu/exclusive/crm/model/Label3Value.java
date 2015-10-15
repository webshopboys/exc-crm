package hu.exclusive.crm.model;

public class Label3Value<T> {

	public Label3Value(String l, T v) {
		this.label = l;
		this.value = v;
	}

	public Label3Value(String l1, String l2, String l3, T commonValue) {
		this(l1, l2, l3, commonValue, commonValue, commonValue);
	}

	public Label3Value(String l1, String l2, String l3, T v1, T v2, T v3) {
		this.label = l1;
		this.value = v1;
		this.label2 = l2;
		this.value2 = v2;
		this.label3 = l3;
		this.value3 = v3;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel2() {
		return label2;
	}

	public void setLabel2(String label2) {
		this.label2 = label2;
	}

	public String getLabel3() {
		return label3;
	}

	public void setLabel3(String label3) {
		this.label3 = label3;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue2() {
		return value2;
	}

	public void setValue2(T value2) {
		this.value2 = value2;
	}

	public T getValue3() {
		return value3;
	}

	public void setValue3(T value3) {
		this.value3 = value3;
	}

	private String label;
	private String label2;
	private String label3;
	private T value;
	private T value2;
	private T value3;
}
