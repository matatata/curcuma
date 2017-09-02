package de.ceruti.curcuma.api.core.exceptions;

import java.lang.reflect.InvocationTargetException;

public class NSForwardException extends RuntimeException {

	private NSForwardException(Throwable e) {
		super(e);
	}
	
	public static RuntimeException runtimeExceptionForThrowable(
			Throwable throwable) {
		if (throwable == null)
			return null;
		if (throwable instanceof RuntimeException)
			return ((RuntimeException) throwable);
		if (throwable instanceof InvocationTargetException) {
			Throwable targetException = ((InvocationTargetException) throwable)
					.getTargetException();
			if (targetException instanceof RuntimeException)
				return ((RuntimeException) targetException);

			return new NSForwardException(targetException);
		}
		return new NSForwardException(throwable);
	}

}
