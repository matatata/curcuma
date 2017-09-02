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

package de.ceruti.curcuma.keyvalueobserving;

import java.util.ArrayList;
import java.util.List;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;

public class KVOIndexedChangeEvent implements KVOEvent {

	private static final List<?> uninitializedValue = new ArrayList<Object>(); 
	private List<?> oldValue = uninitializedValue; //removed or replaced objects
	private List<?> newValue = uninitializedValue; //inserted or replacing objects
	private boolean isPriorEvent;
	private int kvoKind;
	private IndexSet indexes;
	
	public KVOIndexedChangeEvent(KVOEvent src) {
		switch(src.getKind()){
		case KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
		case KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
		case KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		if(src.hasNewValue())
			setNewValue((List<?>)src.getNewValue());
		
		if(src.hasOldValue())
			setOldValue((List<?>)src.getOldValue());
		
		setPriorEvent(src.isPriorEvent());
	
		setIndexes(src.getIndexes());
	}
	
	public KVOIndexedChangeEvent(int kvoEventType, IndexSet indexes) {
		this(kvoEventType,indexes,uninitializedValue,uninitializedValue);
	}
	
	public KVOIndexedChangeEvent(int kvoEventType, IndexSet indexes,List<?> oldValue,List<?> newValue) {
		kvoKind = kvoEventType;
		this.indexes = indexes;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public IndexSet getIndexes() {
		return indexes;
	}

	@Override
	public int getKind() {
		return kvoKind;
	}
	
	private String getKindAsString() {
		switch (getKind()) {
		case KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
			return "ARRAYELEM_INSERTION";
		case KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
			return "ARRAYELEM_REMOVAL";
		case KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
			return "ARRAYELEM_REPLACEMENT";
		case KEY_VALUE_CHANGE_SETTING:
			return "SETTING";
		default:
			return ">> Unknown <<";
		}
	}

	@Override
	public Object getNewValue() {
		if(!hasNewValue())
			throw new IllegalStateException();
		return newValue;
	}

	@Override
	public Object getOldValue() {
		if(!hasOldValue())
			throw new IllegalStateException();
		return oldValue;
	}
	
	@Override
	public String toString(){
		StringBuffer ret=new StringBuffer("[type="+getKindAsString()+", indexes=" + getIndexes());
		
		if(isPriorEvent())
			ret.append(" PriorEvent");
		
		if(hasOldValue())
			ret.append(" oldValues=").append(oldValue);
		if(hasNewValue())
			ret.append(" newValues=").append(newValue);
		
		ret.append("]");
		return ret.toString();
	}

	@Override
	public boolean isPriorEvent() {
		return isPriorEvent;
	}

	public int getKvoKind() {
		return kvoKind;
	}

	public void setKvoKind(int kvoKind) {
		this.kvoKind = kvoKind;
	}

	public void setIndexes(IndexSet indexes) {
		this.indexes = indexes;
	}

	public void setOldValue(List<?> oldValue) {
		this.oldValue = oldValue;
	}

	public void setNewValue(List<?> newValue) {
		this.newValue = newValue;
	}

	public void setPriorEvent(boolean isPriorEvent) {
		this.isPriorEvent = isPriorEvent;
	}
	
	@Override
	public boolean hasNewValue() {
		return newValue!=uninitializedValue;
	}

	@Override
	public boolean hasOldValue() {
		return oldValue!=uninitializedValue;
	}
	
	@Override
	public boolean isIndexedEvent() {
		return true;
	}
	
	public void invalidateOldValue() {
		this.oldValue = uninitializedValue;
	}
	
	public void invalidateNewValue() {
		this.newValue = uninitializedValue;
	}
}
