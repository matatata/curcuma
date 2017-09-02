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

package de.ceruti.curcuma.foundation;

import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;

public class Z extends NSObjectImpl  {
	public String getName() {
		return name;
	}

	@PostKVONotifications
	public void setName(String name) {
		this.name = name;
	}

	private String name = "Untitled";
	
	@Override
	public String toString()
	{
		return "Z[" + name + "]";
	}
	
	public Object validateName(Object name) throws ValidationException {
//		if(name.equals("Satan"))
//			throw new ValidationException("'Satan' cannot be your name!");
		return name;
	}
}