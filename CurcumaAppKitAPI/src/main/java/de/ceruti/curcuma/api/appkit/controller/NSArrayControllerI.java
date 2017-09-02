package de.ceruti.curcuma.api.appkit.controller;

import java.util.Collection;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;

@SuppressWarnings("rawtypes")
public interface NSArrayControllerI extends NSObjectControllerI{

	String CONTENT_ARRAY = "contentArray";
	String ARRANGED_OBJECTS = "arrangedObjects";

	boolean isPreservesSelection();

	void setPreservesSelection(boolean preservesSelection);

	boolean isSelectsInsertedObjects();

	void setSelectsInsertedObjects(boolean selectsInsertedObjects);
	
	void setClearsFilterPredicateOnInsertion(boolean b);

	boolean isClearsFilterPredicateOnInsertion();
	
	boolean isAlwaysUsesMultipleValuesMarker();
	
	void setAlwaysUsesMultipleValuesMarker(boolean b);
	
	IndexSet getSelectionIndexes();

	void setSelectionIndexes(IndexSet indexes);

	void clearSelection();

	boolean addSelectionIndexes(IndexSet idx);

	boolean removeSelectionIndexes(IndexSet idx);

	void setSelectedObjects( Collection obj);

	boolean addSelectedObjects(Collection obj);

	boolean removeSelectedObjects(Collection obj);

	
	void setSelection(Object o);

	void setContent(Object content);

	List getContentArray();

	void setContentArray(List array);

	List getArrangedObjects();

	/**
	 * remove the selected Objects from the receivers content and arrangedObjects array
	 */
	void remove();

	/**
	 * Creates and adds a new object to the receiver's content and arranged objects.
	 */
	void add();

	/**
	 * F�gt obj dem contentArray und den arrangedObjects hinzu 
	 * @param obj
	 */
	void addObject(Object obj);

	/**
	 * F�gt o dem contentArray und den arrangedObjects hinzu (letzteres steht so nicht in der Cocoa DOku, ber ich denke es trifft doch zu
	 * @param o
	 */
	void addObjects(Collection o);

	/**
	 * Entfernt o vom contentArray und von den arrangedObjects
	 * @param o
	 */
	void removeObject(Object o);
	
	void removeObjects(Collection c);

	NSArrayFilter getFilterPredicate();

	void setFilterPredicate(NSArrayFilter filterPredicate);

	/**
	 * Makes a copy
	 * @param src
	 * @return
	 */
	List arrangeObjects(List src);

	void rearrangeObjects();

}