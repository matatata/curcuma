package de.ceruti.curcuma.api.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Selector {

	boolean isRequirePublic();

	void setRequirePublic(boolean requirePublic);

	Class<?> getReturnType();

	void setReturnType(Class<?> returnType);

	/**
	 * More priority than {@link #getRequiredArgCount()}
	 * @return
	 */
	Class<?>[] getParameterTypes();

	void setParameterTypes(Class<?>[] parameterTypes);

	Object invoke(Object object, Object[] arguments)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException;

	Object unsafeInvoke(Object object, Object[] arguments)
			throws NoSuchMethodException;

	/**
	 *
	 * @param object
	 * @return
	 */
	Method findMethod(Class<?> clazz);

	String getMethodName();

	void setMethodName(String methodName);

	/**
	 * 
	 * @return null, if there are no requirements
	 */
	Integer getRequiredArgCount();

	/**
	 * 
	 * @param argCount null, if there are no requirements
	 */
	void setRequiredArgCount(Integer argCount);

}