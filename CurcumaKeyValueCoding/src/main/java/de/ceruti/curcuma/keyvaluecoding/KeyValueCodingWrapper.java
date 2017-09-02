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

/**
 * 
 */
package de.ceruti.curcuma.keyvaluecoding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;

@KeyValueCodeable
public class KeyValueCodingWrapper implements KeyValueCoding{
	private Object pojo;
	
	public static KeyValueCoding wrapIfNecessary(Object obj){
		if(obj instanceof KeyValueCoding)
			return (KeyValueCoding) obj;
		return new KeyValueCodingWrapper(obj);
	}
	
	@Override
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy(this,key);
	}
	
	/**
	 * Constructor.
	 * @param pojo
	 * @throws IllegalArgumentException if pojo==null
	 */
	private KeyValueCodingWrapper(Object pojo) throws IllegalArgumentException{
		if(pojo==null)
			throw new IllegalArgumentException("pojo may not be null");
		if(pojo instanceof KeyValueCoding)
			throw new IllegalArgumentException("pojo already implements KeyValueCoding... what do you want?");
		this.pojo = pojo;
	}
	
	/**
	 * should be protected, but aspectj does not allow to override introduced methods.
	 * @see KeyValueCodingAspect
	 */
	public Object TargetPoJo() {
		return this.pojo;
	}
	
	@Override
	public void setValueForUndefinedKey(Object value, Class<?> type, String key)  {
		if(pojo instanceof List)
			KeyValueCodingUtils.setValueForUndefinedKey((List) pojo, value, type, key);
		else if(pojo instanceof Map)
			KeyValueCodingUtils.setValueForUndefinedKey((Map) pojo, value, type, key);
		else
			throw new NonCompliantAccessorFoundException(pojo, key, type);
	}
	
	
	@Override
	public Object getValueForUndefinedKey(String key) {
		if(pojo instanceof List)
			return KeyValueCodingUtils.getValueForUndefinedKey((List) pojo, key);
		else if(pojo instanceof Map)
			return KeyValueCodingUtils.getValueForUndefinedKey((Map) pojo, key);
		else
			throw new NonCompliantAccessorFoundException(pojo,key);
	}
	
	@Override
	public void setNullValueForKey(String key) {
		throw new IllegalArgumentException();
	}
	
	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element,String key, boolean mutateSelf) {
		if(pojo instanceof Map) {
			if(element==null && mutateSelf) {
				KeyValueCoding ret = new KeyValueCodingWrapper(new HashMap());
				((Map)pojo).put(key,ret);
				return ret;
			}
		}
		
		return KeyValueCodingUtils.createCompliantObjectForKey(this,element,key,mutateSelf);
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("KVCWrapper(").append(pojo).append(")");
		return ret.toString();
	}
}