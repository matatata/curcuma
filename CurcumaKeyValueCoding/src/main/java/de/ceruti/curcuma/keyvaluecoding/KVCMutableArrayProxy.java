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

package de.ceruti.curcuma.keyvaluecoding;

import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.KeyValueCodingTargetException;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.core.utils.SelectorImpl;


@SuppressWarnings("unchecked")
@KeyValueCodeable
public class KVCMutableArrayProxy extends AbstractList {

	private final KeyValueCoding owner;
	private final String key;
	
	//insertObjectIn<Key>AtIndex(Object,int)
	protected Method insertObjectMethod;
	
	//Object removeObjectFrom<Key>AtIndex(int)
	protected Method removeObjectMethod;
	
	//Object replaceObjectIn<Key>WithObjectAtIndex(Object,int)
	protected Method replaceObjectMethod;
	
	//countOf<Key>()
	protected Method count;
	
	//objectIn<Key>AtIndex(int)
	protected Method getAt;
	
	//get<Key>InRange(Object[],NSRange)
	protected Method getInRange;
	

	private Method setter;
	
	private Method getter;
	
	
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy((KeyValueCoding) this,key);
	}
	
	public KVCMutableArrayProxy(KeyValueCoding owner,String key){
		this.owner = owner;
		this.key = key;
		initAccessorMethods();
	}
	

	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractList#set(int, java.lang.Object)
	 */
	@Override
	public Object set(int index, Object element) {
		
		if(replaceObjectMethod!=null){
			try {
				return replaceObjectMethod.invoke(getOwner(),element,index);
			} catch (Throwable t){
				throw new KeyValueCodingTargetException(t,replaceObjectMethod,getOwner(),getKey());
			}
		}
		else if(removeObjectMethod!=null && insertObjectMethod!=null) {
			Object old = get(index);
			
			try {
				removeObjectMethod.invoke(getOwner(),index);
			} catch (Throwable t){
				throw new KeyValueCodingTargetException(t,removeObjectMethod,getOwner(),getKey());
			}
			try {
				insertObjectMethod.invoke(getOwner(), element,index);
			} catch (Throwable t){
				throw new KeyValueCodingTargetException(t,insertObjectMethod,getOwner(),getKey());
			}
			return old;
		}
				
		List list = targetArray();
		try {
			return _set(list,index,element);
		} catch (UnsupportedOperationException e) {
			list = new ArrayList(list);
			Object ret = list.set(index, element);
			setTargetArray(list);
			return ret;
		}
		
	}
	

	

	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Object element) {
		if(insertObjectMethod!=null){	
			try {
				insertObjectMethod.invoke(getOwner(),element,index);
				return;
			} catch (Throwable t){
				throw new KeyValueCodingTargetException(t,insertObjectMethod,getOwner(),getKey());
			}
		}
		
		List list = targetArray();
		try {
			_add(list,index,element);
			return;
		} catch (UnsupportedOperationException e) {
			list = new ArrayList(list);
			list.add(index, element);
			setTargetArray(list);
		}
	}
	

	@Override
	public boolean addAll(Collection c) {
		if(insertObjectMethod!=null){	
			return super.addAll(c);
		}
		
		List list = targetArray();
		try {
			return _addAll(list,c);
		} catch (UnsupportedOperationException e) {
			list = new ArrayList(list);
			boolean ret = list.addAll(c);
			setTargetArray(list);
			return ret;
		}
		
	}
	
	
	@Override
	public boolean removeAll(Collection c) {
		if(removeObjectMethod!=null) {
			return super.removeAll(c);
		}
		
		List list = targetArray();
		try {
			return _removeAll(list, c);
		} catch (UnsupportedOperationException e) {
			list = new ArrayList(list);
			boolean ret = list.removeAll(c);
			setTargetArray(list);
			return ret;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public Object remove(int index) {
		
		if(removeObjectMethod!=null)
		{
			try {
				return removeObjectMethod.invoke(getOwner(),index);
			} catch (Throwable t){
				throw new KeyValueCodingTargetException(t,removeObjectMethod,getOwner(),getKey());
			}
		}
		
		List list = targetArray();
		try {
			return _remove(list, index);
		} catch (UnsupportedOperationException e) {
			list = new ArrayList(list);
			Object ret = list.remove(index);
			setTargetArray(list);
			return ret;
		}
	}
	
	
	/**
	 * 
	 * @param list
	 * @param index
	 * @return
	 * @throws UnsupportedOperationException
	 */
	protected Object _remove(List list, int index) throws UnsupportedOperationException {
		return list.remove(index);
	}

	/**
	 * 
	 * @param list
	 * @param index
	 * @param obj
	 * @throws UnsupportedOperationException
	 */
	protected void _add(List list, int index, Object obj) throws UnsupportedOperationException {
		list.add(index,obj);
	}
	
	protected boolean _addAll(List list, Collection c) throws UnsupportedOperationException {
		return list.addAll(c);
	}
	
	protected boolean _removeAll(List list, Collection c) throws UnsupportedOperationException {
		return list.removeAll(c);
	}

	/**
	 * 
	 * @param list
	 * @param index
	 * @param obj
	 * @return
	 * @throws UnsupportedOperationException
	 */
	protected Object _set(List list, int index, Object obj) throws UnsupportedOperationException {
		return list.set(index,obj);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public Object get(int index) {
		if(getAt!=null)
		{
			try {
				return getAt.invoke(getOwner(), index);
			} catch (Throwable e) {
				throw new KeyValueCodingTargetException(e,getAt,getOwner(),getKey());
			}
		}
		
		return targetArray().get(index);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		if(count!=null)
		{
			try {
				return (Integer) count.invoke(getOwner());
			} catch (Throwable e) {
				throw new KeyValueCodingTargetException(e,count,getOwner(),getKey());
			}
		}
		
		return targetArray().size();
	}
	
	public List objectsInRange(Range range) {
		if(getInRange!=null){
			try {
				Object[] dest = new Object[range.getLength()];
				getInRange.invoke(getOwner(), dest, range);
				return Arrays.asList(dest);
			} catch (Throwable e) {
				throw new KeyValueCodingTargetException(e,getInRange,getOwner(),getKey());
			}
		}
		
		return Utils.objectsInRange(targetArray(), range);
	}


	public List objectsAtIndexes(IndexSet indexes)	{
		return Utils.objectsAtIndexes(this, indexes);
	}
	
	public void removeRange(Range r) {
		Utils.removeRange(this, r);
	}

	public void removeAtIndexes(IndexSet indexes) {
		Utils.removeAtIndexes(this, indexes);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling#setValueForUndefinedKey(java.lang.Object, java.lang.String)
	 */
	public void setValueForUndefinedKey(Object value, Class type, String key)  {
		KeyValueCodingUtils.setValueForUndefinedKey((List)this, value, type, key);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling#getValueForUndefinedKey(java.lang.String)
	 */
	public Object getValueForUndefinedKey(String key) {
		return KeyValueCodingUtils.getValueForUndefinedKey((List)this, key);
	}
	
	public KeyValueCoding createCompliantObjectForKey(Object element,String key, boolean mutateSelf) {
		return KeyValueCodingUtils.createCompliantObjectForKey((KeyValueCoding)this,element,key, mutateSelf);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling#setNullValueForKey(java.lang.String)
	 */
	public void setNullValueForKey(String key) {
		throw new IllegalArgumentException();
	}
	
	private void initAccessorMethods(){
		if(getOwner()==null || getKey()==null)
			return;
			
		String _key = KeyPathUtilities.capitalizedKey(getKey());
		
		Class<?> ownerClass = getOwner().getClass();
		
		//insertMethod
		insertObjectMethod = new SelectorImpl("insertObjectIn" + _key + "AtIndex",Object.class,int.class).findMethod(ownerClass);
		//removeMethod
		removeObjectMethod = new SelectorImpl("removeObjectFrom" + _key + "AtIndex",/*Object.class,*/int.class).findMethod(ownerClass);
		//replaceMethod
		replaceObjectMethod = new SelectorImpl("replaceObjectIn" + _key + "WithObjectAtIndex",Object.class,int.class).findMethod(ownerClass);
		
		//countOf
		count = new SelectorImpl("countOf" + _key).findMethod(ownerClass);
		
		//getterAt
		getAt = new SelectorImpl("objectIn" + _key + "AtIndex",int.class).findMethod(ownerClass);
		
		//getterRange
		getInRange = new SelectorImpl("get" + _key + "InRange",Object[].class, Range.class).findMethod(ownerClass);
		
		
		//getter
		getter = new SelectorImpl("get" + _key).findMethod(ownerClass);
		
		//setter
		SelectorImpl setSel = new SelectorImpl("set" + _key,(Class<?>[])null);
		setSel.setRequiredArgCount(1);
		setter = setSel.findMethod(ownerClass);
		
	}
	
	
	protected List targetArray() {
		if(getter!=null)
			try {
				return (List) getter.invoke(getOwner());
			} catch (Throwable t){
				throw new KeyValueCodingTargetException(t,getter,getOwner(),getKey());
			}
		return (List)getOwner().getValueForKey(getKey());
	}
	
	protected void setTargetArray(List list) {
		if(setter!=null)
			try {
				setter.invoke(getOwner(),list);
				return;
			} catch (Throwable t){
				throw new KeyValueCodingTargetException(t,setter,getOwner(),getKey());
			}
		getOwner().setValueForKey(list,getKey());
	}


	protected KeyValueCoding getOwner() {
		return owner;
	}


	protected String getKey() {
		return key;
	}
	
	
	
	
}
