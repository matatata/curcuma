package de.ceruti.curcuma.api.core.utils;

import java.lang.reflect.InvocationTargetException;


public interface Invocation {

//	public abstract Object[] getArgs();
//
//	public abstract Selector getSelector();
//
//	public abstract Object getTarget();

	//	public void invokeNo() {
	//		try {
	//			getSelector().invoke(getTarget(), getArgs());
	//		} catch (IllegalArgumentException e) {
	//			NSForwardException.runtimeExceptionForThrowable(e);
	//		} catch (IllegalAccessException e) {
	//			NSForwardException.runtimeExceptionForThrowable(e);
	//		} catch (InvocationTargetException e) {
	//			NSForwardException.runtimeExceptionForThrowable(e);
	//		} catch (NoSuchMethodException e) {
	//			NSForwardException.runtimeExceptionForThrowable(e);
	//		}
	//	}
	//
	
	public abstract void invoke() throws InvocationTargetException;

}