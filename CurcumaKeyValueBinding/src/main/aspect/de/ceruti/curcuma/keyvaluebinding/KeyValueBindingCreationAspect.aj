package de.ceruti.curcuma.keyvaluebinding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptionDescription;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingException;
import de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.core.utils.MapMap;
import de.ceruti.curcuma.keyvalueobserving.KVOSettingEvent;

public aspect KeyValueBindingCreationAspect {
	private static Category logger = Logger.getInstance(KeyValueBindingCreationAspect.class);
	
	private interface KeyValueBindingCreationSupport extends KeyValueBindingCreation, KeyValueCoding, ErrorHandling {
	}
	
	declare parents: @KeyValueBindingCreator * implements KeyValueBindingCreationSupport;
	
	
	
	private Map<String,KeyValueBinder> KeyValueBindingCreationSupport.binderMap = new HashMap<String,KeyValueBinder>();
	
	private Map<String, Class<?>> KeyValueBindingCreationSupport.exposedBindings = new HashMap<String, Class<?>>();
	
	private MapMap<String, String, BindingOptionDescription> KeyValueBindingCreationSupport.bindingOptionDescs = new MapMap<String, String, BindingOptionDescription>();
	
	
	public Set<String> KeyValueBindingCreationSupport.exposedBindings() {
		return exposedBindings.keySet();
	}
		
	/*FIXME should be protected, but then it cannot be found*/
	public void KeyValueBindingCreationSupport.exposeBinding(String binding,Class<?> valueClazz) {
		exposedBindings.put(binding,valueClazz);
	}
	
	/*FIXME should be protected, but then it cannot be found*/
	public void KeyValueBindingCreationSupport.unexposeBinding(String binding) {
		exposedBindings.remove(binding);
	}
	
	public Set<String> KeyValueBindingCreationSupport.getBindingOptionKeysForBinding(String binding){
		if(!bindingOptionDescs.containsKey(binding))
			return Collections.emptySet();
		
		return bindingOptionDescs.get(binding).keySet();
	}
	
	public BindingOptionDescription KeyValueBindingCreationSupport.getBindingOptionDescription(String binding,String optionKey){
		if(!bindingOptionDescs.containsKey(binding))
			return null;
		return bindingOptionDescs.get(binding).get(optionKey);
	}
	
	
	public void KeyValueBindingCreationSupport.addBindingOptionDescriptionForBinding(String binding, BindingOptionDescription desc) {
		bindingOptionDescs.put(binding, desc.getKey(), desc);
	}
	
	public void KeyValueBindingCreationSupport.removeBindingOptionDescriptionForBinding(String binding,String key) {
		bindingOptionDescs.remove(binding, key);
	}
	
	public KeyValueBinder KeyValueBindingCreationSupport.createBinderForBinding(String binding, Object observedObject) {
		return new KVCBinder(this);
	}
	
	public void KeyValueBindingCreationSupport.bind(String binding, Object observableController, String withKeyPath,
			BindingOptions options) {
		
		if(!exposedBindings().contains(binding)) {
			logger.warn(this.getClass() + " does not expose binding " + binding);
			throw new KeyValueBindingException(this.getClass() + " does not expose binding " + binding);
		}
		
		if(isBound(binding))
			unbind(binding);
		
		if(options == null)
			options = new DefaultBindingOptions();

		try {
			KeyValueBinder binder = createBinderForBinding(binding,observableController);
			putBinderForBinding(binder,binding);
			BindingInfo bindInfo = binder.bind(this,binding,observableController,withKeyPath, options);
			if(bindInfo==null)
			{
				removeBinderForBinding(binding);
			}
		} catch(KeyValueBindingException e) {
			logger.error(e);
			unbind(binding);
			throw e;
		}
	}
	
	public BindingInfo KeyValueBindingCreationSupport.getInfoForBinding(String binding) {
		
		KeyValueBinder binder = getBinderForBinding(binding);
		if(binder!=null)
			return binder.getBindingInfo();
			
		return null;
	}
	
	void KeyValueBindingCreationSupport.putBinderForBinding(KeyValueBinder binder,String binding) {
		binderMap.put(binding,binder);
	}
	
	void KeyValueBindingCreationSupport.removeBinderForBinding(String binding) {
		binderMap.remove(binding);
	}
	
	public void KeyValueBindingCreationSupport.unbind(String binding) {
		KeyValueBinder binder = getBinderForBinding(binding);
		if(binder!=null)
			binder.unbind();
		
		removeBinderForBinding(binding);
	}

	
	public boolean KeyValueBindingCreationSupport.isBound(String binding) {
		return getBinderForBinding(binding) != null;
	}
		
	private KeyValueBinder KeyValueBindingCreationSupport.getBinderForBinding(String binding) {
		return binderMap.get(binding);
	}


	/**
	 * Does niothing if not bound or binding does not support reverse syncing.
	 * @param binding
	 * @throws KeyValueBindingSyncException
	 */
	public void KeyValueBindingCreationSupport.updateObservableBoundToBinding(String binding) throws KeyValueBindingSyncException {
		_updateObse(binding);
	
	}
	
	private void KeyValueBindingCreationSupport._updateObse(String binding) throws KeyValueBindingSyncException{
		KeyValueBinder binder = getBinderForBinding(binding);
		if(binder==null)
			return;
		
		if(!binder.allowsReverseSynchronisation())
			return;
		
		binder.syncSubject(KVOSettingEvent.emptySettingEvent);
	}
	
	public void KeyValueBindingCreationSupport.updateBinding(String binding) throws KeyValueBindingSyncException {
		KeyValueBinder b = getBinderForBinding(binding);
		if (b != null) {
			b.updateBinding();
		}
	}
	
	/**
	 * Validate newValue so it the observed subject's value can be updated
	 * safely. You only need to implemnent this if reverse-sync is intended.
	 * 
	 * @param subjectValue
	 * @return subjectValue possibly another object supposed to be used as
	 *         subjects new value. If not bound or the binding does not support
	 *         reverse syncing returns <code>newValue</code>.
	 * @throws ValidationException
	 * @throws KeyValueBindingSyncException
	 */
	public Object KeyValueBindingCreationSupport.validateSubject(Object subjectValue, String binding) throws ValidationException, KeyValueBindingSyncException {
		KeyValueBinder b = getBinderForBinding(binding);
		if (b != null && b.allowsReverseSynchronisation()) {
			return b.validateSubjectValue(subjectValue);
		}
		
		return subjectValue;
	}
	
	/**
	 * Validate a value so it can be be safely used as the binding's new value.
	 * @param newBindValue
	 * @return newBindValue if not bound or possibly another object supposed to be used as binding's new value 
	 * @throws ValidationException
	 * @throws KeyValueBindingSyncException
	 */
	public Object KeyValueBindingCreationSupport.validateBinding(Object newBindValue, String binding) throws ValidationException, KeyValueBindingSyncException {
		KeyValueBinder b = getBinderForBinding(binding);
		if (b != null) {
			return b.validateBindingValue(newBindValue);
		}
		
		return newBindValue;
	}
}
