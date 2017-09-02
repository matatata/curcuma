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

/**
 * 
 */
package de.ceruti.curcuma.foundation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.keyvaluecoding.DefaultErrorHandling;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservable;



@KeyValueCodeable
@DefaultErrorHandling
@KeyValueObservable
public class ImageURLsModel implements ImageURLsModelInterface {
	protected List<URL> imageURLs = new ArrayList<URL>();
	
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy((KeyValueCoding) this,key);
	}
	
	@Override
	public void insertObjectInImageURLsAtIndex(Object obj,int index) {
		imageURLs.add(index, (URL) obj );
	}
	
	@Override
	public Object removeObjectFromImageURLsAtIndex(int index) {
		Object ret = imageURLs.remove(index);
		return ret;
	}
	
	@Override
	public Object replaceObjectInImageURLsWithObjectAtIndex(Object obj,int index) {
		Object ret = imageURLs.set(index, (URL) obj );
		return ret;
	}
	
	@Override
	public void getImageURLsInRange(Object[] o,Range r) {
		int j=0;
		for(int i=r.getLocation(); i<r.maxRange();i++){
			o[j++] = imageURLs.get(i);
		}
	}
	
	@Override
	public int countOfImageURLs() {
		return imageURLs.size();
	}
	
	@Override
	public Object objectInImageURLsAtIndex(int index) {
		return imageURLs.get(index);
	}
	

	
}