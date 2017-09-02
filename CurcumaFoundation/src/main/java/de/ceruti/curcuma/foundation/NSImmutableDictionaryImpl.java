package de.ceruti.curcuma.foundation;

import java.util.List;
import java.util.Map;

class NSImmutableDictionaryImpl extends NSMutableDictionaryImpl  {

	protected NSImmutableDictionaryImpl(Map<? extends String,? extends Object> m) {
		super(m);
	}

	protected NSImmutableDictionaryImpl() {
		super();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public List mutableArrayValueForKey(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List mutableArrayValueForKeyPath(String keypath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object put(String arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValueForKey(Object value, Class type, String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValueForKey(Object value, String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValueForKeyPath(Object value, Class type, String keypath) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValueForKeyPath(Object value, String keypath) {
		throw new UnsupportedOperationException();
	}
}
