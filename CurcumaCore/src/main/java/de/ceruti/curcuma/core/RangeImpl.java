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

import de.ceruti.curcuma.api.core.Range;



class RangeImpl implements Range {
	private int location;
	private int length;
	
	public final static Range EmptyRange = new RangeImpl(0,0);
	
	
	
	protected RangeImpl(int loc){
		set(loc,1);
	}
	
	protected RangeImpl(Range s){
		set(s);
	}
	
	protected RangeImpl(int location,int length){
		set(location,length);
	}
	
	protected void setLocation(int location) {
		this.location = location;
	}

	@Override
	public int getLocation() {
		return location;
	}

	protected void setLength(int length) {
		this.length = length;
	}

	@Override
	public int getLength() {
		return length;
	}
	
	
	@Override
	public boolean isEmpty(){
		return getLength()==0;
	}


	protected void set(Range src){
		if(src!=null){
			setLocation(src.getLocation());
			setLength(src.getLength());
		}
	}
	
	protected void set(int location,int length){
			if(location<0 || length < 0)
				throw new IllegalArgumentException();
			this.setLocation(location);
			this.setLength(length);
		}

	
	@Override
	public Range copy(){
		return new RangeImpl(getLocation(),getLength());
	}
	
	@Override
	public Range rangeByIntersectingRange(Range r){
		int start = Math.max(getLocation(), r.getLocation());
		int newEnd = Math.min(r.maxRange(),maxRange());
		int newLength = newEnd - start;
		if(newLength<=0)
			return EmptyRange;
		return new RangeImpl(start,newLength);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + length;
		result = prime * result + location;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Range other = (Range) obj;
		if (length != other.getLength())
			return false;
		if (location != other.getLocation())
			return false;
		return true;
	}

	@Override
	public Range rangeByUnioningRange(Range r){
		int start = Math.min(getLocation(), r.getLocation());
		int newEnd = Math.max(r.maxRange(),maxRange());
		int newLength = newEnd - start;
		return new RangeImpl(start,newLength);
	}
	
	private static Range [] emptyRange = new Range[]{};
	
	@Override
	public Range[] rangesBySubtractingRange(Range r){

		if(rangeByIntersectingRange(r).isEmpty())
			return new Range[]{new RangeImpl(this)};
		
		Range right = null;
		Range left = null;
		
		if(this.getLocation() <= r.getLocation()){
			int newStart = this.getLocation();
			int newEnd = r.getLocation();
			int newLen = newEnd - newStart;
			if(newLen>0)
				left = new RangeImpl(newStart,newLen);
		}
		
		if(this.maxRange() >= r.maxRange()){
			
			int newStart = r.maxRange();
			int newEnd = this.maxRange();
			int newLen = newEnd - newStart;
			if(newLen>0)
				right = new RangeImpl(newStart,newLen);
		}
		
		if(left!=null && right==null)
			return new Range[]{left};
		else if(left==null && right!=null)
			return new Range[]{right};
		else if(left!=null && right!=null)
			return new Range[]{left,right};
		
		
		return emptyRange;
	}
	
	public static void main(String[] args) {
		Range a = new RangeImpl(4,10);
		Range b = new RangeImpl(5,12);
		
		Range i = a.rangeByIntersectingRange(b);
		System.out.println(a + " intersect " + b + "=" + i);
		
		Range u = a.rangeByUnioningRange(b);
		System.out.println(a + " union " + b + "=" + u);
		
		Range l[] = b.rangesBySubtractingRange(a);
		System.out.println(b + " minus " + a + "=");
		for(Range r : l) System.out.println(r);
		
		b = new RangeImpl(1,10);
		l= b.rangesBySubtractingRange(a);
		System.out.println(b + " minus " + a + "=");
		for(Range r : l) System.out.println(r);
	}
	
	
	@Override
	public int maxRange(){
		return getLocation() + getLength();
	}
	
	@Override
	public String toString(){
		return "[" + getLocation() + ", " + maxRange() + "[";
	}
	
	@Override
	public boolean containsLocation(int location){
		return location >= this.getLocation() && location < maxRange();
	}
	
	@Override
	public boolean containsRange(Range r){
		return r.getLocation() >= getLocation() && r.maxRange() <= maxRange();
	}
	
}
