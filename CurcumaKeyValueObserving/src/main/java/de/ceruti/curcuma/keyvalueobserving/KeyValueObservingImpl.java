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

package de.ceruti.curcuma.keyvalueobserving;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.core.utils.CollectionMap;
import de.ceruti.curcuma.keyvaluecoding.KeyPathUtilities;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodingUtils;


class KeyValueObservingImpl  {
	private static Category logger = Logger.getInstance(KeyValueObservingImpl.class);
	
	/**
	 * x -> [ObserverInfo] (some of them might be propagating backwards)
	 */
	private ObserverInfoListMap key2ObserverInfos = new ObserverInfoListMap();
	
	//x -> (y.z -> [ObserverInfos])
	private Map<String,ObserverInfoListMap> forwards = new HashMap<String,ObserverInfoListMap>();


	/**
	 * Wenn ein Observer o f�r den Keypath x.y registeriert wurde, so werden
	 * forwarder eingerichtet, die �nderungen von y, nach o propagieren. Bei
	 * einem l�ngerem Keypath geht das rekursiv so weiter. z.B. Sei o f�r x.y.z
	 * registriert, so leitet ein forwarder �nderungen von z nach y und ein
	 * weiterer von y hierher.
	 */
//	private static KVObserver backwardPropagator = new BackwardPropagator();
	
	private static KVObserver dependentKeyPathNotifier = new DependentKeyPathNotifier();
	
	private static class ObserverInfoList extends ArrayList<ObserverInfo> {
		
	}
	
	private static class ObserverInfoListMap extends CollectionMap<String,ObserverInfo> {
		@Override
		protected Collection<ObserverInfo> createCollection(){
			return new ObserverInfoList();
		}
	}
	

	
	
	
	
	
	private ObserverInfoList getForwardersForKeyPath(String key,String tail,boolean autocreate){
		ObserverInfoListMap c = null;
		if(!forwards.containsKey(key))
		{	
			c = new ObserverInfoListMap();
			forwards.put(key,c);
			return (ObserverInfoList) c.get(tail,autocreate);
		}
		else
			return (ObserverInfoList) forwards.get(key).get(tail,autocreate);
	}
	
		
	private void _cleanForwards(String head,String tail) {
		CollectionMap<String,ObserverInfo> c = forwards.get(head);
		if(c==null)
			return;
		
		if(c.containsKey(tail) && c.get(tail).isEmpty())
			c.remove(tail);
		
		
		if(c.isEmpty())
			forwards.remove(head);
	}
	
	private static class ObserverInfo {
		private KVObserver observer;
		private Object context;
		private int options;
		private String keyPath;
		private KeyValueObserving observedObject;
		private KeyValueCoding kvc;
		private String head;
		private Object snapshot = KeyValueObservingUtils.INVALID_SNAPSHOT;
		private BackwardPropagator bp;
		

		
		ObserverInfo(KVObserver observer,Object context,int options,KeyValueObserving observedObject,KeyValueCoding kvc,String keyPath){
			this.observer = observer;
			this.context = context;
			this.options = options;
			this.observedObject = observedObject;
			this.keyPath = keyPath;
			this.kvc = kvc;
			
			StringBuilder h = new StringBuilder();
			if(KeyPathUtilities.extractHeadAndTail(keyPath,h,null)){
				this.head = h.toString();
			}
			else
				this.head = keyPath;
		}
		

		boolean isBackwardPropagation() {
//			return observer==backwardPropagator;
			return observer instanceof BackwardPropagator;
		}
		
		Object makeSnapshot() {
			return makeSnapshot(true);
		}
		
		/**
		 * Erzeugt einen Snapshot und initialisiert die Instanz-Variable snapshot damit, falls der Observer-Options �berhaupt an einem Snapshot interessiert ist.
		 * @return INVALID_SNAPSHOT falls kein Snapshot erzeugt wurde
		 */
		Object makeSnapshot(boolean useAsSnapshot) {
			
			Object _snapshot = null;
			if(isBackwardPropagation())
				return /*_snapshot = */((ObserverInfo)context).makeSnapshot(useAsSnapshot);
			else
				_snapshot = needsSnapshot() ? KeyValueObservingUtils.makeSnapshot(kvc, keyPath) : KeyValueObservingUtils.INVALID_SNAPSHOT;
			
			if(useAsSnapshot)
				snapshot = _snapshot;
			return _snapshot;
		}
		
		boolean requiresOldValue() {
			return (options & KVOOption.KeyValueObservingOptionOld) != 0;
		}
		
		boolean requiresNewValue() {
			return (options &  KVOOption.KeyValueObservingOptionNew) != 0 ;
		}
		
		boolean expectsPriorEvent() {
			return (options & KVOOption.KeyValueObservingOptionPrior) != 0;
		}
		
		boolean needsSnapshot() {
			return requiresOldValue() ||
				expectsPriorEvent();
		}
		
		
		Object makeSnapshot(IndexSet indexes) {
			return makeSnapshot(indexes,true);
		}
		
		Object makeSnapshot(IndexSet indexes,boolean useAsSnapshot) {
			Object _snapshot = null;
			if(isBackwardPropagation())
				return /*_snapshot= */((ObserverInfo)context).makeSnapshot(indexes,useAsSnapshot);
			else
				_snapshot = needsSnapshot() ? KeyValueObservingUtils.makeSnapshot(kvc, keyPath, indexes) : KeyValueObservingUtils.INVALID_SNAPSHOT;
			if(useAsSnapshot)
				snapshot = _snapshot;
			return _snapshot;
		}
			
		
		
		void invalidateSnapshot() {
			setSnapshot(KeyValueObservingUtils.INVALID_SNAPSHOT);
		}
		
		boolean hasValidSnapshot() {
			return getSnapshot()!=KeyValueObservingUtils.INVALID_SNAPSHOT;
		}
		
		void setSnapshot(Object snapshot) {
			if(isBackwardPropagation()){
				((ObserverInfo)context).setSnapshot(snapshot);
				return;
			}
			this.snapshot = snapshot;
		}
		
		Object getSnapshot() {
			if(isBackwardPropagation()){
				return ((ObserverInfo)context).getSnapshot();
			}
			return this.snapshot;
		}
		
		/**
		 * @param newVal set to unitializedValue if you don't have the new current value. If the observer is our forwarder newVal is ignored, since it is not the new Value we want.
		 * @return
		 */
		KVOEvent notifyChange(Object newVal){
			
			if(isBackwardPropagation())
			{
				//the forwards must fetch their own newVal
				return ((ObserverInfo)context).notifyChange(unitializedVal);
				
			}
			
			if(requiresNewValue() && newVal==unitializedVal){
				newVal = kvc.getValueForKeyPath(keyPath);
			}
//			if((options & KVOOption.KeyValueObservingOptionOldNewCompare) == KVOOption.KeyValueObservingOptionOldNewCompare
//					&& hasValidSnapshot()
//					//&& !expectsPriorEvent()
//					)
//			{
//				if(snapshot==newVal)
//					return null;
//				else if(snapshot!=null && snapshot.equals(newVal))
//					return null;
//				else if(newVal!=null && newVal.equals(snapshot))
//					return null;	
//			}
//			if(logger.isDebugEnabled())
//				logger.debug("notifying " + observer + " about '" + keyPath + "' old=" +snapshot + ", new="+ newVal);
			KVOSettingEvent event = new KVOSettingEvent();
			
			if(requiresOldValue() && hasValidSnapshot()) {
				event.setOldValue(snapshot);
			}
			if(requiresNewValue()) {
				assert(newVal != unitializedVal);
				event.setNewValue(newVal);
			}
			sendEvent(event);
			return event;
		}
			
		void sendEvent(KVOEvent change)
		{
//			logger.debug(this.getClass().getSimpleName() + ".sendEvent() to observer " + observer + "Event=" + change + " this=" + this);
			observer.observeValue(keyPath,observedObject, change, context);
		}

		public void notifyChange(int KVOChangeKind,IndexSet indexes) {
			if(isBackwardPropagation())
			{
				((ObserverInfo)context).notifyChange(KVOChangeKind,indexes);
				return;
			}
			
			KVOIndexedChangeEvent evt = new KVOIndexedChangeEvent(KVOChangeKind, indexes);
			
			Object modifiedArray = KeyValueObservingUtils.getListForKeyPath(kvc,keyPath);
			
			if(KVOChangeKind != KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL &&
					requiresNewValue()
					&& modifiedArray!=null /*&& modifiedArray instanceof List*/
			){
				if(!(modifiedArray instanceof List))
					throw new KVOException("makeSnapshot: " + keyPath + " is not an instance of List");
				
				List newVal = Utils.objectsAtIndexes((List)modifiedArray,indexes);
				evt.setNewValue(newVal);
			}
			
			if(requiresOldValue()&& hasValidSnapshot())
				evt.setOldValue((List)snapshot);
			
			sendEvent(evt);
		}
	}
	
	private Map<String,KVObserver> depKeysObserved = new HashMap<String,KVObserver>();

	
	private KeyValueObserving kvo;
	private KeyValueCoding kvc;
	private Object bean;
	
	
	private void addObserverForProperty(KVObserver observer, String key, Object context, int options) {
//		if(!kvc.isDefinedKey(key)){
//			logger.warn("Observer " + observer + " cannot yet observe " + key + " of " + kvc );
//		}
		
		boolean isDependentKeyPathNotifier = (observer instanceof DependentKeyPathNotifier);
		
		if(!isDependentKeyPathNotifier || !depKeysObserved.containsKey(key)){
			ObserverInfo obsInfo = new ObserverInfo(observer,context,options,kvo,kvc,key);
			key2ObserverInfos.add(key, obsInfo);
			logger.debug("added observer [" + obsInfo.observer + "] to Object ["  + this + "] for property '" + key + "' Observers: " + key2ObserverInfos.get(key));

//			if(isDependentKeyPathNotifier) {
//				depKeysObserved.put(key,observer);
//			}
			
			Set<String> depPaths = KeyValueObservingUtils.keyPathsForValuesAffectingValueForKey(bean.getClass(),key);
				
			for(String kp : depPaths){
				this.addObserver(dependentKeyPathNotifier, kp, obsInfo, KVOOption.KeyValueObservingOptionPrior);
				depKeysObserved.put(kp,dependentKeyPathNotifier);
			}
		}
	}
	
	private void removeObserverForProperty(KVObserver observer, String key) {
		if(!key2ObserverInfos.containsKey(key))
			return;
		ObserverInfoList obsInfos = (ObserverInfoList) key2ObserverInfos.get(key);
		
		for (Iterator it = obsInfos.iterator(); it.hasNext();) {
			ObserverInfo obsInfo = (ObserverInfo) it.next();
			if(obsInfo.observer == observer){
				it.remove();
				logger.debug("removed observer [" + observer + "] from Object ["  + this + "] for property '" + key + "' Observers: " + key2ObserverInfos.get(key));
			}
		}
		if(obsInfos.isEmpty())
			key2ObserverInfos.remove(key);
		
		if(!hasObserversForKey(key)){
			Set<String> depPaths = KeyValueObservingUtils.keyPathsForValuesAffectingValueForKey(bean.getClass(),key);
			for(String kp : depPaths){
				this.removeObserver(dependentKeyPathNotifier, kp);
				depKeysObserved.remove(kp);
			}
		}


	}
	
	public void printInfo(PrintStream pw)
	{
		pw.println(this.toString());
		pw.println("observer: " + key2ObserverInfos);
		pw.println("forwards: " + forwards);
		pw.println("depKeysObserved: " + depKeysObserved);		
	}
	
	private static class BackwardPropagator implements KVObserver {

		@Override
		public void observeValue(String keypath, KeyValueObserving object, KVOEvent change, Object context) {
			((ObserverInfo)context).sendEvent(change);
		}
		
		
		@Override
		public String toString(){
			return "BackwardPropagator";
		}
		
	}
	
	private static class DependentKeyPathNotifier implements KVObserver {
		
		@Override
		public void observeValue(String keypath, KeyValueObserving object, KVOEvent change, Object context) {
			ObserverInfo info = (ObserverInfo)context;
			
			//TODO maybe respect type of Event.
			switch(change.getKind()) {
			
			/*case KVOEvent.KEY_VALUE_CHANGE_SETTING: */
			default: {
				if(change.isPriorEvent()){
					info.observedObject.willChangeValueForKey(info.head);
				}
				else {
					info.observedObject.didChangeValueForKey(info.head);
				}
			} break;
			
			
			}
		}
		
		@Override
		public String toString(){
			return "DependentKeyPathNotifier";
		}
	}
	
	public void addObserver(KVObserver observer, String keyPath, Object context, int options) throws KVOException {
		
//		if(isDefinedKeyPath(keyPath) != IsDefinedKeyReturnCode.YES){
//			throw new KVOException("Cannot observe " + keyPath + " of " + kvc);
//			logger.warn("Observer " + observer + " cannot observe " + keyPath + " of " + this );
//		}
		
		StringBuilder headBuf = new StringBuilder();
		StringBuilder tailBuf = new StringBuilder();
		
		
		if(!KeyPathUtilities.extractHeadAndTail(keyPath,headBuf,tailBuf)) {
			addObserverForProperty(observer, keyPath, context, options);
			return;
		}
		
		String head = headBuf.toString();
		String tail = tailBuf.toString();
		
		ObserverInfo obsInfo = new ObserverInfo(observer,context,options,kvo,kvc,keyPath);
		
		
		
				
		Object o = null;
		try {
			o = kvc.getValueForKey(head);
		}
		catch(NonCompliantAccessorFoundException e)
		{
			throw new KVOException(e);
		}
	
		Collection<ObserverInfo> m = getForwardersForKeyPath(head,tail,true);
		m.add(obsInfo);
		
		BackwardPropagator bp = new BackwardPropagator();
		obsInfo.bp = bp;
		
		if(!(o instanceof KeyValueObserving))
		{
			logger.debug("could not add observer " + observer + " to " + o + " this time");
			return;
		}

//		((KeyValueObserving)o).addObserver(backwardPropagator, tail, obsInfo, options);

		((KeyValueObserving)o).addObserver(bp, tail, obsInfo, options);
	}
	


	private void notifyObserversAboutSettingEvent(String key, boolean reattachForwarder) {

		if(!hasObserversForKey(key))
			return;

		Object theValueForKey = unitializedVal;

		ObserverInfoList obs = (ObserverInfoList) key2ObserverInfos.get(key);
		if(obs!=null) for (Iterator<ObserverInfo> iter = obs.iterator(); iter.hasNext();) {
			ObserverInfo obsInfo = iter.next();
			
			if(!obsInfo.isBackwardPropagation()) {
				//optimize, so that newVal doesn't need to be fetched all the time
				if(obsInfo.requiresNewValue()
						&& theValueForKey==unitializedVal) {
					
					//TODO: theValueForKey = obsInfo.hasValidSnapshot() ? obsInfo.getSnapshot() : getValueForKey(key);
					theValueForKey = kvc.getValueForKey(key);
				}
				
				obsInfo.notifyChange(theValueForKey);
			}
			else {
				// Update: It's not that easy, since obsInfo may be propagating backwards
				obsInfo.notifyChange(unitializedVal);
			}
			
			
			
			obsInfo.invalidateSnapshot();
		}

		if(forwards.containsKey(key))
		{
			if(theValueForKey==unitializedVal)
				theValueForKey = kvc.getValueForKey(key);

			for (Iterator<String> iter = forwards.get(key).keySet().iterator(); iter.hasNext();) {
				String tail = iter.next();
				ObserverInfoList obsInfoList = getForwardersForKeyPath(key, tail, false);
				
				Object sharedNewValue = unitializedVal;
				
				for(Iterator<ObserverInfo> obsIterator = obsInfoList.iterator();  obsIterator.hasNext();)
				{
					ObserverInfo obsInfo = obsIterator.next();
					if(reattachForwarder && theValueForKey instanceof KeyValueObserving) {
						try{
							((KeyValueObserving)theValueForKey).addObserver(obsInfo.bp, tail,obsInfo,obsInfo.options);
						}catch(KVOException e) {
							logger.debug("Cannot observe '" + tail + "' of Object [" + theValueForKey + "]");
						}
					}
					// Und �nderung bekannt machen
					{
						if(obsInfo.requiresNewValue()
								&& sharedNewValue==unitializedVal) {
							sharedNewValue = kvc.getValueForKeyPath(obsInfo.keyPath);
						}
						
//						obsInfo.notifyChange(unitializedVal); //Note that we cannot use newVal here, since we are dealing with forwards and the theValueForKey does not match
						obsInfo.notifyChange(sharedNewValue);
						obsInfo.invalidateSnapshot();
					}
				}
			}
		}
	}
	
	public void didChangeValueForKeyPath(String keyPath) {
		StringBuffer lastKey = new StringBuffer();
		Object target = KeyValueCodingUtils.findTarget(kvc,keyPath,lastKey,false);
		if(target instanceof KeyValueObserving) {
			((KeyValueObserving)target).didChangeValueForKey(lastKey.toString());
		}
	}
	
	public void willChangeValueForKeyPath(String keyPath) {
		StringBuffer lastKey = new StringBuffer();
		Object target = KeyValueCodingUtils.findTarget(kvc,keyPath,lastKey,false);
		if(target instanceof KeyValueObserving) {
			((KeyValueObserving)target).willChangeValueForKey(lastKey.toString());
		}
	}
	
	public void didChangeValueForKey(String key) {
		notifyObserversAboutSettingEvent( key, true);
	}
	
	public void postEventForKey(String key,KVOEvent e){
		if(!hasObservers())
			return;
		ObserverInfoList obs = (ObserverInfoList) key2ObserverInfos.get(key);
		if(obs==null) 
			return;

		for (Iterator<ObserverInfo> iter = obs.iterator(); iter.hasNext();) {
			ObserverInfo obsInfo =  iter.next();
			obsInfo.sendEvent(e);
		}

	}
	
	public void postEventForKeyPath(String keyPath,KVOEvent e){
		int dotPos = keyPath.lastIndexOf('.');
		if(dotPos==-1){
			postEventForKey(keyPath,e);
			return;
		}
		
		String head = keyPath.substring(0, dotPos); 
		String tail = keyPath.substring(dotPos+1,keyPath.length());
		
		Object o = kvc.getValueForKeyPath(head);
		if(o==null || !(o instanceof KeyValueObserving))
			throw new IllegalStateException();
		
		((KeyValueObservingImpl)o).postEventForKeyPath(tail, e);
		

	}
	
	public void didChangeValuesAtIndexesForKey(int KVOChangeKind, IndexSet indexes, String key) {
		if(indexes.isEmpty())
			return;
		
		notifyObserversAboutIndexedEvent(KVOChangeKind,indexes,key);
		
	}
	
	private void notifyObserversAboutIndexedEvent(int KVOChangeKind, IndexSet indexes, String key/*,HashSet<String> alreadyNotifiedDependents*/) {

		if(!hasObserversForKey(key))
			return;

		ObserverInfoList obs = (ObserverInfoList) key2ObserverInfos.get(key);

		if(obs!=null) for (Iterator<ObserverInfo> iter = obs.iterator(); iter.hasNext();) {
			ObserverInfo observer = iter.next();

			observer.notifyChange(KVOChangeKind,indexes);
			observer.invalidateSnapshot();
		}


		//forwarders
		if(forwards.containsKey(key))
		{
			for (Iterator<String> iter = forwards.get(key).keySet().iterator(); iter.hasNext();) {
				String tail = iter.next();
				for(Iterator<ObserverInfo> obsIterator = getForwardersForKeyPath(key, tail, false).iterator();  obsIterator.hasNext();)
				{
					ObserverInfo obsInfo = obsIterator.next();

					obsInfo.notifyChange(KVOChangeKind,indexes);
					obsInfo.invalidateSnapshot();
				}
			}
		}

		//end
	}
	
	public void willChangeValuesAtIndexesForKey(int KVOChangeKind, IndexSet indexes, String key) {
		
		if(indexes.isEmpty())
			return;
		
		if(!hasObserversForKey(key))
			return;
		
		_willChangeValuesAtIndexesForKey(KVOChangeKind,indexes,key);
	}

	private void _willChangeValuesAtIndexesForKey(int KVOChangeKind, IndexSet indexes, String key) {
		
		
		
		logger.debug("Will change indexes for Key " + key + " indexes=" + indexes);

		boolean storeOldValues = (KVOChangeKind == KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL
				|| KVOChangeKind == KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT) /*&& storeOldValueForKey(key)*/;

		

		//forwarders
		if(forwards.containsKey(key))
		{
			for (Iterator<String> iter = forwards.get(key).keySet().iterator(); iter.hasNext();) {
				String keypath = iter.next();

				Object sharedSnapshot = KeyValueObservingUtils.INVALID_SNAPSHOT;
				for(Iterator<ObserverInfo> obs = getForwardersForKeyPath(key, keypath,false).iterator(); obs.hasNext();) {
					ObserverInfo ifo = obs.next();
					
					boolean postPriorEvent = ifo.expectsPriorEvent();
					if(!storeOldValues && !postPriorEvent)
						continue;
					if(storeOldValues) {
						if(sharedSnapshot == KeyValueObservingUtils.INVALID_SNAPSHOT) {
							sharedSnapshot = ifo.makeSnapshot(indexes);
						}
						else {
							ifo.setSnapshot(sharedSnapshot);
						}
					}
					
					if(postPriorEvent) {
						//(NSArray) (((options & KVOOption.KeyValueObservingOptionOld)!=0 && hasValidSnapshot()) ? snapshot:null), (options & KVOOption.KeyValueObservingOptionNew)!=0 ? newVal : null
						KVOIndexedChangeEvent evt = new KVOIndexedChangeEvent(KVOChangeKind, indexes);
						if(ifo.hasValidSnapshot() && ifo.requiresOldValue())
							evt.setOldValue((List<?>) ifo.getSnapshot());
						evt.setPriorEvent(true);
						ifo.sendEvent(evt);
					}
				}
			}
		}//forwarders ende
		
		
		ObserverInfoList coll = (ObserverInfoList) key2ObserverInfos.get(key);
//		Object sharedSnapshot = INVALID_SNAPSHOT;
		if(coll!=null) for(Iterator<ObserverInfo> obs = coll.iterator(); obs.hasNext();) {
			ObserverInfo ifo = obs.next();
			
			boolean postPriorEvent = (ifo.options & KVOOption.KeyValueObservingOptionPrior)!=0;
			if(!storeOldValues && !postPriorEvent)
				continue;
			if(storeOldValues) {
//				if(sharedSnapshot == INVALID_SNAPSHOT) {
					Object s = ifo.makeSnapshot(indexes);
//					if(s!=INVALID_SNAPSHOT)
//						sharedSnapshot = s;
//				}
//				else {
//					ifo.setSnapshot(sharedSnapshot);
//				}
			}
			
			if(postPriorEvent) {
				//(NSArray) (((options & KVOOption.KeyValueObservingOptionOld)!=0 && hasValidSnapshot()) ? snapshot:null), (options & KVOOption.KeyValueObservingOptionNew)!=0 ? newVal : null
				KVOIndexedChangeEvent evt = new KVOIndexedChangeEvent(KVOChangeKind, indexes);
				if(ifo.hasValidSnapshot() && ifo.requiresOldValue())
					evt.setOldValue((List<?>)ifo.getSnapshot());
				evt.setPriorEvent(true);
				ifo.sendEvent(evt);
			}
		}
	
		
		
		
		
	}

	/**
	 * This is not part of the public KeyValueObserving-API, but useful for testing.
	 * Someday it will be hidden.
	 * @return
	 */
	public boolean hasObservers() {
		return ! key2ObserverInfos.isEmpty()
			|| ! forwards.isEmpty()
		;
	}
	
	private boolean hasObserversForKey(String key) {
		return hasObserversForKey(key,true);
	}
	
	private boolean hasObserversForKey(String key, boolean considerForwards) {
		Collection<ObserverInfo> obs = key2ObserverInfos.get(key);
		boolean a = (obs!=null && ! obs.isEmpty());
		if(considerForwards) {
			boolean b = !(forwards.get(key)==null || forwards.get(key).isEmpty());
			
			return a || b;
		}
		return a;
	}
	

	private void pushUpdateForProperty(String key){
		notifyObserversAboutSettingEvent(key, false);
	}
	
	private void pushIndexedUpdateForProperty(int KVOChangeKind, IndexSet indexes,String key){
		notifyObserversAboutIndexedEvent(KVOChangeKind, indexes, key);
	}

	public void removeObserver(KVObserver observer, String keyPath) {
		int dotPos = keyPath.indexOf('.');
		if(dotPos==-1){
			removeObserverForProperty(observer, keyPath);
			return;
		}
		
		String head = keyPath.substring(0, dotPos); 
		String tail = keyPath.substring(dotPos+1,keyPath.length());


		
		Object o = null;
		try{
			o = kvc.getValueForKey(head);
		}
		catch(NonCompliantAccessorFoundException e)
		{
			throw new KVOException(e);
		}
//		if(o==null)
//			throw new IllegalStateException();

		if(!(o instanceof KeyValueObserving))
		{
//			throw new KVOException();
//			System.out.println();
			logger.debug("Cannot remove forwarding Observer '" + head + "." + tail + " ' from Object [" + this + "]:");
		}
		else {
//			((KeyValueObserving)o).removeObserver(backwardPropagator, tail);
		}

		
		//remove forwarders
		Collection<ObserverInfo> c = getForwardersForKeyPath(head, tail, false);
		if(c!=null){
			for(Iterator<ObserverInfo> it = c.iterator(); it.hasNext();) {
				ObserverInfo obsInfo = it.next();
				if(obsInfo.observer == observer){
					if(o instanceof KeyValueObserving){
						((KeyValueObserving)o).removeObserver(obsInfo.bp, tail);
						obsInfo.bp = null;
					}
					
					it.remove();
				}
			}
		}
		_cleanForwards(head,tail);
		//
		
	}
	
	private static Object unitializedVal = new Object() { @Override
	public String toString(){ return "KVO:unitializedVal"; } };
	
	public void willChangeValueForKey(String key) {

		if(!hasObserversForKey(key))
			return;

		
		_willChangeValueForKey(key);
	}
	
	private void _remove_forwarder_and_makeForwardSnapshots(String key,boolean makeSnapshot) {
		
		//forwarders
		if(forwards.containsKey(key))
		{
			Object valueAboutToChange = kvc.getValueForKey(key);
			
			for (Iterator<String> iter = forwards.get(key).keySet().iterator(); iter.hasNext();) {
				String keypath = iter.next();
				
				if(valueAboutToChange instanceof KeyValueObserving) {
					try {
						logger.debug("removing forwarding Observer '" + keypath + "' from Object [" + valueAboutToChange + "]");
					
						//TODO verschmelze mit unterer Iteration:
						ObserverInfoList obil = getForwardersForKeyPath(key, keypath,false);
						for(Iterator<ObserverInfo> obs = obil.iterator(); obs.hasNext();) {
							ObserverInfo ifo = obs.next();
							((KeyValueObserving)valueAboutToChange).removeObserver(ifo.bp, keypath);
						}
						
					} catch(KVOException e)	{
						logger.debug("Cannot remove forwarding Observer '" + keypath + "' from Object [" + valueAboutToChange + "]:" + e.getMessage());
					}
				}
				
				if(makeSnapshot) {
					Object sharedSnapshot = KeyValueObservingUtils.INVALID_SNAPSHOT;
					ObserverInfoList obil = getForwardersForKeyPath(key, keypath,false);
					for(Iterator<ObserverInfo> obs = obil.iterator(); obs.hasNext();) {
						ObserverInfo ifo = obs.next();
						if(sharedSnapshot == KeyValueObservingUtils.INVALID_SNAPSHOT) {
							Object s = ifo.makeSnapshot();
							if(s!=KeyValueObservingUtils.INVALID_SNAPSHOT)
								sharedSnapshot = s;
						}
						else {
							ifo.setSnapshot(sharedSnapshot);
						}
						//neu
						if(ifo.expectsPriorEvent()){
							KVOSettingEvent evt = new KVOSettingEvent();
							if(ifo.hasValidSnapshot() && ifo.requiresOldValue())
								evt.setOldValue(ifo.getSnapshot());
							
							evt.setPriorEvent(true);
							ifo.sendEvent(evt);
						}
					}
				}
			}
		}//forwarders ende
	}
	
	private void _willChangeValueForKey(String key) {
		
		_remove_forwarder_and_makeForwardSnapshots(key,true);
			
//		Object sharedSnapshot = INVALID_SNAPSHOT;
		ObserverInfoList coll = (ObserverInfoList) key2ObserverInfos.get(key);
		if(coll!=null) for(Iterator<ObserverInfo> obs = coll.iterator(); obs.hasNext();) {
			ObserverInfo ifo = obs.next();
//			if(sharedSnapshot == INVALID_SNAPSHOT) {
				Object s = ifo.makeSnapshot();
//				if(s!=INVALID_SNAPSHOT)
//					sharedSnapshot = s;
//			}
//			else {
//				ifo.setSnapshot(sharedSnapshot);
//			}
			
			if(ifo.expectsPriorEvent()){
				KVOSettingEvent evt = new KVOSettingEvent();
				if(ifo.hasValidSnapshot() && ifo.requiresOldValue())
					evt.setOldValue(ifo.getSnapshot());
				
				evt.setPriorEvent(true);
				ifo.sendEvent(evt);
			}
		}
		
	}
}
