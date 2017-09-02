package de.ceruti.curcuma.api.keyvaluebinding;

import java.util.Set;

/**
 * 
 * @author matteo
 *
 */
public interface KeyValueBindingCreation {

	Set<String> exposedBindings();

	/**
	 * 
	 * @param binding
	 * @return immutable Map
	 */
	Set<String> getBindingOptionKeysForBinding(String binding);
	BindingOptionDescription getBindingOptionDescription(String binding,String optionKey);
	void addBindingOptionDescriptionForBinding(String binding, BindingOptionDescription desc);
	void removeBindingOptionDescriptionForBinding(String binding,String key);
	
	/**
	 * Binds the attribute specified by binding to the observable's
	 * object specified by withKeyPath. Whenever the latter
	 * changes, this instance will be updated too.
	 * 
	 * @param binding
	 * @param observable
	 * @param withKeyPath
	 * @param options
	 */
	void bind(String binding,Object observable,String withKeyPath, BindingOptions options);  
	
	BindingInfo getInfoForBinding(String binding); 
	
	
	void unbind(String binding);
	
	boolean isBound(String binding);
}
