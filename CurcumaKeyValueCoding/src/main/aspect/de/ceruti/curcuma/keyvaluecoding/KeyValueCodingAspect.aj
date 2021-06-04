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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.api.core.exceptions.ConversionException;
import de.ceruti.curcuma.api.keyvaluecoding.ErrorHandling;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.KeyValueCodingTargetException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantObjectException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.core.ValueConverter;

aspect KeyValueCodingAspect{
	

	private static Logger logger = LogManager
	.getLogger(KeyValueCodingAspect.class);
	
	public static interface KeyValueCodingSupport extends KeyValueCoding {
		Object TargetPoJo();
	}
	
	declare parents: @KeyValueCodeable * implements KeyValueCodingSupport;
	
	public Object KeyValueCodingSupport.TargetPoJo() {
		return this;
	}
	
	public Object KeyValueCodingSupport.getValueForKey(String key) {
		return KeyValueCodingAspect.getValueForKey(TargetPoJo(),this,key);
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
	

	public Object KeyValueCodingSupport.getValueForKeyPath(String keypath) {
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

	public Object KeyValueCodingSupport.validateValueForKey(Object value,String key) throws ValidationException {
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
	

	public Object KeyValueCodingSupport.validateValueForKeyPath(Object value, String keypath) throws ValidationException {
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
	
	
	public void KeyValueCodingSupport.setValueForKey(Object value,Class<?> type,String key) {
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

	
	public void KeyValueCodingSupport.setValueForKeyPath(Object value,Class<?> type, String keypath) {
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

	
	public void KeyValueCodingSupport.setValueForKey(Object value, String key) {
		setValueForKey(value, null,key);
	}


	public void KeyValueCodingSupport.setValueForKeyPath(Object value, String keypath) {
		setValueForKeyPath(value, null,keypath);
	}

	public List KeyValueCodingSupport.mutableArrayValueForKeyPath(String keypath) {
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

	// FIXME cannot define it here
//	public List KeyValueCodingSupport.mutableArrayValueForKey(String key) {
//		return new KVCMutableArrayProxy(this,key);
//	}

	public Set KeyValueCodingSupport.mutableSetValueForKeyPath(String keypath) {
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

	
		
	public Set KeyValueCodingSupport.mutableSetValueForKey(String key) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	

}