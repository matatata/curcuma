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

package de.ceruti.curcuma.foundation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.NSCollection;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.foundation.NSArray;
import de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodingUtils;


@KeyValueCodeable
class NSMutableArrayImpl<T> extends ArrayList<T>  implements NSArray<T>, ErrorHandling {

	protected NSMutableArrayImpl(T[] t) {
		super(Arrays.asList(t));
	}
	
	protected NSMutableArrayImpl(List<? extends T> a) {
		super(a);
	}
	

	protected NSMutableArrayImpl(int size) {
		super(size);
	}
	
	protected NSMutableArrayImpl(){
		super();
	}
	
	@Override
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy(this,key);
	}
	
	/**
	 * {@link Utils.objectsInRange(List<?>,Range)}
	 */
	@Override
	public List<T> objectsInRange(Range range) {
		return Utils.objectsInRange(this, range);
	}

	/**
	 * {@link Utils.objectsAtIndexes(List<?>,IndexSet)}
	 */
	@Override
	public List<T> objectsAtIndexes(IndexSet indexes)	{
		return Utils.objectsAtIndexes(this, indexes);
	}
	
	/**
	 * {@link Utils.removeRange(List<?>,Range)}
	 */
	@Override
	public void removeRange(Range r) {
		Utils.removeRange(this, r);
	}
	
	/**
	 * {@link Utils.removeAtIndexes(List<?>,IndexSet)}
	 */
	@Override
	public void removeAtIndexes(IndexSet indexes) {
		Utils.removeAtIndexes(this, indexes);
	}
	
	@Override
	public void setValueForUndefinedKey(Object value, Class type, String key)  {
		KeyValueCodingUtils.setValueForUndefinedKey((List)this, value, type, key);
	}
	
	
	@Override
	public Object getValueForUndefinedKey(String key) {
		return KeyValueCodingUtils.getValueForUndefinedKey((List)this, key);
	}
	
	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element,String key, boolean mutateSelf) {
		return KeyValueCodingUtils.createCompliantObjectForKey(this,element,key,mutateSelf);
	}
	
	@Override
	public void setNullValueForKey(String key) {
		throw new IllegalArgumentException();
	}

	
	@Override
	public NSCollection<T> immutableCopy() {
		return new NSImmutableArrayImpl<T>(this);
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public NSCollection<T> mutableCopy() {
		return new NSMutableArrayImpl<T>(this);
	}

	
}
