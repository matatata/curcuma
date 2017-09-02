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


abstract class AbstractMutableValue extends AbstractValue implements MutableValue{

	@Override
	public void setIntValue(int v) {
		setLongValue(v);
	}

	@Override
	public void setBoolValue(boolean b) {
		setLongValue(b ? 1L : 0L);
	}

	@Override
	public void setByteValue(byte b) {
		setLongValue(b);
	}

	@Override
	public void setCharValue(char c) {
		setLongValue(c);
	}

	@Override
	public void setFloatValue(float f) {
		setDoubleValue(f);
	}

	@Override
	public void setShortValue(short s) {
		setLongValue(s);
	}

	@Override
	public abstract void setDoubleValue(double d);

	@Override
	public abstract void setLongValue(long l);

	@Override
	public abstract void setObjectValue(Object o);

	@Override
	public abstract void setStringValue(String s);

	@Override
	public abstract double doubleValue();

	@Override
	public abstract long longValue();

	@Override
	public abstract Object objectValue();

	@Override
	public abstract String stringValue();


	@Override
	public abstract Object clone() throws CloneNotSupportedException;
}
