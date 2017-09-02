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

import de.ceruti.curcuma.api.core.MutableValue;


aspect MutableValueAspect {

	public static interface MutableValueSupport extends MutableValue {
	}
	
	declare parents: @MutableVal * implements MutableValueSupport;
	
	
	public void MutableValueSupport.setIntValue(int v) {
		setLongValue(v);
	}

	public void MutableValueSupport.setBoolValue(boolean b) {
		setLongValue(b ? 1L : 0L);
	}

	public void MutableValueSupport.setByteValue(byte b) {
		setLongValue(b);
	}

	public void MutableValueSupport.setCharValue(char c) {
		setLongValue(c);
	}

	public void MutableValueSupport.setFloatValue(float f) {
		setDoubleValue((double)f);
	}

	public void MutableValueSupport.setShortValue(short s) {
		setLongValue(s);
	}
	
	public boolean MutableValueSupport.boolValue() {
		return longValue() == 0 ? false : true;
	}
	
	public short MutableValueSupport.shortValue() {
		return (short)longValue();
	}

	public byte MutableValueSupport.byteValue() {
		return (byte)longValue();
	}

	public char MutableValueSupport.charValue() {
		return (char)longValue();
	}

	public float MutableValueSupport.floatValue() {
		return (float)doubleValue();
	}

	public int MutableValueSupport.intValue(){
		return (int)longValue();
	}
	
	
	/**
	 * @return stringValue()
	 */
	public String MutableValueSupport.toString() {
		return stringValue();
	}
	
}
