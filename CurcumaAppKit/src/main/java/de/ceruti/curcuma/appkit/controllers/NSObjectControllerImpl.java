package de.ceruti.curcuma.appkit.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.controller.NSObjectControllerI;
import de.ceruti.curcuma.appkit.NSDefaultEditable;
import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvalueobserving.NoKeyValueObserving;

@NSDefaultEditable
@SuppressWarnings("unchecked")
public class NSObjectControllerImpl extends NSObjectImpl implements NSObjectControllerI {
	private static Category logger = Logger
			.getInstance(NSObjectControllerImpl.class);
  
	private Class objectClass;
	private Object content;
	private SelectionProxy selectionProxy;
	private List selectedObjects = new ArrayList();

	protected SelectionProxy getSelectionProxy() {
		if (selectionProxy == null)
			selectionProxy = new SelectionProxy(this);
		return selectionProxy;
	}

	protected void setSelectionProxy(SelectionProxy selectionProxy) {
		this.selectionProxy = selectionProxy;
	}

	public NSObjectControllerImpl(){
		exposeBinding(ContentBinding,List.class);
	}
	
	public static Set<String> keyPathsForValuesAffectingValueForKey(String key){
		Set<String> s = NSObjectImpl.keyPathsForValuesAffectingValueForKey(key);
		
		if("canRemove".equals(key)){
			s.add("selection");
		}
		// well it kind of works, but is not well tested
		if("selectedObjects".equals(key)){
			s.add("selection");
		}
		
		return s;
	}
	
	@Override
	public void add() {
		setContent(newObject());
	}

	@Override
	public void addObject(Object obj) {
		setContent(obj);
	}

	@Override
	public boolean canAdd() {
		return getObjectClass() != null;
	}

	@Override
	public boolean canRemove() {
		return (getContent()!=null);
	}

	@Override
	public Class getObjectClass() {
		return objectClass;
	}
	
	protected List prepareSelectedObjects(List current) {
	  current.clear();
	  current.add(getContent());
	  return current;
	}

	@Override
	public List getSelectedObjects() {
		return selectedObjects;
	}

	
	@Override
	public Object getSelection() {
		return getSelectionProxy();
	}

	@Override
	public Object newObject() {
		if(getObjectClass()==null)
			return null;
		
		try {
			return getObjectClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void remove() {
		setContent(null);
	}

	@Override
	public void setObjectClass(Class c) {
		this.objectClass = c;
	}

	@Override
	public Object getContent() {
		return content;
	}

	/**
	 * In any case you must call {@link #selectionDidChange()} afterwards.
	 * @return false if this method was invoked recursively by itself - meaning there is already a selection change in progress. In this case you should not change the selection any further.
	 */
	protected boolean selectionWillChange() {
		logger.debug("selectionWillChange()");
		try {
			willChangeValueForKey("selection");
		}
		finally {
			getSelectionProxy().controllerWillChangeSelection();
		}
		return true;
	}

	protected void selectionDidChange() {
		logger.debug("selectionDidChange()");
		getSelectionProxy().controllerDidChangeSelection();
		selectedObjects = prepareSelectedObjects(selectedObjects);
		didChangeValueForKey("selection");
	}
	
	@Override
	@NoKeyValueObserving
	public void setContent(Object content) {
		if(!doCommitStuff())
			return;
		
		if(this.content != content){
			try {
				willChangeValueForKey("content");
				selectionWillChange();
				setContentNoKVO(content);
			} finally {
				selectionDidChange();
				didChangeValueForKey("content");
			}
		}
	}
	
	protected void setContentNoKVO(Object content){
		this.content = content;
	}
	
	
	private boolean discardOnCommitFailure = false;
	
	//TODO decide if public API
	protected boolean isDiscardOnCommitFailure() {
		return this.discardOnCommitFailure;
	}
	
	//TODO decide if public API
	protected void setDiscardOnCommitFailure(boolean b){
		this.discardOnCommitFailure=b;
	}
	
	protected boolean doCommitStuff() {
		if (!commitEditing(this)) {

			if (isDiscardOnCommitFailure()) {
				discardEditing(this);
				return true;
			}
			return false;
		}

		return true;
	}
	
}
