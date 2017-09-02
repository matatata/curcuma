package de.ceruti.curcuma.core.utils;

import java.lang.reflect.InvocationTargetException;

import de.ceruti.curcuma.api.core.exceptions.NSForwardException;
import de.ceruti.curcuma.api.core.utils.Invocation;
import de.ceruti.curcuma.api.core.utils.Selector;

public class InvocationImpl implements Invocation {
	private Selector selector;
	private Object target;
	private Object [] args;
	
	

	public InvocationImpl(Selector sel, Object target, Object [] args) {
		if(sel==null || target==null)
			throw new IllegalArgumentException("selector and target must not be null");
		this.selector = sel;
		this.target = target;
		this.args = args;
	}
	
	
	@Override
	public void invoke() throws InvocationTargetException {
		try {
			selector.invoke(target,args);
		} catch (IllegalArgumentException e) {
			NSForwardException.runtimeExceptionForThrowable(e);
		} catch (IllegalAccessException e) {
			NSForwardException.runtimeExceptionForThrowable(e);
		} catch (NoSuchMethodException e) {
			NSForwardException.runtimeExceptionForThrowable(e);
		}
	}
}
