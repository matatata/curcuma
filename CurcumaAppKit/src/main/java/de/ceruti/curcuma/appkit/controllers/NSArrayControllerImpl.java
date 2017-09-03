package de.ceruti.curcuma.appkit.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSSelectionMarker;
import de.ceruti.curcuma.api.appkit.controller.NSArrayControllerI;
import de.ceruti.curcuma.api.appkit.controller.NSArrayFilter;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.MutableIndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.core.Factory;
import de.ceruti.curcuma.core.Utils;
import de.ceruti.curcuma.foundation.ObservableArray;
import de.ceruti.curcuma.keyvalueobserving.NoKeyValueObserving;

@SuppressWarnings({"rawtypes","unchecked"})
public class NSArrayControllerImpl extends NSObjectControllerImpl implements
		NSArrayControllerI {
	private static Category logger = Logger
			.getInstance(NSArrayControllerImpl.class);

	private MutableIndexSet selectionIndexes = Factory.mutableIndexSet();
	private NSArrayFilter filterPredicate;
	private ObservableArray observableArrangedObjects;
	private List mutableKVOContentArray;


	private boolean selectsInsertedObjects = false;
	private boolean preservesSelection = false;
	private boolean clearsFilterPredicateOnInsertion = false;
	private boolean alwaysUsesMultipleValuesMarker;

	public NSArrayControllerImpl() {
		observableArrangedObjects = new ObservableArray(this, this,
				NSArrayControllerI.ARRANGED_OBJECTS, new ArrayList());
		mutableKVOContentArray = mutableArrayValueForKey(NSArrayControllerI.CONTENT_ARRAY);
	}
	
	private List getMutableKVOContentArray() {
		return mutableKVOContentArray;
	}

	@Override
	public void add() {
		if (canAdd())
			addObject(newObject());
	}

	@Override
	public void addObject(Object obj) {
		if(!doCommitStuff())
			return;
		
		getMutableKVOContentArray().add(obj);

		if (getFilterPredicate() != null
				&& isClearsFilterPredicateOnInsertion()) {
			setFilterPredicate(null);
			if (isSelectsInsertedObjects()) {
				int index = getArrangedObjects().indexOf(obj);
				if (index != -1)
					addSelectionIndexes(Factory.indexSet(index));
			}
			return;
		}

		if (matchesFilter(obj)) {
			if (isSelectsInsertedObjects()) {
				int index = -1;
				try {
					boolean chSel = selectionWillChange(Factory.emptyIndexSet()); //current selection is not altered
					getArrangedObjects().add(obj);
					if (chSel) {
						index = getArrangedObjects().indexOf(obj);
						if (index != -1)
							selectionIndexes.add(Factory.range(index));
					}
				} finally {
					selectionDidChange(index==-1 ? Factory.emptyIndexSet() : Factory.indexSet(index));
				}
			} else {
				getArrangedObjects().add(obj);
			}
		}
	}

	@Override
	public boolean canRemove() {
		return !getSelectedObjects().isEmpty();
	}
	public void setCanRemove(boolean b){
		logger.info(this.getClass().getCanonicalName() + " ignoring setCanRemove()");
	}

	@Override
	protected List prepareSelectedObjects(List current) {
		if (getSelectionIndexes().isEmpty()) {
			return new ArrayList<Object>(); //must return a new Object
		}
		return Utils.objectsAtIndexes(getArrangedObjects(),
				getSelectionIndexes());
	}

	// @Override
	// public List getSelectedObjects() {
	// //TODO cache them!
	// return Utils.objectsAtIndexes(getArrangedObjects(),
	// getSelectionIndexes());
	// }

	@Override
	public void remove() {
		if (canRemove()) {
			if(!doCommitStuff())
				return;
			
			// make a copy of selected objects
			List selected = new ArrayList(getSelectedObjects());
			clearSelection();
			removeObjects(selected);
		}
	}

	@Override
	public void addObjects(Collection c) {
		if(!doCommitStuff())
			return;
		
		getMutableKVOContentArray().addAll(c);

		if (getFilterPredicate() != null
				&& isClearsFilterPredicateOnInsertion()) {
			setFilterPredicate(null);
			if (isSelectsInsertedObjects()) {
				IndexSet indexes = Utils.indexesInList(getArrangedObjects(), c);
				addSelectionIndexes(indexes);
			}
			return;
		}

		Collection<Object> matches = new ArrayList<Object>();
		for (Object o : c) {
			if (matchesFilter(o)) {
				matches.add(o);
			}
		}

		if (matches.isEmpty())
			return;

		if (isSelectsInsertedObjects()) {
			IndexSet affectedIndexes = null;
			try {
				boolean chSel = selectionWillChange(Factory.emptyIndexSet()); //the current selection will not be alterered
				getArrangedObjects().addAll(matches); // notifies observers
				if (chSel) {
					affectedIndexes = Utils.indexesInList(
							getArrangedObjects(), matches);
					for (Iterator<Range> it = affectedIndexes.rangesIterator(); it
							.hasNext();) {
						selectionIndexes.add(it.next());
					}
					
				}
			} finally {
				selectionDidChange(affectedIndexes);
			}
		} else {
			getArrangedObjects().addAll(matches);
		}

	}

	@Override
	public boolean removeSelectedObjects(Collection objs) {
		IndexSet indexes = Utils.indexesInList(getArrangedObjects(), objs);
		return removeSelectionIndexes(indexes);
	}

	@Override
	public boolean addSelectedObjects(Collection objs) {
		IndexSet indexes = Utils.indexesInList(getArrangedObjects(), objs);
		return addSelectionIndexes(indexes);
	}

	private int changingSelection = 0;

	/**
	 * 
	 * @param affectedIndexes null means whole selection is affected
	 * @return
	 */
	protected boolean selectionWillChange(IndexSet affectedIndexes){
		logger.debug("selectionWillChange(" + affectedIndexes + ")");
		
		changingSelection++;
		
		if (changingSelection > 1) {
			//Can happen if you remove rows that are currenty selected
			logger.debug("nested selectionWillChange detected");
			return false;
		}
		
		getSelectionProxy().setSelectionChangeDelta(affectedIndexes);
		
		boolean ret = super.selectionWillChange();
		willChangeValueForKey("selectionIndexes");
		return ret;
	}
	
	@Override
	protected boolean selectionWillChange() {
		return selectionWillChange(null);
	}

	/**
	 * 
	 * @param affectedIndexes null means whole selection is affected
	 * @return
	 */
	protected void selectionDidChange(IndexSet affectedIndexes) {
		logger.debug("selectionDidChange(" + affectedIndexes + ")");
		
		changingSelection--;
		
		if (changingSelection > 0) {
			//Can happen if you remove rows that are currenty selected
			logger.debug("nested selectionDidChange detected");
			return;
		}
		getSelectionProxy().setSelectionChangeDelta(affectedIndexes);
		
		didChangeValueForKey("selectionIndexes");
		super.selectionDidChange();
		
		//reset
		getSelectionProxy().setSelectionChangeDelta(null);
	}
	
	@Override
	protected void selectionDidChange() {
		selectionDidChange(null);
	}

	

	
	@Override
	public boolean addSelectionIndexes(IndexSet idx) {
		logger.debug("addSelectionIndexes(" + idx + ")");
		if(!doCommitStuff())
			return false;
		try {
			if (selectionWillChange(Factory.emptyIndexSet())) {
				for (Iterator<Range> it = idx.rangesIterator(); it.hasNext();) {
					selectionIndexes.add(it.next());
				}
			}
		} finally {
			selectionDidChange(idx);
		}
		return true;
	}

	@Override
	public boolean removeSelectionIndexes(IndexSet idx) {
		logger.debug("removeSelectionIndexes(" + idx + ")");
		
		if (!doCommitStuff())
			return false;
		
		try {
			if(selectionWillChange(idx)){
				for (Iterator<Range> it = idx.rangesIterator(); it.hasNext();) {
					selectionIndexes.remove(it.next());
				}
			}
		} finally {
			selectionDidChange(Factory.emptyIndexSet());
		}
		return true;
	}

	@Override
	public List arrangeObjects(List src) {
		List ret = new ArrayList();

		if (src != null && !src.isEmpty()) {
			if (getFilterPredicate() == null)
				ret.addAll(src);
			else {
				for (Object o : src) {
					if (getFilterPredicate().filter(o))
						ret.add(o);
				}
			}
		}

		return ret;
	}

	@Override
	public void clearSelection() {
		setSelectionIndexes(Factory.emptyIndexSet());
	}

	@Override
	public List getArrangedObjects() {
		return observableArrangedObjects;
	}

	@Override
	public List getContentArray() {
		return (List) getContent();
	}

	@Override
	public NSArrayFilter getFilterPredicate() {
		return this.filterPredicate;
	}

	@Override
	public IndexSet getSelectionIndexes() {
		return selectionIndexes.immutableIndexSet();
	}

	@Override
	public boolean isPreservesSelection() {
		return this.preservesSelection;
	}

	@Override
	public boolean isSelectsInsertedObjects() {
		return this.selectsInsertedObjects;
	}

	@Override
	public void removeObject(Object o) {
		List selectedObjects = getSelectedObjects();
		if (getMutableKVOContentArray().remove(o)) {
			int index = selectedObjects.indexOf(o);
			try {
				boolean chSel=selectionWillChange(index!=-1 ? Factory.indexSet(index) : Factory.emptyIndexSet());
				getArrangedObjects().remove(o);
				selectedObjects.remove(o);
				if(chSel){
  				selectionIndexes = Utils.indexesInList(getArrangedObjects(),
  						selectedObjects).mutableIndexSet();
				}
			} finally {
				selectionDidChange(Factory.emptyIndexSet());
			}
		}
	}

	@Override
	public void removeObjects(Collection c) {
		List selectedObjects = getSelectedObjects();
		if (getMutableKVOContentArray().removeAll(c)) {
			IndexSet indexSet = Utils.indexesInList(selectedObjects,
						c);
			try {
				boolean chSel = selectionWillChange(indexSet);
				//If c 
				getArrangedObjects().removeAll(c);
				selectedObjects.removeAll(c);
				if(chSel){
  				selectionIndexes = Utils.indexesInList(getArrangedObjects(),
  						selectedObjects).mutableIndexSet();
				}
			} catch (RuntimeException e) {
				logger.error(e);
				throw e;
			} finally {
				selectionDidChange(Factory.emptyIndexSet());
			}
		}
	}

	@Override
	public void setContent(Object content) {
		if (getContent() == content)
			return;

		if (!(content instanceof List)) {
			if (content instanceof NSSelectionMarker)
				content = new ArrayList();
			else
				throw new IllegalArgumentException();
		}

		try {
			willChangeValueForKey("content");
			setContentNoKVO(content);
			if (isClearsFilterPredicateOnInsertion())
				this.filterPredicate = null;
			rearrangeObjects();
		} finally {
			didChangeValueForKey("content");
		}
	}

	/**
	 * calls {@link #selectionDidChange()} and {@link #selectionWillChange()}
	 * and post Notifications for "arrangedObjects"
	 */
	@Override
	public void rearrangeObjects() {

		if (!commitEditing(this))
			return;
		
		if (!isPreservesSelection()) {
			try {
				clearSelection();
				willChangeValueForKey(NSArrayControllerI.ARRANGED_OBJECTS);
				observableArrangedObjects = new ObservableArray(this, this,
						NSArrayControllerI.ARRANGED_OBJECTS, arrangeObjects(getContentArray()));
				return;
			} finally {
				didChangeValueForKey(NSArrayControllerI.ARRANGED_OBJECTS);
			}
		}

		// Preserve selection

		try {
			boolean chSel = selectionWillChange();
			willChangeValueForKey(NSArrayControllerI.ARRANGED_OBJECTS);
			
			List selectedObjects = null;

			if (chSel) {
				selectedObjects = getSelectedObjects();
			}
			observableArrangedObjects = new ObservableArray(this, this,
					NSArrayControllerI.ARRANGED_OBJECTS, arrangeObjects(getContentArray()));

			if (chSel) {
				selectionIndexes = Utils.indexesInList(getArrangedObjects(),
						selectedObjects).mutableIndexSet();
			}
			return;
		} finally {

			didChangeValueForKey(NSArrayControllerI.ARRANGED_OBJECTS);
			selectionDidChange();
			
		}
	}

	@Override
	public void setContentArray(List array) {
		setContent(array);
	}

	@Override
	public void setFilterPredicate(NSArrayFilter filterPredicate) {
		if (this.filterPredicate != filterPredicate) {
			this.filterPredicate = filterPredicate;
			rearrangeObjects();
		}
	}

	@Override
	public void setPreservesSelection(boolean preservesSelection) {
		this.preservesSelection = preservesSelection;
	}

	@Override
	public void setSelectedObjects(Collection objs) {
		IndexSet indexes = Utils.indexesInList(getArrangedObjects(), objs);
		setSelectionIndexes(indexes);
	}

	@Override
	public void setSelection(Object o) {
		int index = getArrangedObjects().indexOf(o);
		if (index == -1)
			return;

		setSelectionIndexes(Factory.indexSet(index));
	}

	@Override
	@NoKeyValueObserving
	public void setSelectionIndexes(IndexSet indexes) {
		if(!doCommitStuff())
			return;
		
		if (equals(indexes))
			return;

		try {
			if(!selectionWillChange())
			  return;
			selectionIndexes = indexes.mutableIndexSet();
			logger.debug("indexes=" + indexes);
		} finally {
			selectionDidChange();
		}
	}

	@Override
	public boolean isAlwaysUsesMultipleValuesMarker() {
		return this.alwaysUsesMultipleValuesMarker;
	}

	@Override
	public void setAlwaysUsesMultipleValuesMarker(boolean b) {
		this.alwaysUsesMultipleValuesMarker = b;
	}

	@Override
	public void setSelectsInsertedObjects(boolean selectsInsertedObjects) {
		this.selectsInsertedObjects = selectsInsertedObjects;
	}

	@Override
	public boolean isClearsFilterPredicateOnInsertion() {
		return clearsFilterPredicateOnInsertion;
	}

	@Override
	public void setClearsFilterPredicateOnInsertion(
			boolean clearsFilterPredicateOnInsertion) {
		this.clearsFilterPredicateOnInsertion = clearsFilterPredicateOnInsertion;
	}

	/**
	 * Wenn ein Filter da ist, wird obj dagegen geprueft.
	 * 
	 * @param obj
	 * @return
	 */
	protected boolean matchesFilter(Object obj) {
		if (getFilterPredicate() == null)
			return true;

		return getFilterPredicate().filter(obj);
	}

	@Override
	public List mutableArrayValueForKey(String key) {
		if (key.equals(NSArrayControllerI.ARRANGED_OBJECTS))
			return getArrangedObjects();
		return super.mutableArrayValueForKey(key);
	}
}
