package de.ceruti.curcuma.foundation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.core.Factory;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.keyvaluecoding.KeyPathUtilities;
import de.ceruti.curcuma.keyvalueobserving.KVOIndexedChangeEvent;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservingUtils;

public class ObservableArray extends AbstractObservableArray {
	private static Category logger = Logger.getInstance(ObservableArray.class);
		
	
	private Collection<Proxy> proxies = new ArrayList<Proxy>();

	public ObservableArray(KeyValueCoding kvc, KeyValueObserving kvo,
			String key, List obsList) {
		super(kvc, kvo, key, obsList);
	}

	private void willChange(int KVOEventType, IndexSet indexSet) {
		for (Proxy proxy : proxies) {
			willChangeValuesAtIndexesForKey(KVOEventType, indexSet,
					KeyPathUtilities.extractHead(proxy.keyPath));
		}
	}

	private void didChange(int KVOEventType, IndexSet indexSet,
			Object remove, Object add) {
		for (Proxy proxy : proxies) {
			didChangeValuesAtIndexesForKey(KVOEventType, indexSet,
					KeyPathUtilities.extractHead(proxy.keyPath));

			if (remove != null) {
			
				if(remove instanceof KeyValueObserving)
					((KeyValueObserving)remove).removeObserver(proxy, proxy.keyPath);
				else if(remove instanceof List)
					KeyValueObservingUtils.removeObserver((List)remove, proxy, proxy.keyPath);
			}
			
			if (add != null)
				observeArrayElement(proxy, add);

		}
	}

	private void didChangeAndRemoveProxy(int KVOEventType, IndexSet indexSet,
			Object obj) {
		didChange(KVOEventType, indexSet,
				obj, null);
	}

	private void didChangeAndAddProxy(int KVOEventType, IndexSet indexSet,
			Object obj) {
		didChange(KVOEventType, indexSet, null,obj);
	}

	private void didChangeRemoveProxyFromOldAndAddProxyToNew(int KVOEventType,
			IndexSet indexSet, Object oldObj, Object newObj) {
		didChange(
				KVOEventType,
				indexSet,
				oldObj instanceof KeyValueObserving ? (KeyValueObserving) oldObj
						: null,newObj);
	}
	
	@Override
	protected Object _remove(List list, int index)
			throws UnsupportedOperationException {
		IndexSet indexSet = Factory.indexSet(index);
	
		willChange(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet);

		Object ret = super._remove(list, index);
		
		didChangeAndRemoveProxy(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet, ret instanceof KeyValueObserving ? ret : null);
		
		return ret;
	}
	
	@Override
	protected boolean _removeAll(List list, Collection c)
			throws UnsupportedOperationException {
		IndexSet indexSet = Utils.indexesInList(list, c);
		willChange(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet);
	

		ArrayList delta = new ArrayList(c);
		delta.retainAll(list);
		
		boolean ret = super._removeAll(list, c);
		
		
		didChangeAndRemoveProxy(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL, indexSet, delta);
		
		return ret;
	}

	
	@Override
	protected void _add(List list, int index, Object obj)
			throws UnsupportedOperationException {

		IndexSet indexSet = Factory.indexSet(index);
		willChange(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet);
	
		super._add(list, index, obj);
		
		didChangeAndAddProxy(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet, obj instanceof KeyValueObserving ? obj : null);
	}

	@Override
	protected boolean _addAll(List list, Collection c)
			throws UnsupportedOperationException {
		IndexSet indexSet = Factory.indexSet(list.size(),c.size());
		willChange(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet);
		boolean ret = super._addAll(list, c);
		didChangeAndAddProxy(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, indexSet, c);
		return ret;
	}
	

	
	@Override
	protected Object _set(List list, int index, Object obj) throws UnsupportedOperationException {
		IndexSet indexSet = Factory.indexSet(index);
		
		willChange(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT, indexSet);
		
		Object ret = super._set(list, index, obj);
		didChangeRemoveProxyFromOldAndAddProxyToNew(
				KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT, indexSet, ret,
				obj);
		return ret;
	}

	protected static final Object NO_SNAP = new Object();
	
	
	/**
	 * Observes each array element
	 */
	static class MyProxy extends Proxy {
		private ObservableArray array;
		MyProxy(ObservableArray arr,KVObserver obs, String keyPath, Object context, int options) {
			super(arr, obs, keyPath, context, options);
			this.array = arr;
		}

		protected Object snapshot = NO_SNAP;

		protected Object makeSnapshot(String keypath, KeyValueObserving object,IndexSet index) {
			return KeyValueObservingUtils.makeSnapshot(array,keypath,index);
		}
		
		protected KVOEvent makeEvent(String keypath, KeyValueObserving object, boolean isPrior, IndexSet index) {
			KVOIndexedChangeEvent evt = new KVOIndexedChangeEvent(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT,index);
			if ((this.options & KVOOption.KeyValueObservingOptionOld) != 0)
				evt.setOldValue((List<?>) snapshot);
			if (!isPrior && (this.options & KVOOption.KeyValueObservingOptionNew) != 0) {
				Object newObj = array.getValueForKeyPath(keypath);
				newObj = Utils.objectsAtIndexes((List<?>) newObj, index);
				evt.setNewValue((List<?>) newObj);
			}
			
			evt.setPriorEvent(isPrior);
			
			return evt;
		}

		private IndexSet calcIndex(Object object) {
			int i = array.indexOf(object);
			if(i==-1){
				logger.error(object + " is not an element of array " + array);
				return null;
			}
			
			return Factory.indexSet(i);
		}
		
		@Override
		public void observeValue(String keypath, KeyValueObserving object,
				KVOEvent change, Object context) {
			
			logger.debug("Proxy event=" + change);
			
			IndexSet index = calcIndex(object);
			if(index==null)
				return;
			
			switch (change.getKind()) {
				case KVOEvent.KEY_VALUE_CHANGE_SETTING:
					/*
				case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
				case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
				case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT: */{
					if (change.isPriorEvent()) {
						
						if ((this.options & KVOOption.KeyValueObservingOptionOld) != 0)
							snapshot = makeSnapshot(keypath,object,index);
						// if original observer was interested in PriorEvents so
						// notifiy it
						if ((this.options & KVOOption.KeyValueObservingOptionPrior) != 0) {
							KVOEvent evt = makeEvent(keypath, object, true,index);
							this.observer.observeValue(this.keyPath,
									(KeyValueObserving) this.owner, evt,
									this.context);
						}
					} else {
						
						KVOEvent evt = makeEvent(keypath, object, false,index);
						this.observer.observeValue(this.keyPath,
								(KeyValueObserving) this.owner, evt, this.context);
					}
				}break;
				default:
					logger.debug("ignoring: " + change);
					break;
			}
		}
	}
	
	protected void observeArrayElement(Proxy proxy, Object obs) {
		if(obs instanceof KeyValueObserving)
			((KeyValueObserving)obs).addObserver(proxy, proxy.keyPath, proxy.context, proxy.options
				| KVOOption.KeyValueObservingOptionPrior);
		else if(obs instanceof List)
			KeyValueObservingUtils.addObserver((List)obs, proxy, proxy.keyPath, proxy.context, proxy.options
					| KVOOption.KeyValueObservingOptionPrior);
	}
	
	@Override
	public void addObserver(KVObserver observer, String keyPath, Object context, int options) throws KVOException {
		Proxy proxy = new MyProxy(this,observer,keyPath,context,options);
		Range range = Factory.range(0, list.size());
		
		KeyValueObservingUtils.addObserverInRange(list, proxy, range,
				keyPath, new Object[]{context}, options
						| KVOOption.KeyValueObservingOptionPrior);
		proxies.add(proxy);

	}



	public void removeObserver(KVObserver observer, String keyPath) {
		Range range = Factory.range(0, list.size());

		for (Iterator<Proxy> it = proxies.iterator(); it.hasNext();) {
			Proxy proxy = it.next();

			if (proxy.observer == observer && proxy.owner == this
					&& proxy.keyPath.equals(keyPath)) {
				KeyValueObservingUtils.removeObserverInRange(list, proxy,
						range, keyPath);
				it.remove();
			}
		}
		
	}
	
	@Override
	public String toString() {
		return "ObservableArray[" + super.toString() + "]";
	}
	
}
