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

aspect DefaultErrorHandlingAspect {

	public static interface ErrorHandlingSupport extends ErrorHandling {
	}
	
	declare parents: @DefaultErrorHandling * implements ErrorHandlingSupport;
	
	
	public Object ErrorHandlingSupport.getValueForUndefinedKey(String key) {
		throw new NonCompliantAccessorFoundException(this, key);
	}

	public void ErrorHandlingSupport.setNullValueForKey(String key) {
		KeyValueCodingUtils.setNullValueForKey((KeyValueCoding)this,key);
	}

	public void ErrorHandlingSupport.setValueForUndefinedKey(Object value, Class<?> type, String key) {
		KeyValueCodingUtils.setValueForUndefinedKey((KeyValueCoding)this,value,type,key);
	}
	
	public KeyValueCoding ErrorHandlingSupport.createCompliantObjectForKey(Object element,String key, boolean shouldCreateCompliantObjectsOnTraversal) {
		return KeyValueCodingUtils.createCompliantObjectForKey((KeyValueCoding)this,element, key, shouldCreateCompliantObjectsOnTraversal);
	}
	
}
