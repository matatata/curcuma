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

package de.ceruti.curcuma.api.foundation;


import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.NSCollection;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;

public interface NSArray<T> extends List<T>, NSCollection<T>, KeyValueCoding {
	
	/**
	 * 
	 * @param range
	 * @return unmodifiable View
	 */
	public List<T> objectsInRange(Range range);

	/**
	 * 
	 * @param indexes
	 * @return unmodifiable List
	 */
	public List<T> objectsAtIndexes(IndexSet indexes);
	
	
	/**
	 * @param r
	 * @return;
	 */
	public void removeRange(Range r);

	/**
	 * @param indexes
	 */
	public void removeAtIndexes(IndexSet indexes);
}
