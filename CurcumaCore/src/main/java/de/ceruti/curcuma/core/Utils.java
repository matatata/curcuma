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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.MutableIndexSet;
import de.ceruti.curcuma.api.core.Range;


public class Utils {

	public static <T> List<T> objectsAtIndexes(List<T> list, IndexSet indexes) {
		ArrayList<T> result = new ArrayList<T>();
		if(indexes!=null && !indexes.isEmpty()){
  		for(Iterator<Range> it = indexes.rangesIterator(); it.hasNext(); ){
  			Range r = it.next();
  			result.addAll(subList(list,r));
  		}
		}
		return /*Collections.unmodifiableList(*/result/*)*/;
	}
	
	public static <T> List<T> objectsInRange(List<T> list,Range range) {
		return Collections.unmodifiableList(subList(list,range));
	}
	
	public static void removeAtIndexes(List<?> list,IndexSet iset) {
		int prevLength = 0;
		if(iset!=null && !iset.isEmpty()){
  		for(Iterator<Range> it = iset.rangesIterator(); it.hasNext();) {
  			Range r = it.next();
  			subList(list, r).clear();
  			prevLength += r.getLength();
  		}
		}
	}
	
	/**
	 * This seems to be a bottleneck. I guess its complexity might be O(a*b) where a is the length of c and b is the length of this list
	 * @param c
	 * @return
	 */
	public static IndexSet indexesInList(List list,Collection c) {
		MutableIndexSet indexSet = Factory.mutableIndexSet();
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Object element = iter.next();
			int index = list.indexOf(element);
			if(index!=-1){
				indexSet.add(Factory.range(index));
			}
		}
		
		return indexSet;
	}
	
	public static void removeRange(List<?> list,Range r) {
		subList(list, r).clear();
	}
	
	/**
	 * Returns a view of the portion of <code>list</code>
	 * @param <T>
	 * @param list
	 * @param range
	 * @return
	 * @see java.util.List.subList(int fromIndex, int toIndex)
	 */
	protected static <T> List<T> subList(List<T> list,Range range) {
		return list.subList(range.getLocation(), range.maxRange());
	}
}
