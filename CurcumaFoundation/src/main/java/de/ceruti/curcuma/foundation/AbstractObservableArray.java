package de.ceruti.curcuma.foundation;

import java.util.List;

import de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodingUtils;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservable;

@KeyValueObservable
abstract class AbstractObservableArray extends KVOMutableArrayProxy implements
		ErrorHandling, KeyValueCoding {
	
	protected List list;

	@Override
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy(this,key);
	}
	
	/**
	 * 
	 * @param kvc
	 * @param kvo
	 * @param key
	 * @param obsList may be null
	 */
	public AbstractObservableArray(KeyValueCoding kvc, KeyValueObserving kvo, String key, List obsList){
		super(kvc,kvo,key);
		this.list = obsList;
	}
	
	@Override
	protected void setTargetArray(List list){
		if(this.list!=null)
			throw new UnsupportedOperationException();
		super.setTargetArray(list);
	}
	
	@Override
	protected List targetArray() {
		if(this.list==null)
			return super.targetArray();
		return this.list;
	}

//
//	public void setValueForUndefinedKey(Object value, Class type, String key)  {
//		KeyValueCodingUtils.setValueForUndefinedKey((List)this, value, type, key);
//	}
//	
//	public Object getValueForUndefinedKey(String key) {
//		return KeyValueCodingUtils.getValueForUndefinedKey((List)this, key);
//	}
	
//	@Override
//	public Object getValueForKey(String key) {
//		
//	}
	
	@Override
	public void setValueForKey(Object value, Class<?> type, String key) {
		KeyValueCodingUtils.setValueForUndefinedKey((List)this, value, type, key);
	}
	
	@Override
	public Object getValueForKey(String key) {
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
	
	

}
