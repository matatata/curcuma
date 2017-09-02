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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.MutableIndexSet;
import de.ceruti.curcuma.api.core.Range;




public final class Factory {
	
	public static Range range(int loc){
		return new RangeImpl(loc,1);
	}
	
	public static Range range(Range s){
		return new RangeImpl(s);
	}
	
	public static Range range(int location,int length){
		return new RangeImpl(location,length);
	}
	
	public static IndexSet indexSet(Range range){
		return new IndexSetImpl(range);
	}
	
	public static IndexSet indexSet(int location, int length) {
		return new IndexSetImpl(range(location, length));
	}
	
	public static IndexSet indexSet(int index){
		return new IndexSetImpl(range(index));
	}
		
	public static IndexSet emptyIndexSet(){
		return IndexSetImpl.EMPTY_SET;
	}
	
	/**
	 * This seems to be a bottleneck. I guess its complexity might be O(a*b) where a is the length of c and b is the length of this list
	 * @param c
	 * @return
	 */
	public static IndexSet indexesInList(List<?> list,Collection<?> c) {
		MutableIndexSet indexSet = null;
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Object element = iter.next();
			int index = list.indexOf(element);
			if(index!=-1){
				if(indexSet==null)
					indexSet = Factory.mutableIndexSet(index);
				else
					indexSet.add(Factory.range(index));
			}
		}
		
		return indexSet;
	}
	
	public static MutableIndexSet mutableIndexSet(Range range){
		return new MutableIndexSetImpl(range);
	}
	
	public static MutableIndexSet mutableIndexSet(int index){
		return new MutableIndexSetImpl(range(index));
	}
		
	public static MutableIndexSet mutableIndexSet(){
		return new MutableIndexSetImpl();
	}
	
//	public static Value number(int integer){
//		return new CCInteger(integer);
//	}
//	
//	public static Value number(double dbl){
//		return new CCDouble(dbl);
//	}
//	
//	public static Value number(Number n){
//		return new CCNumber(n);
//	}
//	
//	public static Value string(String s){
//		return new CCString(s);
//	}
//	
//	private static Value objectValue(Object obj) {
//		if (obj == null)
//			return CCNull.getInstance();
//		return new CCObjectValue(obj);
//	}
//
//	public static Value Null(){
//		return CCNull.getInstance();
//	}
//	
//	public static Value value(Object obj) {
//		if(obj == null)
//			return Null();
//		if(obj instanceof Value)
//			return (Value)obj;
//		if(obj instanceof String)
//			return string((String)obj);
//		if(obj instanceof Number)
//			return number((Number)obj);
//		if(obj instanceof Boolean)
//			return number((Boolean)obj?1:0);
//		
//		return objectValue(obj);
//	}
	
	private Factory() {
	}
}
