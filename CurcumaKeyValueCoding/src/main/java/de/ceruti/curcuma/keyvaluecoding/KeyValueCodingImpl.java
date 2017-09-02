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

package de.ceruti.curcuma.keyvaluecoding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.core.exceptions.ConversionException;
import de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.KeyValueCodingTargetException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantObjectException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.core.ValueConverter;

public class KeyValueCodingImpl implements KeyValueCoding {
	

	private static Category logger = Logger
	.getInstance(KeyValueCodingImpl.class);
	
	private final ErrorHandling errHandling;

	public KeyValueCodingImpl(){
		this.errHandling = new DefaultErrorHandlingImpl(this);
	}
	
	
	
	public Object TargetPoJo() {
		return this;
	}
	
	@Override
	public Object getValueForKey(String key) {
		return getValueForKey(TargetPoJo(),errHandling,key);
	}
	
	private static Object getValueForKey(Object pojo,ErrorHandling err,String key) {
		KeyPathUtilities.validateKey(key);
		Method m = ClassDescriptionCache.getGetterMethod(key,pojo);
		if(m==null)
			return err.getValueForUndefinedKey(key);
		
		try {
			return m.invoke(pojo, (Object[])null);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("method has illegal or wrong number of arguments: " + m, e);
		} catch (IllegalAccessException e) {
			NonCompliantAccessorFoundException e2 = new NonCompliantAccessorFoundException(pojo,key);
			e2.initCause(e);
			throw e2;
		} catch (InvocationTargetException e) {
			throw new KeyValueCodingTargetException(e.getCause(), m, pojo, key);
		}
		
	}
	

	@Override
	public Object getValueForKeyPath(String keypath) {
		StringBuilder headBuf = new StringBuilder();
		StringBuilder tailBuf = new StringBuilder();
		if(!KeyPathUtilities.extractHeadAndTail(keypath,headBuf,tailBuf))
			return getValueForKey(keypath);
		
		String head = headBuf.toString();
		String tail = tailBuf.toString();
		
		Object next = getValueForKey(head);
		
		if(next instanceof KeyValueCoding)
			return ((KeyValueCoding)next).getValueForKeyPath(tail);
		
		KeyValueCoding obj = createCompliantObjectForKey(next, head, false);
		if(obj==null && next!=null){
			throw new NonCompliantObjectException(this,next,head);
		}
		return obj==null ? null : obj.getValueForKeyPath(tail);
	}

	@Override
	public Object validateValueForKey(Object value,String key) throws ValidationException {
		Method m = ClassDescriptionCache.getValidateMethod(key,TargetPoJo());
		if(m==null)
			return value; // default is OK
		
		Class<?> parameterType = m.getParameterTypes()[0];
		
		try {
			value = coerceValueForKey(value,key,parameterType);
		}
		catch(ConversionException e){
			logger.error(e);
//			throw new IllegalArgumentException(e);
			throw new ValidationException(e);
		}
		
		try {
			return m.invoke(TargetPoJo(), value);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("method has illegal or wrong number of arguments: " + m + " args: " + value, e);
		} catch (IllegalAccessException e) {
			throw new NonCompliantAccessorFoundException(TargetPoJo(),key);
		} catch (InvocationTargetException e) {
			if(e.getCause() instanceof ValidationException)
				throw (ValidationException)e.getCause();
			throw new KeyValueCodingTargetException(e.getCause(), m, TargetPoJo(), key);
		}
	}
	

	@Override
	public Object validateValueForKeyPath(Object value, String keypath) throws ValidationException {
		StringBuilder head = new StringBuilder();
		StringBuilder tail = new StringBuilder();
		if(!KeyPathUtilities.extractHeadAndTail(keypath,head,tail))
			return validateValueForKey(value,keypath);
		Object next = getValueForKey(head.toString());
		
		if(next instanceof KeyValueCoding)
			return ((KeyValueCoding)next).validateValueForKeyPath(value,tail.toString());
		
		KeyValueCoding obj = createCompliantObjectForKey(next, head.toString(), false);
		return obj==null ? value : obj.validateValueForKeyPath(value,tail.toString());
	}
	
	
	private static Object coerceValueForKey(Object value,String key,Class<?> parameterType) throws ConversionException {

		try {

			if (value == null
					|| !parameterType.isAssignableFrom(value.getClass())) {
				return ValueConverter.convertObject(value, parameterType);
			}

			return value;
		} catch (NumberFormatException e) {
			throw new ConversionException(value,parameterType);
		}
	}
	
	
	@Override
	public void setValueForKey(Object value,Class<?> type,String key) {
		KeyValueCodingAspect.setValueForKey(TargetPoJo(),this,value,type,key);
	}

	protected static void setValueForKey(Object pojo,ErrorHandling err, Object value,Class<?> type,String key) {
		
		Method m = ClassDescriptionCache.getSetterMethod(key,type,pojo);
		if(m==null){
			err.setValueForUndefinedKey(value,type,key);
			return;
		}
		
		Class<?> parameterType = m.getParameterTypes()[0];
		try {
			value = coerceValueForKey(value,key,parameterType);
		}
		catch(ConversionException e){
			logger.error(e);
			throw new IllegalArgumentException(e);
		}
		
		if(value==null && parameterType.isPrimitive()){
			err.setNullValueForKey(key);
			return;
		}
		
		try {
			m.invoke(pojo,value);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("method has illegal or wrong number of arguments: " + m + " args: " + value, e);
		} catch (IllegalAccessException e) {
			throw new NonCompliantAccessorFoundException(pojo,key);
		} catch (InvocationTargetException e) {
			throw new KeyValueCodingTargetException(e.getCause(), m, pojo, key);
		}

	}

	
	@Override
	public void setValueForKeyPath(Object value,Class<?> type, String keypath) {
		StringBuilder head = new StringBuilder();
		StringBuilder tail = new StringBuilder();
		if(!KeyPathUtilities.extractHeadAndTail(keypath,head,tail)) {
			setValueForKey(value,keypath);
			return;
		}
		Object next = getValueForKey(head.toString());
		if(next instanceof KeyValueCoding){
			((KeyValueCoding)next).setValueForKeyPath(value,type,tail.toString());
			return;
		}
		KeyValueCoding obj = createCompliantObjectForKey(next, head.toString(), true);
		if(obj!=null) obj.setValueForKeyPath(value,type,tail.toString());
	}

	
	@Override
	public void setValueForKey(Object value, String key) {
		setValueForKey(value, null,key);
	}


	@Override
	public void setValueForKeyPath(Object value, String keypath) {
		setValueForKeyPath(value, null,keypath);
	}

	@Override
	public List mutableArrayValueForKeyPath(String keypath) {
		StringBuilder head = new StringBuilder();
		StringBuilder tail = new StringBuilder();
		if(!KeyPathUtilities.extractHeadAndTail(keypath,head,tail))
			return mutableArrayValueForKey(keypath);
		Object next = getValueForKey(head.toString());
		
		if(next instanceof KeyValueCoding)
			return ((KeyValueCoding)next).mutableArrayValueForKeyPath(tail.toString());
		
		KeyValueCoding obj = createCompliantObjectForKey(next, head.toString(), false);
		return obj==null ? null : obj.mutableArrayValueForKeyPath(tail.toString());
	}

	
		
	@Override
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy(this,key);
	}


	@Override
	public Object getValueForUndefinedKey(String key) {
		return errHandling.getValueForUndefinedKey(key);
	}

	@Override
	public void setValueForUndefinedKey(Object value, Class<?> type, String key) {
		errHandling.setValueForUndefinedKey(value, type, key);
	}

	@Override
	public void setNullValueForKey(String key) {
		errHandling.setNullValueForKey(key);
	}

	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element,
			String key, boolean isWrite) {
		return errHandling.createCompliantObjectForKey(element, key, isWrite);
	}

	
	@Override
	public Set mutableSetValueForKey(String key) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@Override
	public Set mutableSetValueForKeyPath(String keypath) {
		StringBuilder head = new StringBuilder();
		StringBuilder tail = new StringBuilder();
		if(!KeyPathUtilities.extractHeadAndTail(keypath,head,tail))
			return mutableSetValueForKey(keypath);
		Object next = getValueForKey(head.toString());
		
		if(next instanceof KeyValueCoding)
			return ((KeyValueCoding)next).mutableSetValueForKeyPath(tail.toString());
		
		KeyValueCoding obj = createCompliantObjectForKey(next, head.toString(), false);
		return obj==null ? null : obj.mutableSetValueForKeyPath(tail.toString());
	}


}