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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSEditable;
import de.ceruti.curcuma.api.appkit.NSEditor;



aspect NSEditableAspect {
	private static Logger logger = LogManager.getLogger(NSEditableAspect.class);
	
	public static interface NSEditableSupport extends NSEditable {
	}
	
	declare parents: @NSDefaultEditable * implements NSEditableSupport;
	
	private ArrayList<NSEditor> NSEditableSupport.editors = new ArrayList<NSEditor>();
	
	public void NSEditableSupport.objectDidBeginEditing(NSEditor editor) {
		logger.debug("objectDidBeginEditing(" + editor + ")");
		editors.add(editor);
	}

	public void NSEditableSupport.objectDidEndEditing(NSEditor editor) {
		logger.debug("objectDidEndEditing(" + editor + ")");
		editors.remove(editor);
	}
	
	public boolean NSEditableSupport.hasEditors() {
		return editors.size() > 0;
	}
	
	public void NSEditableSupport.discardEditing(Object sender)
	{
		List<NSEditor> copy = (List<NSEditor>) editors.clone();
		
		for(Iterator<NSEditor> it=copy.iterator();it.hasNext();)
			it.next().discardEditing(sender);
	}
	
	public boolean NSEditableSupport.commitEditing(Object sender)
	{
		boolean ret = true;
		
		List<NSEditor> copy = (List<NSEditor>) editors.clone();
		
		for(Iterator<NSEditor> it=copy.iterator();it.hasNext();)
			ret &= it.next().commitEditing(sender);
	
		return ret;
	}
	
}
