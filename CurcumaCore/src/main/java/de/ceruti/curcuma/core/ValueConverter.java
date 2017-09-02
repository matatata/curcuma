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

package de.ceruti.curcuma.core;

import java.util.HashMap;
import java.util.Map;

import de.ceruti.curcuma.api.core.Value;
import de.ceruti.curcuma.api.core.exceptions.ConversionException;


public class ValueConverter {
	enum Type {
		INTEGER,
		DOUBLE,
		FLOAT,
		BYTE,
		SHORT,
		LONG,
		BOOL,
		CHAR,
		STRING,
		OBJECT,
		VALUE,
		
		/**
		 * Unsupported
		 */
		OTHER;

	}

	private ValueConverter(){
		
	}
	
	
	private static Map<Class<?>,Type> clazz2ClazzID = new HashMap<Class<?>,Type>();
	
	static {
		register(Integer.class,Type.INTEGER);
		register(Double.class,Type.DOUBLE);
		register(Float.class,Type.FLOAT);
		register(Boolean.class,Type.BOOL);
		register(Character.class,Type.CHAR);
		register(Byte.class,Type.BYTE);
		register(Short.class,Type.SHORT);
		register(Long.class,Type.LONG);
		register(Number.class,Type.DOUBLE);
		register(int.class,Type.INTEGER);
		register(double.class,Type.DOUBLE);
		register(float.class,Type.FLOAT);
		register(boolean.class,Type.BOOL);
		register(char.class,Type.CHAR);
		register(byte.class,Type.BYTE);
		register(short.class,Type.SHORT);
		register(long.class,Type.LONG);
		
		register(String.class,Type.STRING);
		register(Object.class,Type.OBJECT);
//		register(Value.class,Type.VALUE);
	}
	
	/**
	 * for fast lookup
	 * @param clazz
	 * @param id
	 */
	private static void register(Class<?> clazz, Type id) {
		clazz2ClazzID.put(clazz, id);
	}
	
	/**
	 * 
	 * @param clazz
	 * @return OTHER if not found
	 */
	private static Type getID(Class<?> clazz) {
		if (!clazz2ClazzID.containsKey(clazz)) {
			if (Value.class.isAssignableFrom(clazz))
				return Type.VALUE;
			
			return Type.OTHER;
		}
		return clazz2ClazzID.get(clazz);
	}


	/**
	 * If necessary attempts to convert <code>convertMe</code> into an object that can
	 * be assigned to a variable of the type <code>clazz</code>.
	 * 
	 * @param convertMe
	 *            the object to convert
	 * @param clazz
	 *            the desired type
	 * @return an instance of <class>clazz</code>
	 * @throws ConversionException
	 *             if obj could not be converted
	 */
	public static Object convertObject(final Object convertMe, final Class<?> clazz) throws ConversionException {
		if (convertMe != null) {
			Class<?> fromClass = convertMe.getClass();
			if (clazz.isAssignableFrom(fromClass))
				return convertMe;
		}
		
		//see if clazz is a supported type
		Type clazzID = getID(clazz);
		
		if(clazzID==Type.OTHER) {
			if(convertMe instanceof Value) {
				Object objVal = ((Value)convertMe).objectValue();
				if(objVal!=convertMe)
					return convertObject(objVal, clazz);
			}

			if(convertMe==null)
				return null;
			
			throw new ConversionException(convertMe,clazz);
		}
		
		return convertObject(Values.value(convertMe),clazzID);
	}
	
	/**
	 * @param object
	 * @param CLAZZID
	 * @param destClazz
	 * @return
	 * @throws IllegalArgumentException if <code>CLAZZID</code> is none of the supported types.
	 */
	private static Object convertObject(Value object,Type CLAZZID){
		switch(CLAZZID){
		case INTEGER :
			return object.intValue();
		case DOUBLE :
			return object.doubleValue();
		case FLOAT :
			return object.floatValue();
		case BOOL :
			return object.boolValue();
		case CHAR:
			return object.charValue();
		case BYTE:
			return object.byteValue();
		case SHORT:
			return object.shortValue();
		case LONG:
			return object.longValue();
		case STRING:
			return object.stringValue();
		case VALUE:
		  return object;
		case OBJECT:
			return object.objectValue();
		default: throw new IllegalArgumentException();
		}
	}
	
	
	
}