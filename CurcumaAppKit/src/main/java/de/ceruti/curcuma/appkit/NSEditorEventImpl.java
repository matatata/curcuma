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
package de.ceruti.curcuma.appkit;

import de.ceruti.curcuma.api.appkit.NSEditorEvent;

public class NSEditorEventImpl implements NSEditorEvent {
	private Type t;
	private Object sender;
	
	public NSEditorEventImpl(Type t, Object sender) {
		this.t = t;
		this.sender = sender;
	}
	
	@Override
	public Type getType() {
		return t;
	}
	
	@Override
	public Object getSender() {
		return sender;
	}

}
