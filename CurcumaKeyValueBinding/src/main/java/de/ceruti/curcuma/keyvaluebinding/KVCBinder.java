package de.ceruti.curcuma.keyvaluebinding;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.KeyValueCodingException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;


/**
 * This works if the exposed binding is (or can be mapped to) a KVC-compliant property
 * @author cerutim
 *
 */
public class KVCBinder extends AbstractScalarValueBinder {
	
	private KeyValueCoding kvc;
	
	public KVCBinder(KeyValueCoding kvc) {
		this.kvc = kvc;
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.AbstractScalarValueBinder#setBindingValue(java.lang.Object)
	 */
	@Override
	protected void setBindingValue(Object newValue) throws KeyValueBindingSyncException {
		try {
			kvc.setValueForKey(newValue, info.getName());
		} catch (KeyValueCodingException e) {
			throw new KeyValueBindingSyncException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.AbstractScalarValueBinder#validateBinding(java.lang.Object)
	 */
	@Override
	protected Object validateBinding(Object newValue) throws ValidationException, KeyValueBindingSyncException {
		try {
			return kvc.validateValueForKey(newValue, info.getName());
		} catch (KeyValueCodingException e) {
			throw new KeyValueBindingSyncException(e);
		}
	}
	
	/**
	 * validate newValue so it the observed subject's value can be updated
	 * safely
	 * 
	 * @param newValue
	 * @return
	 * @throws ValidationException
	 */
	@Override
	protected Object validateSubject(Object newValue)
			throws ValidationException, KeyValueBindingSyncException {
		try {
			return ((KeyValueCoding) info.getObservedObject())
					.validateValueForKeyPath(newValue, info
							.getObservedKeyPath());
		} catch (KeyValueCodingException e) {
			throw new KeyValueBindingSyncException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.keyvaluebinding.AbstractScalarValueBinder#getBindingValue()
	 */
	@Override
	protected Object getBindingValue() throws KeyValueBindingSyncException {
		return kvc.getValueForKey(info.getName());
	}
	
}
