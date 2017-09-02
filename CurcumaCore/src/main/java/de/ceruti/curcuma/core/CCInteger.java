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

final class CCInteger extends AbstractValue {
	private int value;
	
	protected CCInteger(int value){
		this.value=value;
	}
	
	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public long longValue() {
		return value;
	}
	
	@Override
	public String stringValue() {
		return String.valueOf(value);
	}
	

	@Override
	public Object objectValue() {
		return Integer.valueOf(value);
	}
	
	@Override
	protected boolean equals(Value o) {
		return value==((CCInteger)o).value;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(value).hashCode();
	}

}
