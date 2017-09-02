package de.ceruti.curcuma.keyvalueobserving;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.core.utils.Selector;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.KeyValueCodingException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOException;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.core.Factory;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.core.utils.SelectorImpl;
import de.ceruti.curcuma.keyvaluecoding.ClassDescriptionCache;


public class KeyValueObservingUtils {

	private static Category logger = Logger.getInstance(KeyValueObservingUtils.class);
	
	private static Selector keyPathsForValuesAffectingValueForKeySelector;
	static {
		keyPathsForValuesAffectingValueForKeySelector = new SelectorImpl("keyPathsForValuesAffectingValueForKey",String.class);
		keyPathsForValuesAffectingValueForKeySelector.setReturnType(Set.class);
	}


	
	/**
	 * 
	 * @param clazz
	 * @param key
	 * @return
	 */
	public static Set<String> keyPathsForValuesAffectingValueForKey(Class<?> clazz, String key) {
		
		if(clazz==null || !KeyValueObserving.class.isAssignableFrom(clazz)){
			throw new IllegalArgumentException();
		}
		
		HashSet<String> result = new HashSet<String>();
		
		Method m = ClassDescriptionCache.getMethod(keyPathsForValuesAffectingValueForKeySelector, clazz);
		if(m!=null) {
			try {
				result.addAll((Set<String>) m.invoke(null, key));
			} catch (Throwable e){
				logger.error(e);
			}
			
			if(result.remove(key)){
				logger.warn("circular dep '" + key + "'");
			}
		}
		return result;
	}
	
	public static <T> void addObserverAtIndexes(List<T> list, KVObserver observer,IndexSet indexes,String keyPath,Object context,int options)
	{
		for(Iterator<Range> it = indexes.rangesIterator(); it.hasNext(); ){
			Range r = it.next();
			for(int i=r.getLocation();i<r.maxRange();i++) {
				addObserverAtIndex(list,observer, i , keyPath, context, options);
			}
		}
	}
	
	public static <T> void addObserver(List<T> list, KVObserver observer,String keyPath,Object context,int options) {
		addObserverInRange(list, observer, Factory.range(0,list.size()), keyPath, context, options);
	}
	
	public static <T> void addObserverInRange(List<T> list, KVObserver observer,Range r,String keyPath,Object context,int options)
	{
		for(int i=r.getLocation();i<r.maxRange();i++) {
			addObserverAtIndex(list,observer, i , keyPath, context, options);
		}
		
	}

	public static <T> void removeObserver(List<T> list, KVObserver observer,
			String keyPath) {
		removeObserverInRange(list, observer, Factory.range(0, list.size()),
				keyPath);
	}
	
	public static <T> void removeObserverInRange(List<T> list, KVObserver observer,Range r,String keyPath)
	{
		for(int i=r.getLocation();i<r.maxRange();i++) {
			removeObserverAtIndex(list,observer, i , keyPath);
		}
		
	}
	
	public static <T> void addObserverAtIndex(List<T> list, KVObserver observer,int index,String keyPath,Object context,int options) {
		T o = list.get(index);
		if(o instanceof KeyValueObserving) {
			((KeyValueObserving)o).addObserver(observer, keyPath, context, options);
		}
	}
	
	public static <T> void removeObserverAtIndexes(List<T> list, KVObserver observer,IndexSet indexes,String keyPath)
	{
		for(Iterator<Range> it = indexes.rangesIterator(); it.hasNext(); ){
			Range r = it.next();
			for(int i=r.getLocation();i<r.maxRange();i++) {
				removeObserverAtIndex(list,observer, i , keyPath);
			}
		}
	}
	
	public static <T> void removeObserverAtIndex(List<T> list, KVObserver observer,int index,String keyPath)
	{
		T o = list.get(index);
		if(o instanceof KeyValueObserving) {
			((KeyValueObserving)o).removeObserver(observer, keyPath);
		}
	}
	
	public static final Object INVALID_SNAPSHOT = new Object() {
		@Override
		public String toString() { return "INVALID_SNAPSHOT"; }
	};
	
	public static Object makeSnapshot(KeyValueCoding kvc,String keypath) {
		try {
			return kvc.getValueForKeyPath(keypath);
		}
		catch(KeyValueCodingException e)
		{
			logger.error(e);
			return INVALID_SNAPSHOT;
		}
	}
	

	public static List getListForKeyPath(KeyValueCoding kvc,String keypath) {
		Object theChangedArray = null;
		try {
			theChangedArray = kvc.getValueForKeyPath(keypath);
		} catch(NonCompliantAccessorFoundException e) {
			theChangedArray = kvc.mutableArrayValueForKeyPath(keypath);
		}
		
		if(!(theChangedArray instanceof List))
			throw new KVOException("makeSnapshot: " + keypath + " is not an instance of List");
		
		return (List) theChangedArray;
	}
	
	public static Object makeSnapshot(KeyValueCoding kvc,String keypath,IndexSet indexes) {
		Object theChangedArray = getListForKeyPath(kvc,keypath);

		return Utils.objectsAtIndexes((List)theChangedArray,indexes);
	}
	
}
