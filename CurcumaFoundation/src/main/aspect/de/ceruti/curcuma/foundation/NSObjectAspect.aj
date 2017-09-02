package de.ceruti.curcuma.foundation;

import java.util.List;

import de.ceruti.curcuma.api.foundation.NSObject;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingCreator;
import de.ceruti.curcuma.keyvaluecoding.DefaultErrorHandling;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservable;

public aspect NSObjectAspect {
	
	// marker interface  
	private interface NSObjectSupport {}
	
	declare @type: @NSObject *: @KeyValueCodeable;
	declare @type: @NSObject *: @DefaultErrorHandling;
	declare @type: @NSObject *: @KeyValueObservable;
	declare @type: @NSObject *: @KeyValueBindingCreator;
	declare parents : @NSObject * implements NSObjectSupport;  
	
	/**
	 * FIXME this would interfere with KeyValueCodingAspect.mutableArrayValueForKey if it was there
	 * @param key
	 * @return
	 */
	public List NSObjectSupport.mutableArrayValueForKey(String key) {
		return new KVOMutableArrayProxy((KeyValueCoding)this,(KeyValueObserving)this,key);
	}
}
