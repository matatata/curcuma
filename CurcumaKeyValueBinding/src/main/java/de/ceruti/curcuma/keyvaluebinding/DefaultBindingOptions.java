package de.ceruti.curcuma.keyvaluebinding;


import java.util.HashMap;
import java.util.Map;

import de.ceruti.curcuma.api.core.ValueTransformer;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;

public class DefaultBindingOptions implements BindingOptions {
	private ValueTransformer valueTransformer;
	
	private boolean automaticValidation = true;
	private Map<String, Object> customOptions;

	public DefaultBindingOptions(ValueTransformer transformer,boolean automaticValidation){
		setValueTransformer(transformer);
		setAutomaticValidation(automaticValidation);
	}

	public DefaultBindingOptions(ValueTransformer transformer) {
		this(transformer, true);
	}
	
	public DefaultBindingOptions() {
		this(null, true);
	}

	@Override
	public ValueTransformer getValueTransformer() {
		return valueTransformer;
	}

	@Override
	public void setValueTransformer(ValueTransformer valueTransformer) {
		this.valueTransformer = valueTransformer;
	}

	@Override
	public boolean isAutomaticValidation() {
		return automaticValidation;
	}


	@Override
	public void setAutomaticValidation(boolean automaticValidation) {
		this.automaticValidation = automaticValidation;
	}

	@Override
	public Object getCustomBindingOptionForKey(String key) {
		if(customOptions==null)
			return null;
		return customOptions.get(key);
	}

	@Override
	public void setCustomBindingOptionForKey(Object value, String key) {
		if(customOptions==null)
			customOptions = new HashMap<String, Object>();
		customOptions.put(key, value);
	}
	
	@Override
	public boolean hasCustomBindingOptionForKey(String key) {
		if(customOptions==null)
			return false;
		return customOptions.containsKey(key);
	}

}
