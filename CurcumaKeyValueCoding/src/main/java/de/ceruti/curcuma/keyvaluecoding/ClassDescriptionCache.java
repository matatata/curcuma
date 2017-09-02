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

public class ClassDescriptionCache {
	private static Map<Class<?>, ClassDescription> descByClass = new HashMap<Class<?>, ClassDescription>();

	public static ClassDescription getClassDescription(Class<?> key) {
		if (!descByClass.containsKey(key))
			descByClass.put(key, new ClassDescription(key));

		return descByClass.get(key);
	}

	public void flushCaches() {
		for (ClassDescription desc : descByClass.values()) {
			desc.flushCaches();
		}
		descByClass.clear();
	}

	// Convenience-Methods

	public static Method getGetterMethod(String key, Object target) {
		return getGetterMethod(key,target.getClass());
	}

	public static Method getSetterMethod(String key, Class<?> type,
			Object target) {
		return getSetterMethod(key,type,target.getClass());
	}

	public static Method getGetterMethod(String key, Class<?> clazz) {
		return getClassDescription(clazz).getGetterMethod(key);
	}
	
	public static Method getValidateMethod(String key, Object target) {
		return getValidateMethod(key,target.getClass());
	}

	public static Method getMethod(Selector sel, Object target) {
		return getMethod(sel, target.getClass());
	}
	
	public static Method getSetterMethod(String key, Class<?> type,
			Class<?> clazz) {
		return getClassDescription(clazz)
				.getSetterMethod(key, type);
	}

	public static Method getValidateMethod(String key, Class<?> clazz) {
		return getClassDescription(clazz).getValidateMethod(key);
	}

	public static Method getMethod(Selector sel, Class<?> clazz) {
		return getClassDescription(clazz).getMethod(sel);
	}
}
