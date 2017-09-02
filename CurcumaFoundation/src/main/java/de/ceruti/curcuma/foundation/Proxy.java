package de.ceruti.curcuma.foundation;

import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;

/**
 * This Observer observes the Array-Elements instead of the original Observer in
 * order to notify us
 * 
 */
abstract class Proxy implements KVObserver {
	KVObserver observer;
	String keyPath;
	boolean notifyObject = true;
	Object owner;
	Object context;
	int options;

	/**
	 * @param owner Who wants to be notified also
	 * @param obs
	 * @param keyPath
	 * @param context
	 */
	Proxy(Object owner, KVObserver obs, String keyPath, Object context, int options) {
//		if(!(owner instanceof KeyValueObserving))
//			throw new IllegalArgumentException("owner must be instance of KeyValueObserving");
//		if(!(owner instanceof KVObserver))
//			throw new IllegalArgumentException("owner must be instance of KVObserver");
//		
		this.observer = obs;
		this.keyPath = keyPath;
		this.owner = owner;
		this.context = context;
		this.options = options;
		
	}

//	/**
//	 * 
//	 * @param o
//	 * @return
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if ((obj == null) || (obj.getClass() != this.getClass()))
//			return false;
//
//		Proxy p = (Proxy) obj;
//
//		return (p.observer == observer
//				&& p.context == context
//				&& p.options == options
//				&& (p.keyPath == keyPath || (p.keyPath != null && p.keyPath
//						.equals(keyPath))) && p.owner == owner && p.notifyObject == notifyObject);
//	}
//
//	@Override
//	public int hashCode() {
//		StringBuffer b = new StringBuffer();
//		b.append(owner != null ? owner.hashCode() : "0");
//		b.append(keyPath != null ? keyPath.hashCode() : "0");
//		b.append(context != null ? context.hashCode() : "0");
//		b.append(observer != null ? observer.hashCode() : "0");
//		b.append(options);
//		return b.toString().hashCode();
//	}

//	public void observeValue(String keypath, KeyValueObserving object,
//			KVOEvent change, Object context) {
//
//		((KVObserver) owner)
//					.observeValue(keypath, object, change, context);
//	}

}