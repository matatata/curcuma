package de.ceruti.curcuma.foundation;

import java.util.List;
import java.util.Map;

import de.ceruti.curcuma.api.foundation.NSArray;
import de.ceruti.curcuma.api.foundation.NSDictionary;

public class NSFactory {
	
	public static <T> NSArray<T> array() {
		return new NSImmutableArrayImpl<T>();
	}
	
	public static <T> NSArray<T> array(List<? extends T> list) {
		return new NSImmutableArrayImpl<T>(list);
	}
	
	public static <T> NSArray<T> array(T [] arr) {
		return new NSImmutableArrayImpl<T>(arr);
	}
	
	public static <T> NSArray<T> mutableArray() {
		return new NSMutableArrayImpl<T>();
	}
	
	public static <T> NSArray<T> mutableArray(int size){
		return new NSMutableArrayImpl<T>(size);
	}

	public static <T> NSArray<T> mutableArray(List<? extends T> list) {
		return new NSMutableArrayImpl<T>(list);
	}
	
	public static <T> NSArray<T> mutableArray(T [] arr) {
		return new NSMutableArrayImpl<T>(arr);
	}
	
	public static NSDictionary dictionary() {
		return new NSImmutableDictionaryImpl();
	}
	
	public static NSDictionary dictionary(Map<? extends String, ? extends Object> src) {
		return new NSImmutableDictionaryImpl(src);
	}

	public static NSDictionary mutableDictionary() {
		return new NSMutableDictionaryImpl();
	}
	
	public static NSDictionary mutableDictionary(Map<? extends String, ? extends Object> src) {
		return new NSMutableDictionaryImpl(src);
	}
	
	private NSFactory(){
	}
}
