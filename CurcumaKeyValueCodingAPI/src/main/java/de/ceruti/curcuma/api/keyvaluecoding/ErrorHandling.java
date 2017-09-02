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

package de.ceruti.curcuma.api.keyvaluecoding;

public interface ErrorHandling {
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	Object getValueForUndefinedKey(String key);
	
	/**
	 * 
	 * @param value
	 * @param type
	 * @param key
	 */
	void setValueForUndefinedKey(Object value , Class<?> type,  String key);
	
	/**
	 * 
	 * @param key
	 */
	void setNullValueForKey(String key);
	
	/**
	 * When traversing the object-graph along a given keypath, we might find an
	 * object which is null or not an instance of KeyValueCoding. That means the
	 * target object cannot be reached. If you want to give the receiver a chance
	 * to recover from this error you should invoke this method.
	 * 
	 * The default-implementation will return null
	 * 
	 * @param element
	 *            the illegal Object
	 * @param key
	 * @param isWrite
	 *            if true, you are allowed to mutate this object. This is true
	 *            if this method has been called during a
	 *            setValueForKeyPath-Call
	 * @return the object to continue instead of element or null if no compliant
	 *         object can be provided
	 */
	KeyValueCoding createCompliantObjectForKey(Object element, String key,
			boolean isWrite);
}