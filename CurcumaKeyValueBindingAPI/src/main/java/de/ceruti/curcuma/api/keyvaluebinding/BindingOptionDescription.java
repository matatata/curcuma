package de.ceruti.curcuma.api.keyvaluebinding;

public class BindingOptionDescription {

	private Object defValue;
	private String key;
	private String desc;
	private Class<?> clazz;
	
	public BindingOptionDescription(String key,Class<?> clazz,String description, Object defaultValue) {
		this.key = key;
		this.defValue = defaultValue;
		this.clazz = clazz;
		this.desc = description;
	}
	
	public Object getDefaultValue() {
		return defValue;
	}

	public String getDescription() {
		return desc;
	}

	public String getKey() {
		return key;
	}

	public Class<?> getValueClass() {
		return clazz;
	}
}
