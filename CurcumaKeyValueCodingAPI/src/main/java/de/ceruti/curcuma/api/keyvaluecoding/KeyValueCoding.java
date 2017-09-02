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

import java.util.List;
import java.util.Set;

import de.ceruti.curcuma.api.keyvaluecoding.exceptions.KeyValueCodingTargetException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantObjectException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;

public interface KeyValueCoding extends ErrorHandling {

	/**
	 * 
	 * @param key
	 * @throws NonCompliantAccessorFoundException
	 *             if a KVC compliant Accessor could not be found
	 * @throws KeyValueCodingTargetException
	 *             if an exception occured during the invocation of the
	 *             accessor-method
	 * @return
	 */
	Object getValueForKey(String key);

	/**
	 * 
	 * @param keypath
	 * @throws NonCompliantObjectException
	 *             if the model-graph is not KVC compliant for
	 *             <code>keypath</code>
	 * @throws KeyValueCodingTargetException
	 *             if an exception occured during the invocation of the
	 *             accessor-method
	 * @throws NonCompliantAccessorFoundException
	 *             if a KVC compliant Accessor could not be found
	 * @return
	 */
	Object getValueForKeyPath(String keypath);

	/**
	 * 
	 * @param value
	 * @param type
	 *            If you don not know the concrete type just set it to null
	 * @param keypath
	 * @throws NonCompliantObjectException
	 *             if the model-graph is not KVC compliant for
	 *             <code>keypath</code>
	 * @throws KeyValueCodingTargetException
	 *             if an exception occured during the invocation of the
	 *             accessor-method
	 * @throws NonCompliantAccessorFoundException
	 *             if a KVC compliant Accessor could not be found
	 */
	void setValueForKeyPath(Object value, Class<?> type, String keypath);

	/**
	 * 
	 * @param value
	 * @param type
	 * @param key
	 * @throws KeyValueCodingTargetException
	 *             if an exception occured during the invocation of the
	 *             accessor-method
	 * @throws NonCompliantAccessorFoundException
	 *             if a KVC compliant Accessor could not be found
	 */
	void setValueForKey(Object value, Class<?> type, String key);

	/**
	 * Identical to <code>setValueForKeyPath(value,null,keypath)</code>
	 * 
	 * @param value
	 * @param keypath
	 * @throws NonCompliantObjectException
	 *             if the model-graph is not KVC compliant for
	 *             <code>keypath</code>
	 * @throws KeyValueCodingTargetException
	 *             if an exception occured during the invocation of the
	 *             accessor-method
	 * @throws NonCompliantAccessorFoundException
	 *             if a KVC compliant Accessor could not be found
	 */
	void setValueForKeyPath(Object value, String keypath);

	/**
	 * Identical to <code>setValueForKey(value,null,key)</code>
	 * 
	 * @param value
	 * @param key
	 * @throws NonCompliantAccessorFoundException
	 *             if a KVC compliant Accessor could not be found
	 * @throws KeyValueCodingTargetException
	 *             if an exception occured during the invocation of the
	 *             accessor-method
	 */
	void setValueForKey(Object value, String key);

	/**
	 * 
	 * @param value
	 * @param error
	 * @throws KeyValueCodingTargetException
	 *             if a checked exception occurred during the invocation of the
	 *             accessor-method
	 * @throws ValidationException
	 *             if <code>value</code> is invalid and could not be converted
	 *             into a valid one
	 * @return possibly modified Value
	 */
	Object validateValueForKey(Object value, String key)
			throws ValidationException;

	/**
	 * 
	 * @param value
	 * @param keyPath
	 * @throws ValidationException
	 *             if <code>value</code> is invalid and could not be converted
	 *             into a valid one
	 * @throws NonCompliantObjectException
	 *             if the model-graph is not KVC compliant for
	 *             <code>keypath</code>
	 * @throws KeyValueCodingTargetException
	 *             if a checked exception occurred during the invocation of the
	 *             accessor-method
	 * @return possibly modified Value
	 */
	Object validateValueForKeyPath(Object value, String keyPath)
			throws ValidationException;

	/**
	 * @param key
	 * @return mutable array for the collection with the given key
	 */
	List mutableArrayValueForKey(String key);

	/**
	 * @param keypath
	 * @return mutable array for the collection at the given keypath
	 */
	List mutableArrayValueForKeyPath(String keypath);
	

	/**
	 * NOT IMPLEMENTED YET
	 * 
	 * @param key
	 * @return
	 */
	Set mutableSetValueForKey(String key);

	/**
	 * NOT IMPLEMENTED YET
	 * 
	 * @param keypath
	 * @return
	 */
	Set mutableSetValueForKeyPath(String keypath);

}
