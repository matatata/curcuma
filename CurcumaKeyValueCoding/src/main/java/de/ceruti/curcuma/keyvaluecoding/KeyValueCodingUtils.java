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

package de.ceruti.curcuma.keyvaluecoding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantObjectException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;

public class KeyValueCodingUtils {

	private static Logger logger = LogManager
	.getLogger(KeyValueCodingUtils.class);
	
	public static Object getValueForUndefinedKey(KeyValueCoding owner, String key) {
		throw new NonCompliantAccessorFoundException(owner, key);
	}

	public static void setNullValueForKey(KeyValueCoding owner,String key) {
		throw new IllegalArgumentException();
	}

	public static void setValueForUndefinedKey(KeyValueCoding owner, Object value, Class type, String key) {
		throw new NonCompliantAccessorFoundException(owner, key, type);
	}
	
	
	public static <T> void setValueForUndefinedKey(List<T> list, Object value, Class type, String key)  {
		for(Iterator<T> it = list.iterator();it.hasNext();){
			Object elem = it.next();
			if(elem instanceof KeyValueCoding)
				((KeyValueCoding) elem).setValueForKey(value, type, key);
			else {
				logger.warn(elem + " does not implement KeyValueCoding");
				KeyValueCodingWrapper.wrapIfNecessary(elem).setValueForKey(value, type, key);
			}
		}
	}
	
	
  public static <T> List<Object> validateValueForKey(List<T> list, Object value, String key) throws ValidationException
  {
    ArrayList<Object> ret = new ArrayList<Object>();
    for(Iterator<T> it = list.iterator();it.hasNext();){
      Object elem = it.next();
      if(elem instanceof KeyValueCoding)
        ret.add(((KeyValueCoding) elem).validateValueForKey(value, key));
      else {
//        ret.add(elem);
        throw new NonCompliantObjectException(null,elem,key);
      }
    }
    return ret;
  }
	
	public static <T> Object getValueForUndefinedKey(List<T> list,String key) {
		ArrayList ret = new ArrayList();
		for(Iterator<?> it = list.iterator();it.hasNext();){
			Object elem = it.next();
			if(elem == null)
			{
				ret.add(null);
				continue;
			}
			
			Object val = KeyValueCodingWrapper.wrapIfNecessary(elem).getValueForKey(key);
			ret.add(val);
		}
		return ret;
	}
	
	public static void setValueForUndefinedKey(Map map, Object value, Class type, String key)  {
		map.put(key,value);
	}
	
	
	public static Object getValueForUndefinedKey(Map map,String key) {
		return map.get(key);
	}
	
	public static KeyValueCoding createCompliantObjectForKey(KeyValueCoding parent,
			Object element, String key, boolean isWrite) {
		
		if(element==null)
			return null;

		return KeyValueCodingWrapper.wrapIfNecessary(element);
	}
	
	
	/**
	 * 
	 * @param root
	 * @param keypath
	 * @param lastKey
	 * @param isWrite
	 * @return
	 * @throws NonCompliantObjectException
	 */
	public static KeyValueCoding findTarget(KeyValueCoding root,String keypath,StringBuffer lastKey,boolean isWrite) {
		String[] elems = KeyPathUtilities.split(keypath);
		
		if(elems.length == 0)
			throw new IllegalArgumentException("Illegal keypath '" + keypath + "'");
		
		Object objectAtPathElem = root;
		int i;
		for (i=0 ; i < elems.length - 1; i++) {
			String key = elems[i];
			
			KeyValueCoding parent = (KeyValueCoding) objectAtPathElem;
			
			objectAtPathElem = ((KeyValueCoding)objectAtPathElem).getValueForKey(key);
		
			if(!(objectAtPathElem instanceof KeyValueCoding))
			{
				Object obj = parent.createCompliantObjectForKey(objectAtPathElem,key,isWrite);
				
				if(obj==null && objectAtPathElem!=null) {
					throw new NonCompliantObjectException(parent,objectAtPathElem,KeyPathUtilities.keyPathFromElements(elems, 0, i));
				}
				
				objectAtPathElem = obj;
			}
			
			if(objectAtPathElem == null)
				break;
		}
		
		if(lastKey!=null) {
			lastKey.setLength(0);
			lastKey.append(elems[i]);
		}
		
		return (KeyValueCoding)objectAtPathElem;
	}
}
