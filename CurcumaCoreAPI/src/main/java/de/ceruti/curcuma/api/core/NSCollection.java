package de.ceruti.curcuma.api.core;

public interface NSCollection<T> {
	boolean isMutable();
	
	NSCollection<T> immutableCopy();
	NSCollection<T> mutableCopy();
	
}
