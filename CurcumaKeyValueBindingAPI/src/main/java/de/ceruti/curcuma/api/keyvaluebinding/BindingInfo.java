package de.ceruti.curcuma.api.keyvaluebinding;


public interface BindingInfo {
	/*
	 * A dictionary with information about binding, or nil if the binding is not
	 * bound. The dictionary contains three key/value pairs:
	 * NSObservedObjectKey: object bound, NSObservedKeyPathKey: key path bound,
	 * NSOptionsKey: dictionary with the options and their values for the
	 * bindings.
	 */
	
	Object getObservedObject();
	String getObservedKeyPath();
	BindingOptions getOptions();
	
	String getName();
}
