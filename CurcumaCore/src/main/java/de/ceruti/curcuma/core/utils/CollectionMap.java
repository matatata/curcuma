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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * a map of collections
 * @author matteo
 *
 */
public class CollectionMap<K,T> {
	private Map<K,Collection<T>> map = new HashMap<K,Collection<T>>();
	
	
	public Collection<T> get(K key){
		return get(key,false);
	}
	
	protected Collection<T> createCollection(){
		return new ArrayList<T>();
	}
	
	public Collection<T> get(K key,boolean autocreate){
		if(autocreate && !map.containsKey(key))
			put(key,createCollection());
		
		return map.get(key);
	}
	
	public void put(K key,Collection<T> l)
	{
		map.put(key,l);
	}
	
	public Set<K> keySet(){
		return map.keySet();
	}
	
	public void add(K key,T value){
		get(key,true).add(value);
	}
	

	public void remove(K key,T value){
		Collection<T> c = get(key,false);
		if(c!=null)
			c.remove(value);
		if(c==null || c.isEmpty())
			map.remove(key);
	}
	
	public void remove(K key) {
		Collection<T> c = get(key,false);
		if(c!=null)
			c.clear();
		map.remove(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	
	@Override
	public String toString() {
		return map.toString();
	}

	public void clear() {
		map.clear();
	}
	
}
