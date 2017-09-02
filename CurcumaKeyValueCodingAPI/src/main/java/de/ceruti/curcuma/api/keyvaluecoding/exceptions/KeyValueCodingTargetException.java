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

import java.lang.reflect.Method;

/**
 * Thrown if an unhandled exception occured during invokation of a KeyValueCoding-compliant accessor-method.
 */
public final class KeyValueCodingTargetException extends KeyValueCodingException {

	/**
	 * Constructor. 
	 * @param cause the cause
	 * @param m the accessor
	 * @param target the target where <code>m</code> has been invoked.
	 * @param key the key
	 */
	public KeyValueCodingTargetException(Throwable cause, Method m, Object target, String key) {
		super("Exception " + cause + " in " + m + " Target=" + target + " key=" + key, cause);
	}


}
