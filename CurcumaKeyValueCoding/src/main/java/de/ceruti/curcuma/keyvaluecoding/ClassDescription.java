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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.ceruti.curcuma.api.core.utils.Selector;
import de.ceruti.curcuma.core.utils.SelectorImpl;

public class ClassDescription {

	private Class<?> clazz;

	/**
	 * cached getters. Class->(key->Method)
	 */
	private Map<String, Method> getterMethodCache = new HashMap<String, Method>();
	/**
	 * cached setters. Class->(key->Method)
	 */
	private Map<String, Method> setterMethodCache = new HashMap<String, Method>();

	/**
	 * cached validate-Methods. Class->(key->Method)
	 */
	private Map<String, Method> validatorMethodCacheByClass = new HashMap<String, Method>();

	private Map<Selector, Method> selectorMethodBySelector = new HashMap<Selector, Method>();

	private static final Method NULL_METHOD = null;

	public ClassDescription(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		if (!clazz.equals(this.clazz)) {
			flushCaches();
			this.clazz = clazz;
		}
	}

	public void flushCaches() {
		getGetterCache().clear();
		getSetterCache().clear();
		getValidatorCache().clear();
		getterMethodCache.clear();
	}

	private static abstract class MyMethodSelectorForKey {
		String key;
		Class<?>[] parameterTypes;

		MyMethodSelectorForKey(String key, Class<?>[] parameterTypes) {
			this.key = key;
			this.parameterTypes = parameterTypes;
		}

		abstract String[] getMethodNames();
	}

	private static class GetterMethodSelector extends MyMethodSelectorForKey {

		GetterMethodSelector(String key) {
			super(key, new Class[] {});
		}

		@Override
		public String[] getMethodNames() {
			String capKey = KeyPathUtilities.capitalizedKey(key);
			return new String[] { "get" + capKey, "is" + capKey, key };
		}
	}

	private static class SetterMethodSelector extends MyMethodSelectorForKey {

		SetterMethodSelector(String key, Class<?> type) {
			super(key, type != null ? new Class[] { type } : null);
		}

		@Override
		public String[] getMethodNames() {
			String capKey = KeyPathUtilities.capitalizedKey(key);

			return new String[] { "set" + capKey };
		}
	}

	/**
	 * search validate_Key_(Object value, StringBuffer error)
	 * 
	 * @author matteo
	 * 
	 */
	private static class ValidatorMethodSelector extends MyMethodSelectorForKey {

		ValidatorMethodSelector(String key) {
			super(key, null);

		}

		@Override
		public String[] getMethodNames() {
			char c = key.charAt(0);
			c = Character.toUpperCase(c);
			key = c + key.substring(1, key.length());

			return new String[] { "validate" + key };
		}
	}

	private Map<String, Method> getGetterCache() {
		return getterMethodCache;
	}

	private Map<String, Method> getSetterCache() {
		return setterMethodCache;
	}

	private Map<String, Method> getValidatorCache() {
		return validatorMethodCacheByClass;
	}
	
	public Method getMethod(Selector sel) {
		return getMethod(sel, selectorMethodBySelector);
	}

	private Method getMethod(Selector selector,
			Map<Selector, Method> cache) {
		if (cache.containsKey(selector)) {
			Object o = cache.get(selector);
			if (o == NULL_METHOD)
				return null;

			return (Method) o;
		}

		Method m = null;

		m = selector.findMethod(clazz);
		// synchronized(target){
		if (m == null)
			cache.put(selector, NULL_METHOD);
		else
			cache.put(selector, m);
		// }
		return m;
	}

	private Method getMethod(String key,
			MyMethodSelectorForKey sel, Map<String, Method> cache) {
		KeyPathUtilities.validateKey(key);

		if (cache.containsKey(key)) {
			Object o = cache.get(key);
			if (o == NULL_METHOD)
				return null;

			return (Method) o;
		}
		Method m = null;
		SelectorImpl selector = null;
		String[] names = sel.getMethodNames();
		for (int i = 0; i < names.length; i++) {

			if (selector == null)
				selector = new SelectorImpl(names[i], sel.parameterTypes, true);
			else
				selector.setMethodName(names[i]);

			m = selector.findMethod(clazz);

			if (m != null)
				break;

		}

		// synchronized(target){
		if (m == null)
			cache.put(key, NULL_METHOD);
		else
			cache.put(key, m);
		// }
		return m;
	}

	public Method getGetterMethod(String key) {
		return getMethod(key, new GetterMethodSelector(key),
				getGetterCache());
	}

	public Method getSetterMethod(String key, Class<?> type) {
		return getMethod(key, new SetterMethodSelector(key, type),
				getSetterCache());
	}

	public Method getValidateMethod(String key) {
		return getMethod(key, new ValidatorMethodSelector(key),
				getValidatorCache());
	}
}
