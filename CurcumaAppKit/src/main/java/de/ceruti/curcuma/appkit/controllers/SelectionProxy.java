package de.ceruti.curcuma.appkit.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSSelectionMarker;
import de.ceruti.curcuma.api.appkit.controller.NSArrayController;
import de.ceruti.curcuma.api.appkit.controller.NSObjectController;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOException;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.core.Factory;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.core.utils.CollectionMap;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodingUtils;
import de.ceruti.curcuma.keyvalueobserving.KVOIndexedChangeEvent;
import de.ceruti.curcuma.keyvalueobserving.KVOSettingEvent;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservable;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservingUtils;

@KeyValueCodeable
@KeyValueObservable
class SelectionProxy implements ErrorHandling, KVObserver {
	private static Logger logger = LogManager.getLogger(SelectionProxy.class);
	private NSObjectController controller;
	private Map<String, Object> cachedValues = new HashMap<String, Object>();
	private Collection<Object> observableSelection = new ArrayList<Object>();
	private Set<String> cachedKeys;

	SelectionProxy(NSObjectController c) {
		this.controller = c;
		observableSelection.addAll(controller.getSelectedObjects());
	}

	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy((KeyValueCoding) this, key);
	}

	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element, String key, boolean isWrite) {
		return KeyValueCodingUtils.createCompliantObjectForKey(this, element, key, isWrite);
	}

	public void setValueForKey(Object value, Class<?> type, String key) {
		setValueForUndefinedKey(value, type, key);
	}

	private boolean settingValue = false;

	@Override
	public void setValueForUndefinedKey(Object value, Class<?> type, String key) {
		List<?> l = controller.getSelectedObjects();
		if (l.isEmpty())
			return;
		Object last = l.get(l.size() - 1);
		List<?> subL = l.subList(0, l.size() - 1);
		settingValue = true;
		KeyValueCodingUtils.setValueForUndefinedKey(subL, value, type, key);
		settingValue = false;

		if (last instanceof KeyValueCoding)
			((KeyValueCoding) last).setValueForKey(value, type, key);

	}

	public Object getValueForKey(String key) {
		return getValueForUndefinedKey(key);
	}

	@Override
	public Object getValueForUndefinedKey(String key) {
		if (cachedValues.containsKey(key)) {
			Object val = cachedValues.get(key);
			logger.debug("got cached selection." + key + "=" + val);
			return val;
		}

		List<?> selectedObjects = controller.getSelectedObjects();

		List<?> allVals = null;
		if (selectedObjects instanceof KeyValueCoding)
			allVals = (List<?>) ((KeyValueCoding) selectedObjects).getValueForKey(key);
		else
			allVals = (List<?>) KeyValueCodingUtils.getValueForUndefinedKey(selectedObjects, key);

		Object val = null;

		if (allVals.size() == 0) {
			val = NSSelectionMarker.NSNoSelectionMarker;
		} else if (allVals.size() == 1) {
			val = allVals.get(0);
		} else {
			if (!((NSArrayController) controller).isAlwaysUsesMultipleValuesMarker()) {
				// first
				Object first = allVals.get(0);
				boolean heterogen = false;
				for (int i = 1; i < allVals.size(); i++) {
					Object test = allVals.get(i);
					if (!ObjectUtils.equals(first, test)) {
						heterogen = true;
						break;
					}
				}
				if (!heterogen)
					val = first;
				else
					val = NSSelectionMarker.NSMultipleValuesMarker;
			} else
				val = NSSelectionMarker.NSMultipleValuesMarker;
		}

		if (val == null)
			val = NSSelectionMarker.NSNullValueMarker;

		cachedValues.put(key, val);

		logger.debug("got selection." + key + "=" + val);

		return val;

	}

	public void controllerDidChangeSelection() {
		logger.info("controllerDidChangeSelection()");
		// If your're getting NullPointerException the changes are nested...

		if (cachedKeys == null) {
			logger.error("controllerWillChangeSelection() was not called!");
			return;
		}

		for (String k : cachedKeys) {
			didChangeValueForKey(k);
		}
		cachedKeys.clear();
		cachedKeys = null;
	}

	public void controllerWillChangeSelection() {
		logger.info("controllerWillChangeSelection()");
		cachedKeys = cachedValues.keySet();
		for (String k : cachedKeys) {
			willChangeValueForKey(k);
		}
		cachedValues.clear();
	}

	@Override
	public void setNullValueForKey(String key) {
		KeyValueCodingUtils.setNullValueForKey(this, key);
	}

	public void addObserver(KVObserver observer, String keyPath, Object context, int options) throws KVOException {
		List<?> l = null;
		if (selectionChangeDelta != null && controller instanceof NSArrayController) {
			l = Utils.objectsAtIndexes(((NSArrayController) controller).getArrangedObjects(), selectionChangeDelta);
			logger.debug(
					"addObserver(" + keyPath + ") will only affect " + selectionChangeDelta + " observer=" + observer);
		} else {
			l = controller.getSelectedObjects();
			// TODO what if l contains x more than once ... !? Should create
			// union first:

			logger.debug("addObserver(" + keyPath + ") will affect whole selection observer=" + observer);
		}
		if (l.isEmpty())
			return;
		KeyValueObservingUtils.addObserverInRange(l, this, Factory.range(0, l.size()), keyPath, context,
				options /* KVOOption.KeyValueObservingOptionNone */);

		obsss.add(keyPath, observer);
	}

	private CollectionMap<String, KVObserver> obsss = new CollectionMap<String, KVObserver>();

	public void removeObserver(KVObserver observer, String keyPath) {
		List<?> l = null;
		if (selectionChangeDelta != null && controller instanceof NSArrayController) {
			l = Utils.objectsAtIndexes(((NSArrayController) controller).getArrangedObjects(), selectionChangeDelta);
			logger.debug("removeObserver(" + keyPath + ") will only affect " + selectionChangeDelta + " observer="
					+ observer);
		} else {
			l = controller.getSelectedObjects();
			logger.debug("removeObserver(" + keyPath + ") will affect whole selection observer=" + observer);
		}
		if (l.isEmpty())
			return;
		KeyValueObservingUtils.removeObserverInRange(l, this, Factory.range(0, l.size()), keyPath);

		obsss.remove(keyPath, observer);
	}

	@Override
	public void observeValue(String keyPath, KeyValueObserving objet, KVOEvent change, Object context) {
		if (settingValue) {
			return;
		}

		cachedValues.remove(keyPath);
		if (cachedKeys != null)
			cachedKeys.remove(keyPath);

		if (change.isIndexedEvent()) {
			KVOIndexedChangeEvent indexedEvent = new KVOIndexedChangeEvent(change);
			indexedEvent.invalidateNewValue();
			indexedEvent.invalidateOldValue();
			change = indexedEvent;
		} else {
			KVOSettingEvent settingEvent = new KVOSettingEvent(change);
			settingEvent.invalidateNewValue();
			settingEvent.invalidateOldValue();
			change = settingEvent;
		}

		for (KVObserver o : obsss.get(keyPath)) {

			o.observeValue(keyPath, objet, change, context);
		}
	}

	public Object validateValueForKey(Object value, String key) throws ValidationException {
		List<?> l = controller.getSelectedObjects();

		if (l.size() == 1)
			return ((KeyValueCoding) l.get(0)).validateValueForKey(value, key);

		KeyValueCodingUtils.validateValueForKey(l, value, key);

		return value;
	}

	private IndexSet selectionChangeDelta;

	void setSelectionChangeDelta(IndexSet delta) {
		this.selectionChangeDelta = delta;
	}

}
