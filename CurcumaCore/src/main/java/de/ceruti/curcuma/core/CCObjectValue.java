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

package de.ceruti.curcuma.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.api.core.Value;

final class CCObjectValue extends AbstractValue {
	private static final Logger logger = LogManager.getLogger(CCObjectValue.class);
	
	private Object obj;
	
	public CCObjectValue(Object obj) {
		if(obj == null)
			throw new IllegalArgumentException();
		this.obj = obj;
	}
	
	@Override
	public String stringValue() {
		return obj.toString();
	}
	

	@Override
	public Object objectValue() {
		return obj;
	}
	
	@Override
	public String toString(){
		return "CCObjectValue[" + stringValue() + "]";
	}


	@Override
	public double doubleValue() {
		logger.warn("converting " + obj.getClass() + " to double: 0.0");
		return 0.0;
	}


	@Override
	public long longValue() {
		logger.warn("converting " + obj.getClass() + " to long: 0L");
		return 0L;
	}

	@Override
	protected boolean equals(Value o) {
		return obj.equals(o.objectValue());
	}

	@Override
	public int hashCode() {
		return obj.hashCode();
	}

}
