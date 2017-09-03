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
package de.ceruti.curcuma.keyvalueobserving.util;

import java.io.PrintStream;

import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;

public class LoggingObserver implements KVObserver
{
	private String name;
	private PrintStream writer;
	

	public LoggingObserver(String name){
		this(name,System.out);
	}
	
	public LoggingObserver(String name,PrintStream writer)
	{
		this.name = name;
		this.writer = writer;
	}
	
	
	@Override
	public void observeValue(String keypath, KeyValueObserving object, KVOEvent change, Object context) {
		writer.println(this + ": observeValue " + keypath + " of " + object + " context=" + context + " change=" + change );
	}
	
	@Override
	public String toString() {
		return "LoggingObserver(" + name + ")";
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PrintStream getWriter() {
		return writer;
	}

	public void setWriter(PrintStream writer) {
		this.writer = writer;
	}
	
}