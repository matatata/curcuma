package de.ceruti.curcuma.foundation;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.core.Factory;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
/**
 * 
 * Overrides the abstract mutators of {@link AbstractList} in order to post Key-Value-Observing-Notifications.
 * If the observed object implements the Key-Value-Observing-Mutators, this class relies on them - assuming they'll notify the Obervers.
 */
public class KVOMutableArrayProxy extends KVCMutableArrayProxy {

	private final KeyValueObserving KVOowner;
	
	public KVOMutableArrayProxy(KeyValueCoding kvcOwner,KeyValueObserving kvoOwner,String key){
		super(kvcOwner,key);
		this.KVOowner = kvoOwner;
		
	}
	
//	@Override
//	public void add(int arg0, Object arg1) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION))
//		{
//			super.add(arg0, arg1);
//			return;
//		}
//		
//		IndexSet indexSet = Factory.indexSet(arg0);
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet , key);
//		super.add(arg0, arg1);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION,  indexSet , key);
//	}
	
	/**
	 * 
	 * @param list
	 * @param index
	 * @return
	 * @throws UnsupportedOperationException
	 */
	@Override
	protected Object _remove(List list, int index) throws UnsupportedOperationException {
		IndexSet indexSet = Factory.indexSet(index);
		KVOowner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , getKey());
		Object ret = super._remove(list,index);
		KVOowner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , getKey());
		return ret;
	}

	/**
	 * 
	 * @param list
	 * @param index
	 * @param obj
	 * @throws UnsupportedOperationException
	 */
	@Override
	protected void _add(List list, int index, Object obj) throws UnsupportedOperationException {
		IndexSet indexSet = Factory.indexSet(index);
		KVOowner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet , getKey());
		super._add(list, index, obj);
		KVOowner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION,  indexSet , getKey());
	}
	
	@Override
	protected boolean _addAll(List list, Collection c)
			throws UnsupportedOperationException {
		IndexSet indexSet = Factory.indexSet(list.size(),c.size());
		KVOowner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet , getKey());
		boolean ret = super._addAll(list, c);
		KVOowner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION,  indexSet , getKey());
		return ret;
	}
	
	@Override
	protected boolean _removeAll(List list, Collection c)
			throws UnsupportedOperationException {
		IndexSet indexSet = Utils.indexesInList(list, c);
		KVOowner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , getKey());
		boolean ret = super._removeAll(list, c);
		KVOowner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , getKey());
		return ret;
	}

	/**
	 * 
	 * @param list
	 * @param index
	 * @param obj
	 * @return
	 * @throws UnsupportedOperationException
	 */
	@Override
	protected Object _set(List list, int index, Object obj) throws UnsupportedOperationException {
		IndexSet indexSet = Factory.indexSet(index);
		KVOowner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT, indexSet , getKey());
		Object ret = super._set(list,index,obj);
		KVOowner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT, indexSet , getKey());
		return ret;
	}

	
	
//
//	public boolean add(Object arg0) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION))
//		{
//			return super.add(arg0);
//		}
//		
//		NSIndexSet indexSet = NSFactory.indexSet(size());
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet , key);
//		boolean ret = super.add(arg0);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION,  indexSet , key);
//		return ret;
//	}

//	public boolean addAll(Collection arg0) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION))
//		{
//			return super.addAll(arg0);
//		}
//		
//		NSIndexSet indexSet = NSFactory.indexSet(NSFactory.range(size(),arg0.size()));
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet , key);
//		boolean ret = super.addAll(arg0);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION,  indexSet , key);
//		return ret;
//	}

//	public void clear() {
//		if(isEmpty()){
//			super.clear();
//			return;
//		}
//		
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL))
//		{
//			super.clear();
//			return;
//		}
//		
//		NSIndexSet indexSet = NSFactory.indexSet(NSFactory.range(0,size()-1));
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		super.clear();
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//	}

//	@Override
//	public Object remove(int arg0) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL))
//		{
//			return super.remove(arg0);
//		}
//		
//		IndexSet indexSet = Factory.indexSet(arg0);;
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		Object ret = super.remove(arg0);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		return ret;
//	}
	
//	@Override
//	public void removeRange(Range o) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL))
//		{
//			super.removeRange(o);
//			return;
//		}
//		
//		IndexSet indexSet = Factory.indexSet(o);
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		super.removeRange(o);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//	}

	
//	public boolean remove(Object arg0) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL))
//		{
//			return super.remove(arg0);
//		}
//		
//		
//		int index = indexOf(arg0);
//		if(index==-1)
//			return false;
//		NSIndexSet indexSet = NSFactory.indexSet(index);
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		boolean ret = super.remove(arg0);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		return ret;
//	}
	
//	public boolean removeAll(Collection arg0) {
//		
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL))
//		{
//			return super.removeAll(arg0);
//		}
//		
//		
//		NSIndexSet indexSet = Utils.indexesInList((List<?>) this, arg0);
//		
//		if(indexSet!=null)
//			owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		boolean ret = super.removeAll(arg0);
//		if(indexSet!=null)
//			owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		return ret;
//	}

//	@Override
//	public void removeAtIndexes(IndexSet indexSet) {
//		if(indexSet.isEmpty()) return;
//		
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL))
//		{
//			super.removeAtIndexes(indexSet);
//			return;
//		}
//		
//		
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//		super.removeAtIndexes(indexSet);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet , key);
//	}

//	@Override
//	public Object set(int arg0, Object arg1) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT))
//		{
//			return super.set(arg0,arg1);
//		}
//		
//		IndexSet indexSet = Factory.indexSet(arg0);
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT, indexSet , key);
//		Object ret = super.set(arg0, arg1);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT, indexSet , key);
//		return ret;
//	}

//	public boolean addAll(int arg0, Collection arg1) {
//		if(useMutationAccessors(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION))
//		{
//			return super.addAll(arg0,arg1);
//		}
//		
//		
//		NSIndexSet indexSet = NSFactory.indexSet(NSFactory.range(arg0,arg1.size()));
//		owner.willChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet , key);
//		boolean ret = super.addAll(arg0, arg1);
//		owner.didChangeValuesAtIndexesForKey(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION,  indexSet , key);
//		return ret;
//	}
	
//	protected boolean useMutationAccessors(int KVOChangeKind) {
//		switch(KVOChangeKind){
//		case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
//			if(insertObjectMethod!=null)
//				return true;
//			break;
//		case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
//			if(removeObjectMethod!=null)
//				return true;
//			break;
//		case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
//			//replaceMethode ist da, oder insert und remove exisitert
//			if(replaceObjectMethod!=null || (removeObjectMethod!=null && insertObjectMethod!=null))
//				return true;
//			break;
//		default:
//			return false;
//		}
//		
//		return false;
//	}
	
}
