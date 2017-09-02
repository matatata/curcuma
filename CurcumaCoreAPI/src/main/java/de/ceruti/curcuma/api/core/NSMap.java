package de.ceruti.curcuma.api.core;

public interface NSMap<K,T> extends java.util.Map<K,T> {
	boolean isMutable();
	
	NSMap<K,T> immutableCopy();
	NSMap<K,T> mutableCopy();
	
}
