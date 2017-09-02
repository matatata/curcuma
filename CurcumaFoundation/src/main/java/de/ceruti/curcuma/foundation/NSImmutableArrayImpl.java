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

import java.util.Arrays;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;


@KeyValueCodeable
class NSImmutableArrayImpl<T> extends NSMutableArrayImpl<T> {
	
	protected NSImmutableArrayImpl(T[] t) {
		super(Arrays.asList(t));
	}
	
	protected NSImmutableArrayImpl(List<? extends T> a) {
		super(a);
	}
	

	protected NSImmutableArrayImpl(int size) {
		super(size);
	}
	
	protected NSImmutableArrayImpl(){
		super();
	}
	

	@Override
	public boolean isMutable() {
		return false;
	}
		
	/**
	 * {@link Utils.removeRange(List<?>,Range)}
	 */
	@Override
	public void removeRange(Range r) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@link Utils.removeAtIndexes(List<?>,IndexSet)}
	 */
	@Override
	public void removeAtIndexes(IndexSet indexes) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setValueForUndefinedKey(Object value, Class type, String key)  {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public void setNullValueForKey(String key) {
		throw new IllegalArgumentException();
	}

	@Override
	public void add(int index, Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(java.util.Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, java.util.Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(java.util.Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(java.util.Collection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object set(int index, Object element) {
		throw new UnsupportedOperationException();
	}
	
}
