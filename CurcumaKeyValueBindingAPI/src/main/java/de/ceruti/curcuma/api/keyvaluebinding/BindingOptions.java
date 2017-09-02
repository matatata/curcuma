package de.ceruti.curcuma.api.keyvaluebinding;


import de.ceruti.curcuma.api.core.ValueTransformer;

public interface BindingOptions {
	
	ValueTransformer getValueTransformer();
	void setValueTransformer(ValueTransformer t);
	boolean isAutomaticValidation();
	void setAutomaticValidation(boolean b);
	
	
	boolean hasCustomBindingOptionForKey(String key);
	Object getCustomBindingOptionForKey(String key);
	void setCustomBindingOptionForKey(Object value,String key);
}
