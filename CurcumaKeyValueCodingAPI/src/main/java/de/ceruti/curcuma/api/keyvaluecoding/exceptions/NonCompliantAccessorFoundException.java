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

package de.ceruti.curcuma.api.keyvaluecoding.exceptions;

/**
 * Thrown when no KeyValueCoding-compliant accessor was found for a given key.
 * 
 */
public class NonCompliantAccessorFoundException extends KeyValueCodingException {

	private Object target;
	private String key;
	private Class type;

	/**
	 * Constructor. Simply calls <code>this(target,key,null)</code> to indicate
	 * that an accessor of non-specific type was requested.
	 * 
	 * @param target
	 *            the object supposed to have an accessor for <code>key</code>
	 * @param key
	 *            the key for which no accessor was found
	 */
	public NonCompliantAccessorFoundException(Object target, String key) {
		this(target, key, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param target
	 *            the object supposed to have an accessor for <code>key</code>
	 * @param key
	 *            the key for which no accessor was found
	 * @param type
	 *            the expected type of the property. <code>null</code> indicates
	 *            that an accessor of non-specific type was requested.
	 */
	public NonCompliantAccessorFoundException(Object target, String key,
			Class type) {
		super("No KeyValueCoding-compliant accessor found for key '" + key
				+ "'" + (type != null ? " " + type.getName() : "") + " in "
				+ target.getClass().getName());
		this.target = target;
		this.key = key;
		this.type = type;
	}

	public Object getTarget() {
		return target;
	}

	public String getKey() {
		return key;
	}

	public Class getType() {
		return type;
	}

}
