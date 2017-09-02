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

package de.ceruti.curcuma.core;

import de.ceruti.curcuma.api.core.Value;

final class CCString extends AbstractValue {
	private String value;
	private double doubleVal=0.0;
	private long longVal=0L;;
	private boolean numbersInitialized = false;
	
	public CCString(String s){
		this.value=s;
	}
	
	private void initNumbers(){
		NumberFormatException parseDoubleFailed = null;
		NumberFormatException decodeLongFailed = null;
		if (value.equals("true"))
		{
			doubleVal = 1.0;
			longVal = 1L;
			numbersInitialized = true;
			return;
		}
		else if (value.equals("false"))
		{
			doubleVal = 0.0;
			longVal = 0L;
			numbersInitialized = true;
			return;
		}
		
		try{
			doubleVal = Double.parseDouble(value);
			longVal = (long)doubleVal;
		}catch(NumberFormatException e){
			parseDoubleFailed = e;
		}
		try{
			longVal = Long.decode(value).longValue();
		}catch(NumberFormatException e){
			decodeLongFailed = e;
		}
		finally {
			numbersInitialized = true;
			if((parseDoubleFailed!=null) && (decodeLongFailed!=null)){
				throw new NumberFormatException("For input string: \"" + value + "\"");
			}
		}
	}
	
	@Override
	public double doubleValue() {
		if(!numbersInitialized)
			initNumbers();
		
		return doubleVal;
	}

	@Override
	public long longValue() {
		if(!numbersInitialized)
			initNumbers();
		return longVal;
	}
	
	@Override
	public String stringValue() {
		return value;
	}
	
	@Override
	public boolean boolValue() {
		if(!numbersInitialized)
			initNumbers();
		return super.boolValue();
	}

	@Override
	public Object objectValue() {
		return value;
	}
	
	@Override
	protected boolean equals(Value o) {
		return value.equals(((CCString)o).value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public char charValue() {
		return value.length() == 1 ? value.charAt(0) : super.charValue();
	}
}
