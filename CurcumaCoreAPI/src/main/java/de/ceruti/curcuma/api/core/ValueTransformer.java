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

package de.ceruti.curcuma.api.core;

import de.ceruti.curcuma.api.core.exceptions.ConversionException;

public interface ValueTransformer {



	// Getting information about a transformer
	boolean allowsReverseTransformation();
	
	Object transformedValue(Object val) throws ConversionException;

	Object reverseTransformedValue(Object val) throws ConversionException;
	
	
	
	ValueTransformer Identity = new ValueTransformer() {
		
		@Override
		public Object transformedValue(Object val) throws ConversionException {
			return val;
		}
		
		@Override
		public Object reverseTransformedValue(Object val)
				throws ConversionException {
			return val;
		}
		
		@Override
		public boolean allowsReverseTransformation() {
			return true;
		}
	};

}