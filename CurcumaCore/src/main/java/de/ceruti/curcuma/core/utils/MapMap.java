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

package de.ceruti.curcuma.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * a map of collections
 * @author matteo
 *
 */
public class MapMap<K,K2,T> {
	private Map<K,Map<K2,T>> map = new HashMap<K,Map<K2,T>>();
	
	
	public Map<K2,T> get(K key){
		return get(key,false);
	}
	
	protected Map<K2,T> createCollection(){
		return new HashMap<K2,T>();
	}
	
	protected Map<K2,T> get(K key,boolean autocreate){
		if(autocreate && !map.containsKey(key))
			put(key,createCollection());
		
		return map.get(key);
	}
	
	public void put(K key,Map<K2,T> l)
	{
		map.put(key,l);
	}
	
	public Set<K> keySet(){
		return map.keySet();
	}
	
	public void put(K key,K2 key2,T value){
		get(key,true).put(key2,value);
	}
	

	public void remove(K key,K2 key2){
		Map<K2,T> c = get(key,false);
		if(c!=null)
			c.remove(key2);
		if(c.isEmpty())
			map.remove(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	
}
