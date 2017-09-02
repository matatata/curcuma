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




final class CCNumber extends AbstractValue {
	private Number number;
	
	public CCNumber(Number n){
		this.number = n;
	}

	@Override
	public byte byteValue() {
		return number.byteValue();
	}

	@Override
	public double doubleValue() {
		return number.doubleValue();
	}

	@Override
	public long longValue() {
		return number.longValue();
	}
	
	@Override
	public float floatValue() {
		return number.floatValue();
	}
	
	@Override
	public short shortValue() {
		return number.shortValue();
	}

	@Override
	public int intValue() {
		return number.intValue();
	}

	@Override
	public String stringValue() {
		return number.toString();
	}
	

	@Override
	public Object objectValue() {
		return number;
	}

	@Override
	protected boolean equals(Value o) {
		return number==((CCNumber)o).number;
	}

	@Override
	public int hashCode() {
		return number.hashCode();
	}
}
