package de.ceruti.curcuma.api.appkit;
import java.util.List;
import java.util.Set;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;

public abstract class NSSelectionMarker implements KeyValueCoding {
	public static final NSSelectionMarker NSMultipleValuesMarker = new NSSelectionMarker() { public String getBindingOptionKey() {return "NSMultipleValuesMarker";}};
	public static final NSSelectionMarker NSNoSelectionMarker = new NSSelectionMarker() { public String getBindingOptionKey() {return "NSNoSelectionMarker";}};
	public static final NSSelectionMarker NSNotApplicableMarker = new NSSelectionMarker() { public String getBindingOptionKey() {return "NSNotApplicableMarker";}};
	public static final NSSelectionMarker NSNullValueMarker = new NSSelectionMarker() { public String getBindingOptionKey() {return "NSNullValueMarker";}};

	public abstract String getBindingOptionKey();
	
	public String toString() {
		return getBindingOptionKey();
	}

	
	public Object getValueForKey(String key) {
		return this;
	}
	public Object getValueForKeyPath(String keypath) {
		return this;
	}
	public List mutableArrayValueForKey(String key) {
		return null;
	}
	public List mutableArrayValueForKeyPath(String keypath) {
		return null;
	}
	
	public Set mutableSetValueForKey(String key) {
		return null;
	}
	
	public Set mutableSetValueForKeyPath(String keypath) {
		return null;
	}
	public void setValueForKey(Object value, Class<?> type, String key) {
	}
	public void setValueForKey(Object value, String key) {
	}
	public void setValueForKeyPath(Object value, Class<?> type, String keypath) {
	}
	public void setValueForKeyPath(Object value, String keypath) {
	}
	public Object validateValueForKey(Object value, String key)
			throws ValidationException {
		throw new ValidationException();
	}
	public Object validateValueForKeyPath(Object value, String keyPath)
			throws ValidationException {
		throw new ValidationException("Cannot validate agains " + toString());
	}
	public KeyValueCoding createCompliantObjectForKey(Object element,
			String key, boolean isWrite) {
		return null;
	}
	public Object getValueForUndefinedKey(String key) {
		return null;
	}
	public void setNullValueForKey(String key) {
	}
	public void setValueForUndefinedKey(Object value, Class<?> type, String key) {
	}

}
