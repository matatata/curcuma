package de.ceruti.curcuma.keyvaluebinding;

import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;

public class SimplePropertyBinder<T> extends AbstractScalarValueBinder {

	private T value;
	
	@Override
	protected T getBindingValue() {
		return value;
	}

	@Override
	protected void setBindingValue(Object newValue) throws KeyValueBindingSyncException {
		this.value=(T)newValue;
	}

	@Override
	protected Object validateBinding(Object newValue)
			throws ValidationException, KeyValueBindingSyncException  {
		try {
			return newValue;
		} catch (ClassCastException e) {
			throw new ValidationException(e);
		}
	}

	@Override
	protected Object validateSubject(Object newValue)
			throws ValidationException, KeyValueBindingSyncException {
		try {
			return newValue;
		} catch (ClassCastException e) {
			throw new ValidationException(e);
		}
	}

}
