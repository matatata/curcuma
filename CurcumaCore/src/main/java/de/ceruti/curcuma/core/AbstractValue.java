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

import de.ceruti.curcuma.api.core.Value;

/**
 * You need to implement doubleValue, longValue, stringValue, objectValue
 * @author matteo
 *
 */

abstract class AbstractValue implements Value {

	@Override
	public boolean boolValue() {
		return longValue() == 0L ? false : true;
	}
	
	@Override
	public short shortValue() {
		return (short)longValue();
	}

	@Override
	public byte byteValue() {
		return (byte)longValue();
	}


	@Override
	public float floatValue() {
		return (float)doubleValue();
	}

	@Override
	public int intValue(){
		return (int)longValue();
	}


	@Override
	public char charValue() {
		return (char)longValue();
	}
	
	@Override
	public abstract double doubleValue();
	
	@Override
	public abstract long longValue();
	
	@Override
	public abstract String stringValue();
	
	@Override
	public abstract Object objectValue();
	
	/**
	 * @return stringValue()
	 */
	@Override
	public String toString() {
		return stringValue();
	}
	
	/**
	 * 
	 * @param o you may assume this is never null
	 * @return
	 */
	protected abstract boolean equals(Value o);
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(this==obj)
			return true;
		
		if(getClass().equals(obj.getClass()))
			return equals((Value)obj);
		return false;
	}
	
	@Override
	public abstract int hashCode();
	

}
