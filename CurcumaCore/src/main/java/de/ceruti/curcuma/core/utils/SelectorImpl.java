/*
This file is part of Curcuma.

Copyright (c) Matteo Ceruti 2009

Curcuma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Curcuma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Curcuma.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.ceruti.curcuma.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import de.ceruti.curcuma.api.core.exceptions.NSForwardException;
import de.ceruti.curcuma.api.core.utils.Selector;

public class SelectorImpl implements Selector {

	private Class<?>[] parameterTypes;
	private Integer argCount;
	private String methodName;
	private Class<?> returnType;
	private boolean requirePublic;
	
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#isRequirePublic()
	 */
	@Override
	public boolean isRequirePublic() {
		return requirePublic;
	}
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#setRequirePublic(boolean)
	 */
	@Override
	public void setRequirePublic(boolean requirePublic) {
		this.requirePublic = requirePublic;
	}
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#getReturnType()
	 */
	@Override
	public Class<?> getReturnType() {
		return returnType;
	}
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#setReturnType(java.lang.Class)
	 */
	@Override
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#getParameterTypes()
	 */
	@Override
	public Class<?>[] getParameterTypes() {
		if(parameterTypes==null)
			return null;
		Class<?>[] ret = new Class<?>[parameterTypes.length];
		System.arraycopy(parameterTypes, 0, ret, 0, parameterTypes.length);
//		Class<?>[] ret = Arrays.copyOf(parameterTypes, 0);
		return ret;
	}
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#setParameterTypes(java.lang.Class)
	 */
	@Override
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	
	
//	@Override
//	public boolean equals(Object o) {
//		if(o==null)
//			return false;
//		if(o==this)
//			return true;
//		if(!(o instanceof Selector))
//			return false;
//		Selector s = (Selector)o;
//		if(!ObjectUtils.equals(argCount, s.getRequiredArgCount()))
//			return false;
//		if(!ObjectUtils.equals(methodName, s.getMethodName()))
//			return false;
//		if(!ObjectUtils.equals(parameterTypes, s.getParameterTypes()))
//			return false;
//		if(requirePublic!=s.isRequirePublic())
//			return false;
//		if(!ObjectUtils.equals(returnType, s.getReturnType()))
//			return false;
//		return true;
//	}
//	
//    @Override
//    public int hashCode()
//    {
//        final int PRIME = 31;
//        int result = 1;
//        result = PRIME * result + ( ( this.argCount == null ) ? 0 : this.argCount.hashCode() );
//        result = PRIME * result + ( ( this.methodName == null ) ? 0 : this.methodName.hashCode() );
//        result = PRIME * result + ( ( this.parameterTypes == null ) ? 0 : this.parameterTypes.hashCode() );
//        result = PRIME * result + ( ( this.requirePublic ) ? 1 : 0 );
//        result = PRIME * result + ( ( this.returnType == null ) ? 0 : this.returnType.hashCode() );
//        return result;
//    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((argCount == null) ? 0 : argCount.hashCode());
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + Arrays.hashCode(parameterTypes);
		result = prime * result + (requirePublic ? 1231 : 1237);
		result = prime * result
				+ ((returnType == null) ? 0 : returnType.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectorImpl other = (SelectorImpl) obj;
		if (argCount == null) {
			if (other.argCount != null)
				return false;
		} else if (!argCount.equals(other.argCount))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (!Arrays.equals(parameterTypes, other.parameterTypes))
			return false;
		if (requirePublic != other.requirePublic)
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}
	/**
	 * 
	 * @param methodName
	 * @param parameterTypes null means unknown parameters. It does not mean no parameters.
	 */
	public SelectorImpl(String methodName,Class<?>... parameterTypes){
		this(methodName,parameterTypes,true);
	}
	
	/**
	 * 
	 * @param methodName
	 * @param parameterTypes null means unknown parameters. It does not mean no parameters.
	 * @param requirePublic
	 */
	public SelectorImpl(String methodName,Class<?> [] parameterTypes,boolean requirePublic){
		setMethodName(methodName);
		setParameterTypes(parameterTypes);
		setRequirePublic(requirePublic);
	}
	
	/**
	 * 
	 * @param methodName
	 * @param returnType null if not specified
	 * @param argCount null if not specified
	 */
	public SelectorImpl(String methodName,Class<?> returnType,Integer argCount,boolean requirePublic){
		setMethodName(methodName);
		setParameterTypes(null);
		setReturnType(returnType);
		setRequiredArgCount(argCount);
		setRequirePublic(requirePublic);
	}

	
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#invoke(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object object,Object[]arguments) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Method m = findMethod(object.getClass());
		if(m==null)
			throw new NoSuchMethodException(this.toString());
		
		return m.invoke(object, arguments);
	}
	
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#unsafeInvoke(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public Object unsafeInvoke(Object object,Object[]arguments) throws NoSuchMethodException{
		Method m = findMethod(object.getClass());
		if(m==null)
			throw new NoSuchMethodException(this.toString());
		try {
			return m.invoke(object, arguments);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("method has illegal or wrong number of arguments: " + m, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("method is not public: " + m , e);
		} catch (InvocationTargetException e) {
			throw NSForwardException.runtimeExceptionForThrowable(e);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#findMethod(java.lang.Object)
	 */
	@Override
	public Method findMethod(Class<?> clazz){
		Method m  = null;
		
		if(getParameterTypes()==null)
			return findMethod2(clazz);
		
		try {
			m = clazz.getMethod(
					getMethodName(), getParameterTypes());

			if(isRequirePublic() && !Modifier.isPublic(m.getModifiers())){
				System.err.println(m + " is not public");
				m = null;
			}
		} catch (NoSuchMethodException e) {
		}

		
		return m;
	}
	
	private Method findMethod2(Class<?> clazz){
		Method [] methods = clazz.getMethods();

		for(int i = 0; i < methods.length; i++){
			if(methods[i].getName().equals(getMethodName())){
				Class<?> [] paramsTypes = methods[i].getParameterTypes();
				
				if(getParameterTypes() != null && !paramsTypes.equals(getParameterTypes()))
					continue;
				
				if(argCount != null && paramsTypes.length != argCount.intValue() )
					continue;

				if(isRequirePublic() && !Modifier.isPublic(methods[i].getModifiers())){
//					System.err.println(methods[i] + " is not public");
					continue;
				}

				if(getReturnType()!=null &&
						//!methods[i].getReturnType().equals(getReturnType()))
						!getReturnType().isAssignableFrom(methods[i].getReturnType())
						)
						continue;
				
				return methods[i]; //first match! (although there might be other potential matches TODO) 
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#getMethodName()
	 */
	@Override
	public String getMethodName() {
		return methodName;
	}
	
	/* (non-Javadoc)
	 * @see de.ceruti.curcuma.core.utils.Selector#setMethodName(java.lang.String)
	 */
	@Override
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.core.utils.Selector#getRequiredArgCount()
	 */
	@Override
	public Integer getRequiredArgCount() {
		return argCount;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.core.utils.Selector#setRequiredArgCount(java.lang.Integer)
	 */
	@Override
	public void setRequiredArgCount(Integer argCount) {
		this.argCount = argCount;
	}

//	public static void main(String arg[]){
//		NSSelector sel = new NSSelector("setMethodName",new Class[]{String.class},false);
//		
//		try {
//			sel.invoke(sel, new Object[]{"DD"});
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
