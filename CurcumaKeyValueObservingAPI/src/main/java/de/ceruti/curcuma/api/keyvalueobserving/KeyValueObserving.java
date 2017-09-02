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

package de.ceruti.curcuma.api.keyvalueobserving;

import de.ceruti.curcuma.api.core.IndexSet;


/**
 * public static java.util.Set<String> keyPathsForValuesAffectingValueForKey(java.lang.String key); 
 *
 */

public interface KeyValueObserving {
	

	
	/**
	 * The order in which observers are added must be reflected when notifying them.
	 * @param observer
	 * @param keyPath
	 * @param context
	 * @param options @see {@link KVOOption}
	 * @throws KVOException on faiure
	 */
	void addObserver(KVObserver observer,String keyPath,Object context,int options) throws KVOException;

	/**
	 * 
	 * @param observer
	 * @param keyPath
	 */
	void removeObserver(KVObserver observer,String keyPath);

	/**
	 * @param key
	 */
	void willChangeValueForKey(String key);
	
	/**
	 * 
	 * @param key
	 */
	void didChangeValueForKeyPath(String keyPath);
	
	/**
	 * @param key
	 */
	void willChangeValueForKeyPath(String keyPath);
	
	/**
	 * 
	 * @param key
	 */
	void didChangeValueForKey(String key);
	
	/**
	 * 
	 * @param KVOChangeKind
	 * @param indexes
	 * @param key
	 */
	void willChangeValuesAtIndexesForKey(int KVOChangeKind,IndexSet indexes,String key);
	
	/**
	 * 
	 * @param KVOChangeKind {@link KVOEvent}
	 * @param indexes
	 * @param key
	 */
	void didChangeValuesAtIndexesForKey(int KVOChangeKind,IndexSet indexes,String key);
	
	
//	/**
//	 * 
//	 * @param key
//	 */
//	void pushUpdateForProperty(String key);
	
//	/**
//	 * 
//	 * @param KVOChangeKind
//	 * @param indexes
//	 * @param property
//	 */
//	void pushIndexedUpdateForProperty(int KVOChangeKind, NSIndexSet indexes,String key);
//

//	boolean hasObservers();
//	
//	boolean hasObserversForKey(String key);
	
//	/**
//	 * @deprecated
//	 * @param key
//	 * @param e
//	 */
//	void postEventForKey(String key,KVOEvent e);
//	
//	/**
//	 * @deprecated
//	 * @param keyPath
//	 * @param e
//	 */
//	void postEventForKeyPath(String keyPath,KVOEvent e);
}
