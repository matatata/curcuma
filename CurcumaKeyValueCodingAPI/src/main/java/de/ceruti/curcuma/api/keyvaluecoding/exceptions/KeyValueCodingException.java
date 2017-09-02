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

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;

/**
 * Base class for all unchecked Exceptions that may occured using the {@link KeyValueCoding} API.
 */
public abstract class KeyValueCodingException extends RuntimeException {

	protected KeyValueCodingException(String string) {
		super(string);
	}
	
	protected KeyValueCodingException(String string, Throwable cause) {
		super(string,cause);
	}
}
