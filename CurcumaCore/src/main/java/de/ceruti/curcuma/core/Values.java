package de.ceruti.curcuma.core;

import de.ceruti.curcuma.api.core.Value;
import de.ceruti.curcuma.api.core.exceptions.ConversionException;

public final class Values {
	private Values(){
	}
	
	public static Value number(int integer){
		return new CCInteger(integer);
	}
	
	public static Value number(double dbl){
		return new CCDouble(dbl);
	}
	
	public static Value number(Number n){
		return new CCNumber(n);
	}
	
	public static Value string(String s){
		return new CCString(s);
	}
	
	private static Value objectValue(Object obj) {
		if (obj == null)
			return CCNull.getInstance();
		return new CCObjectValue(obj);
	}

	public static Value Null(){
		return CCNull.getInstance();
	}
	
	public static Value value(Object obj) {
		if(obj == null)
			return Null();
		if(obj instanceof Value)
			return (Value)obj;
		if(obj instanceof String)
			return string((String)obj);
		if(obj instanceof Number)
			return number((Number)obj);
		if(obj instanceof Boolean)
			return number((Boolean)obj?1:0);
		
		return objectValue(obj);
	}
	
	public static Value number(Object obj) throws ConversionException {
		if(obj == null)
			return Null();
		if(obj instanceof Value)
			return (Value)obj;
		if(obj instanceof String)
			return string((String)obj);
		if(obj instanceof Number)
			return number((Number)obj);
		if(obj instanceof Boolean)
			return number((Boolean)obj?1:0);
		
		throw new ConversionException(obj,Number.class);
	}
}
