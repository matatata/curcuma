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

package de.ceruti.curcuma.api.keyvalueobserving;

import de.ceruti.curcuma.api.core.IndexSet;


public interface KVOEvent {
	int KEY_VALUE_CHANGE_SETTING = 1;
	int KEY_VALUE_CHANGE_ARRAYELEM_INSERTION = 2;
	int KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL = 3;
	int KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT = 4;
	
	
	int getKind();
	Object getOldValue();
	Object getNewValue();
	IndexSet getIndexes();
	
	boolean hasOldValue();
	boolean hasNewValue();
	boolean isPriorEvent();

	//convenience
	boolean isIndexedEvent();
}
