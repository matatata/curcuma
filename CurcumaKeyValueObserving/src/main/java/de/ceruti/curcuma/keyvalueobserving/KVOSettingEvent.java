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

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;

public class KVOSettingEvent implements KVOEvent {
	private static final Object uninitializedValue = new String("KVOSettingEvent.unitializedValue"); 
	private Object newValue = uninitializedValue;
	private Object oldValue = uninitializedValue;
	private boolean isPriorEvent;
	
	public static final KVOEvent emptySettingEvent = new KVOSettingEvent();
	
	public KVOSettingEvent() {
	}
	
	public KVOSettingEvent(KVOEvent src) {
		if(src.getKind()!=KVOEvent.KEY_VALUE_CHANGE_SETTING)
			throw new IllegalArgumentException();
		
		if(src.hasNewValue())
			setNewValue(src.getNewValue());
		
		if(src.hasOldValue())
			setOldValue(src.getOldValue());
		
		setPriorEvent(src.isPriorEvent());
	}
	
	public KVOSettingEvent(Object oldObj,Object newObj){
		newValue=newObj;
		oldValue=oldObj;
	}
	
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	@Override
	public IndexSet getIndexes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getKind() {
		return KVOEvent.KEY_VALUE_CHANGE_SETTING;
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
		StringBuffer buf = new StringBuffer("{KVOSettingEvent");
		
		if(isPriorEvent()){
			buf.append(" PriorEvent");
		}
		
		if(hasOldValue())
			buf.append(" from ").append(getOldValue());
		
		if(hasNewValue())
			buf.append(" to ").append(getNewValue());
		
		
		
		buf.append("}");
		
		return buf.toString();
	}

	@Override
	public boolean isPriorEvent() {
		return isPriorEvent;
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
		return false;
	}
	
	public void invalidateOldValue() {
		this.oldValue = uninitializedValue;
	}
	
	public void invalidateNewValue() {
		this.newValue = uninitializedValue;
	}
	
}
