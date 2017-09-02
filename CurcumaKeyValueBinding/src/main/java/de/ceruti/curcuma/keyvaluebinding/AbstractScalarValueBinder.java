package de.ceruti.curcuma.keyvaluebinding;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.core.ValueTransformer;
import de.ceruti.curcuma.api.core.exceptions.ConversionException;
import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingException;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.KeyValueCodingException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.keyvalueobserving.KVOSettingEvent;

/**
 * 
 * @author cerutim
 *
 */
public abstract class AbstractScalarValueBinder implements KeyValueBinder, KVObserver {

	private static Category logger = Logger.getInstance(AbstractScalarValueBinder.class);
	
	protected BindingInfoImpl info;
	protected KeyValueBindingCreation bindingCreation;
	
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.KeyValueBinder#syncBinding(de.ceruti.curcuma.api.keyvalueobserving.KVOEvent)
	 */
	@Override
	public void syncBinding(KVOEvent change) throws KeyValueBindingSyncException {
		
		String binding = info.getName();
		
		if(!(info.getObservedObject() instanceof KeyValueCoding))
			throw new KeyValueBindingSyncException("Observed Object is not an instanceof " + KeyValueCoding.class.getSimpleName() + " " + info);
		
		if(logger.isDebugEnabled())
			logger.debug(this.getClass().getSimpleName() + " synchronizing binding " + binding + "(" + info + ") evt=" + change);
		
		
		switch(change.getKind())
		{
			case KVOEvent.KEY_VALUE_CHANGE_SETTING:
			{
				Object value = null;
				
				if(change.hasNewValue())
					value = change.getNewValue();
				else
					value = getSubjectValue();
						
				syncBinding(value);
			} break;
			
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
			{	
				//cannot use change.getNewValue()
				Object value = getSubjectValue();

				syncBinding(value);
			
			}break;
			

		}

	}
	
	
	@Override
	public final void syncSubject(KVOEvent change) throws KeyValueBindingSyncException {

		if(!(info.getObservedObject() instanceof KeyValueCoding))
			throw new KeyValueBindingSyncException("observed object not an instance of KeyValueCoding");
		KeyValueCoding observedObject = (KeyValueCoding) info.getObservedObject();
		
		if(logger.isDebugEnabled())
			logger.debug(this.getClass().getSimpleName() + " synchronizing Observable " + info.getObservedObject() + "(" + info + ") evt=" + change);
		
		switch(change.getKind())
		{
			case KVOEvent.KEY_VALUE_CHANGE_SETTING:
			{
				Object value = null;
				
				if(change.hasNewValue()) {
					value = change.getNewValue();
				}
				else
					value = getBindingValue(); //fetch value

				syncSubject(value);
			} break;
			
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
			{	
				//cannot use change.getNewValue()
				Object value = observedObject.getValueForKeyPath(info.getObservedKeyPath());
				syncSubject(value);
			}break;
		}
	}



	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.KeyValueBinder#allowsReverseSynchronisation()
	 */
	@Override
	public boolean allowsReverseSynchronisation() {
		return true;
	}
	

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.KeyValueBinder#getBindingInfo()
	 */
	@Override
	public final BindingInfo getBindingInfo() {
		return info;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.KeyValueBinder#bind(java.lang.String, java.lang.Object, java.lang.String, de.ceruti.curcuma.api.keyvaluebinding.BindingOptions)
	 */
	@Override
	public BindingInfo bind(KeyValueBindingCreation creation,String binding,Object subject,String subjectKeyPath, BindingOptions options) {
		BindingInfoImpl bindInfo = new BindingInfoImpl(binding,subject,subjectKeyPath,options);
		
		Object initialValue = ((KeyValueCoding) subject).getValueForKeyPath(subjectKeyPath);
		info=bindInfo;
		bindingCreation = creation;
		startObservingSubject();
		try {
			syncBinding(new KVOSettingEvent(null,initialValue));
		} catch(KeyValueBindingSyncException e) {
			logger.error(e);
			unbind();
			throw new KeyValueBindingException(e);
		}
		return bindInfo;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.KeyValueBinder#unbind()
	 */
	@Override
	public void unbind() {
		if(info!=null)
			stopObservingSubject(info);
		info = null;
		bindingCreation = null;
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.keyvalueobserving.KVObserver#observeValue(java.lang.String, de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving, de.ceruti.curcuma.api.keyvalueobserving.KVOEvent, java.lang.Object)
	 */
	@Override
	public final void observeValue(String keypath, KeyValueObserving object, KVOEvent change, Object context) {
		if(isUpdatingSubject()){
			logger.debug("ignoring " + change + " because subjects is beeing updated.");
			return;
		}
		
		try {
			
			if(change.hasNewValue() && change.hasOldValue())
			{
				if(ObjectUtils.equals(change.getOldValue(), change.getNewValue())){
					logger.debug("ignoring " + change + " because ols and new value are equal.");
					return;
				}
			}
			
			syncBinding(change);
		} catch (KeyValueBindingSyncException e) {
			throw new KeyValueBindingException(e);
		}
	}
	
	protected boolean isUpdatingSubject() {
		logger.debug("nestingLevel=" +  info.nestingLevelUpdatingSubject() + " " + info);
		return info.nestingLevelUpdatingSubject() > 0;
	}
	
	protected final int subjectObservingOptions() {
		return KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /* KVOOption.KeyValueObservingOptionNew */;
	}
	
	private void startObservingSubject() {
		((KeyValueObserving)info.getObservedObject()).addObserver(this, info.getObservedKeyPath(), info,
				subjectObservingOptions());
	}
	
	private void stopObservingSubject(BindingInfo info) {
		((KeyValueObserving)info.getObservedObject()).removeObserver(this, info.getObservedKeyPath());

	}
	
	protected void enterUpdatingSubject() {
		info.enterUpdatingSubject();
	}

	protected void exitUpdatingSubject() {
		info.exitUpdatingSubject();
	}
	
	/**
	 * Prepare a value for beeing used to update the observed subject's value. If there is a ValueTransformer specified, it will be used.
	 * If the AutoValidate-BindingOption is set, then the value will be validated.
	 * @param bindingsValue the value that should be prepared and validated in order to be set as the observed subject's new value
	 * @return
	 * @throws ValidationException
	 * @throws KeyValueBindingSyncException 
	 */
	private Object transformAndValidateSubject(Object bindingsValue) throws ValidationException, KeyValueBindingSyncException
	{
		BindingOptions opts = info.getOptions();
		ValueTransformer tr = opts!=null?opts.getValueTransformer():null;
		if(tr!=null && tr.allowsReverseTransformation())
			try {
				bindingsValue = tr.reverseTransformedValue(bindingsValue);
			} catch (ConversionException e) {
				throw new KeyValueBindingSyncException(e);
			}
		
		if(opts!=null && opts.isAutomaticValidation()){
			// validate
			bindingsValue = validateSubject(bindingsValue);
		}
		
		return bindingsValue;
	}
	

	
	/**
	 * update the exposed binding's value
	 * @param newValue
	 */
	protected abstract void setBindingValue(Object newValue) throws KeyValueBindingSyncException;
	
	/**
	 * return the exposed binding's current value
	 * @return
	 */
	protected abstract Object getBindingValue() throws KeyValueBindingSyncException;
	
	/**
	 * validate newValue so it the exposed binding's value can be updated safely 
	 * @param newValue
	 * @return
	 * @throws ValidationException
	 */
	protected abstract Object validateBinding(Object newValue) throws ValidationException, KeyValueBindingSyncException;

	/**
	 * validate newValue so it the observed subject's value can be updated safely.
	 * You only need to implemnent this if reverse-sync is intended.
	 * @param newValue
	 * @return
	 * @throws ValidationException
	 * @see {@link #allowsReverseSynchronisation()}
	 */
	protected Object validateSubject(Object newValue) throws ValidationException, KeyValueBindingSyncException {
		return newValue;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.KeyValueBinder#validateSubjectValue(java.lang.Object)
	 */
	@Override
	public final Object validateSubjectValue(Object newValue) throws ValidationException, KeyValueBindingSyncException {
		return transformAndValidateSubject(newValue);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.KeyValueBinder#validateBindingValue(java.lang.Object)
	 */
	@Override
	public final Object validateBindingValue(Object newValue) throws ValidationException, KeyValueBindingSyncException {
		return transformAndValidateBinding(newValue);
	}
	
	/**
	 * return the observed subject's current value
	 * @return
	 */
	protected Object getSubjectValue() {
		return ((KeyValueCoding)info.getObservedObject()).getValueForKeyPath(info.getObservedKeyPath());
	}

	
	@Override
	public final void updateBinding() throws KeyValueBindingSyncException {
		setBindingValue(getSubjectValue());
	}
	
	/**
	 * update the observed subject's value
	 * Must post KVO-Notifications
	 * @param newValue
	 */
	protected final void setSubjectValue(Object newValue) throws KeyValueBindingSyncException {
		Object observedObject = info.getObservedObject();
		if(!(observedObject instanceof KeyValueCoding))
			throw new KeyValueBindingSyncException("Observed Object is not an instanceof " + KeyValueCoding.class.getSimpleName() + " " + info);
	
		try {
			enterUpdatingSubject();
	
			((KeyValueCoding)observedObject).setValueForKeyPath(newValue,info.getObservedKeyPath());
		
		} catch(KeyValueCodingException kvoE) {
			throw new KeyValueBindingSyncException(kvoE);
		}
		finally {
			exitUpdatingSubject();
		}
	}
	

	/**
	 * calls {@link #setSubjectVainlue(Object)}
	 * @param newValue
	 */
	protected final void syncSubject(Object bindingsValue) throws KeyValueBindingSyncException {
		try {
			Object original = bindingsValue;
			bindingsValue = transformAndValidateSubject(bindingsValue);
			
			//experimental
			//Update Binding if value changed:
			if(!ObjectUtils.equals(original,bindingsValue)) {
				logger.debug("Updating Binding after validation with Observable");
				syncBinding(bindingsValue);
			}

			setSubjectValue(bindingsValue);
		}
		catch(ValidationException e){
			logger.warn("Could not sync Observable: " + e.getMessage());
			throw new KeyValueBindingSyncException(e);
		}
	}

	/**
	 * calls {@link #setBindingValue(Object)}
	 * @param newValue
	 */
	protected final void syncBinding(Object subjectsValue) throws KeyValueBindingSyncException {
		try {
			subjectsValue = transformAndValidateBinding(subjectsValue);
			setBindingValue(subjectsValue);
		}
		catch(ValidationException e){
			logger.debug("Could not sync Binding",e);
			throw new KeyValueBindingSyncException(e);
		}
	}
	
	/**
	 * Prepare a value for beeing used to update the binding's value. If there is a ValueTransformer specified, it will be used.
	 * If the AutoValidate-BindingOption is set, then the value will be validated.
	 * @param subjectsValue the value that should be prepared and validated in order to be set as the binding's new value
	 * @return
	 * @throws ValidationException
	 * @throws KeyValueBindingSyncException 
	 */
	private Object transformAndValidateBinding(Object subjectsValue) throws ValidationException, KeyValueBindingSyncException
	{
		BindingOptions opts = info.getOptions();
		ValueTransformer tr = opts!=null ? opts.getValueTransformer() : null;
		if(tr!=null)
			try {
				subjectsValue = tr.transformedValue(subjectsValue);
			} catch (ConversionException e) {
				throw new KeyValueBindingSyncException(e);
			}
		
		if(opts!=null && opts.isAutomaticValidation()){
			// validate
			subjectsValue = validateBinding(subjectsValue);
		}
		
		return subjectsValue;
	}
}
