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

package de.ceruti.curcuma.keyvaluecoding;



public class KeyPathUtilities {
	public static String [] split(String keyPath) {
		return keyPath.split("\\.");
	}
	
	public static String pathWithoutLastElement(String keyPath) {
		int i=-1;
		if((i=keyPath.lastIndexOf('.')) == -1)
			return null;
		else
			return keyPath.substring(0,i);
	}
	
	public static String keyPathFromElements(String[] components,int startindex,int lastindex)
	{
		StringBuffer buf=new StringBuffer();
		for(int i=startindex;i<=lastindex;i++){
			buf.append(components);
			if(i<=lastindex) buf.append('.');
		}
		return buf.toString();
	}
	
	public static String keyPathFromElements(String[] components)
	{
		return keyPathFromElements(components, 0, components.length-1);
	}
	
	/**
	 * 
	 * @param keyPath
	 * @param head can be null
	 * @param tail can be null
	 * @return true, if the path consist of at least 2 elements
	 */
	public static boolean extractHeadAndTail(String keyPath,StringBuilder head, StringBuilder tail) {
		int dotPos = keyPath.indexOf('.');
		if(dotPos==-1){
			validateKey(keyPath);
			return false;
		}
		String h = keyPath.substring(0, dotPos);
		String t = keyPath.substring(dotPos+1,keyPath.length());
		if(head!=null)
			head.append(h); 
		if(tail!=null)
			tail.append(t);
		return true;
	}
	
	
	public static String extractHead(String keyPath) {
		int dotPos = keyPath.indexOf('.');
		if(dotPos==-1)
			return keyPath;
		
		return keyPath.substring(0, dotPos);
	}
	
	public static String extractTail(String keyPath) {
		int dotPos = keyPath.indexOf('.');
		if(dotPos==-1)
			return keyPath;
		
		return keyPath.substring(dotPos+1,keyPath.length());
	}

	/**
	 * 
	 * @param key
	 * @throws IllegalArgumentException
	 */
	public static void validateKey(String key) throws IllegalArgumentException {
		if(key.contains("."))
			throw new IllegalArgumentException("key '" + key + " must not contain '.'");
		if(key.length()==0)
			throw new IllegalArgumentException("key must not be empty.");
	}
	
	/**
	 * 
	 * @param key
	 * @throws IllegalArgumentException
	 */
	public static void validateKeyPath(String keyPath) throws IllegalArgumentException {
		String [] elems = split(keyPath);
		for(String e : elems) {
			validateKey(e);
		}
	}
	
	/**
	 * Replaces the first Character with the corresponding capital-Letter.
	 * @param key
	 * @return Key
	 */
	public static String capitalizedKey(String key) {
		char c = key.charAt(0);
		c = Character.toUpperCase(c);
		return c + key.substring(1,key.length());
	}
}
