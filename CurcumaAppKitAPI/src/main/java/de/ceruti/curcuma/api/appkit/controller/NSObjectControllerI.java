package de.ceruti.curcuma.api.appkit.controller;

import java.util.List;

import de.ceruti.curcuma.api.appkit.NSEditable;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;


@SuppressWarnings("rawtypes")
public interface NSObjectControllerI extends NSEditable, KeyValueCoding, KeyValueObserving, KeyValueBindingCreation {

	String ContentBinding="content";
	
	Object getContent();
	
	void setContent(Object content);
	
	Class getObjectClass();

	void setObjectClass(Class c);

	boolean canAdd();

	boolean canRemove();

	/**
	 * remove the receivers content
	 */
	void remove();

	/**
	 * set the receivers content to the object returned by newObject
	 */
	void add();
	
	/**
	 * Sets the receiver's content object.
	 * @param obj
	 */
	void addObject(Object obj);

	/**
	 * Creates a new instance of the class returned by getObjectClass
	 * @return
	 */
	Object newObject();

	/**
	 * 
	 * @return
	 */
	Object getSelection();

	List getSelectedObjects();

}