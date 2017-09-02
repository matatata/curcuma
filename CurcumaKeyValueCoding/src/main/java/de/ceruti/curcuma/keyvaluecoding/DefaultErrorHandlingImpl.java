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

import de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;

public class DefaultErrorHandlingImpl implements ErrorHandling {
	private final KeyValueCoding kvc;
	
	public DefaultErrorHandlingImpl(KeyValueCoding kvc) {
		this.kvc = kvc;
	}
	
	@Override
	public Object getValueForUndefinedKey(String key) {
		throw new NonCompliantAccessorFoundException(kvc, key);
	}

	@Override
	public void setNullValueForKey(String key) {
		KeyValueCodingUtils.setNullValueForKey(kvc,key);
	}

	@Override
	public void setValueForUndefinedKey(Object value, Class<?> type, String key) {
		KeyValueCodingUtils.setValueForUndefinedKey(kvc,value,type,key);
	}
	
	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element,String key, boolean shouldCreateCompliantObjectsOnTraversal) {
		return KeyValueCodingUtils.createCompliantObjectForKey(kvc,element, key, shouldCreateCompliantObjectsOnTraversal);
	}
	
}
