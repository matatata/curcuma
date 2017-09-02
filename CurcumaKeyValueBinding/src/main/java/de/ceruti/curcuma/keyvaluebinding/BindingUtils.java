package de.ceruti.curcuma.keyvaluebinding;

import de.ceruti.curcuma.api.keyvaluebinding.BindingOptionDescription;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluebinding.NoSuchBindingOption;

public class BindingUtils {
	

	
	/**
	 * 
	 * @param object
	 * @param binding
	 * @param optionKey
	 * @param opts
	 * @return
	 * @throws NoSuchBindingOption 
	 */
	public static Object getCustomBindingOptionValue(KeyValueBindingCreation object,
			String binding, String optionKey, BindingOptions opts) throws NoSuchBindingOption
	{
		if (opts == null || !opts.hasCustomBindingOptionForKey(optionKey)) {
			if(object==null)
				throw new NoSuchBindingOption(binding,optionKey);
			BindingOptionDescription desc = object.getBindingOptionDescription(binding, optionKey);
			if(desc==null)
				throw new NoSuchBindingOption(binding,optionKey);
			else
				return desc.getDefaultValue();
		}
	
		return opts.getCustomBindingOptionForKey(optionKey);
	}
	
}
