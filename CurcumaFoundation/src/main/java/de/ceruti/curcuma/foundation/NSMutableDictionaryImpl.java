/*
This file is part of Curcuma.

Copyright (c) Matteo Ceruti 2009

Curcuma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Curcuma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Curcuma.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.ceruti.curcuma.foundation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.ceruti.curcuma.api.core.NSMap;
import de.ceruti.curcuma.api.foundation.NSDictionary;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodingUtils;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservable;


@KeyValueCodeable
@KeyValueObservable
class NSMutableDictionaryImpl extends HashMap<String,Object> implements NSDictionary {

	protected NSMutableDictionaryImpl(Map<? extends String,? extends Object> m) {
		super(m);
	}
	
	protected NSMutableDictionaryImpl(){
		super();
	}
	
	
	@Override
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy(this,key);
	}
	
	@Override
	public NSMap<String,Object> immutableCopy() {
		return new NSImmutableDictionaryImpl(this);
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public NSMap<String,Object> mutableCopy() {
		return new NSMutableDictionaryImpl(this);
	}

	@Override
	public void setValueForUndefinedKey(Object value, Class type, String key)  {
		KeyValueCodingUtils.setValueForUndefinedKey((Map)this, value, type, key);
	}
	
	@Override
	public Object put(String key,Object value) {
		willChangeValueForKey(key);
		Object ret = super.put(key, value);
		didChangeValueForKey(key);
		return ret;
	}
	
	
	@Override
	public Object getValueForUndefinedKey(String key) {
		return get(key);
	}
	
	@Override
	public void setNullValueForKey(String key) {
		throw new IllegalArgumentException();
	}
	
	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element,String key, boolean mutateSelf) {
		
		if(element == null && mutateSelf){
			KeyValueCoding ret = new NSMutableDictionaryImpl();
			put(key,ret);
			return ret;
		}
		
		return KeyValueCodingUtils.createCompliantObjectForKey(this, element, key, mutateSelf);
	}
	
	@Override
	public void clear() {
		if(isEmpty()) {
			super.clear();
			return;
		}
		HashSet<String> keys = new HashSet<String>(keySet());
		for (String key : keys) {
			willChangeValueForKey(key);
		}
		super.clear();
		for (String key : keys) {
			didChangeValueForKey(key);
		}
	}
}
