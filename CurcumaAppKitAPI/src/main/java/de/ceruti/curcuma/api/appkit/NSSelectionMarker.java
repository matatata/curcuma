package de.ceruti.curcuma.api.appkit;
import java.util.List;
import java.util.Set;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;

public abstract class NSSelectionMarker implements KeyValueCoding {
	public static final NSSelectionMarker NSMultipleValuesMarker = new NSSelectionMarker() { @Override
	public String getBindingOptionKey() {return "NSMultipleValuesMarker";}};
	public static final NSSelectionMarker NSNoSelectionMarker = new NSSelectionMarker() { @Override
	public String getBindingOptionKey() {return "NSNoSelectionMarker";}};
	public static final NSSelectionMarker NSNotApplicableMarker = new NSSelectionMarker() { @Override
	public String getBindingOptionKey() {return "NSNotApplicableMarker";}};
	public static final NSSelectionMarker NSNullValueMarker = new NSSelectionMarker() { @Override
	public String getBindingOptionKey() {return "NSNullValueMarker";}};

	public abstract String getBindingOptionKey();
	
	@Override
	public String toString() {
		return getBindingOptionKey();
	}

	
	@Override
	public Object getValueForKey(String key) {
		return this;
	}
	@Override
	public Object getValueForKeyPath(String keypath) {
		return this;
	}
	@Override
	public List mutableArrayValueForKey(String key) {
		return null;
	}
	@Override
	public List mutableArrayValueForKeyPath(String keypath) {
		return null;
	}
	
	@Override
	public Set mutableSetValueForKey(String key) {
		return null;
	}
	
	@Override
	public Set mutableSetValueForKeyPath(String keypath) {
		return null;
	}
	@Override
	public void setValueForKey(Object value, Class<?> type, String key) {
	}
	@Override
	public void setValueForKey(Object value, String key) {
	}
	@Override
	public void setValueForKeyPath(Object value, Class<?> type, String keypath) {
	}
	@Override
	public void setValueForKeyPath(Object value, String keypath) {
	}
	@Override
	public Object validateValueForKey(Object value, String key)
			throws ValidationException {
		throw new ValidationException();
	}
	@Override
	public Object validateValueForKeyPath(Object value, String keyPath)
			throws ValidationException {
		throw new ValidationException("Cannot validate agains " + toString());
	}
	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element,
			String key, boolean isWrite) {
		return null;
	}
	@Override
	public Object getValueForUndefinedKey(String key) {
		return null;
	}
	@Override
	public void setNullValueForKey(String key) {
	}
	@Override
	public void setValueForUndefinedKey(Object value, Class<?> type, String key) {
	}

}
