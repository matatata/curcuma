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

package de.ceruti.curcuma.api.keyvalueobserving;

public interface KVOOption {
	public final static int KeyValueObservingOptionNone = 0x0;
	public final static int KeyValueObservingOptionNew = 0x1;
	public final static int KeyValueObservingOptionOld = 0x2;
	public final static int KeyValueObservingOptionPrior = 0x4;

	public final static int KeyValueObservingOptionAll = KeyValueObservingOptionNew | KeyValueObservingOptionOld
			| KeyValueObservingOptionPrior;
	public static final int KeyValueObservingOptionOldNew = KeyValueObservingOptionNew | KeyValueObservingOptionOld;

}
