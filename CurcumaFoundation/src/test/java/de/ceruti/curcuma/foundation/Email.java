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

import java.util.List;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.keyvaluecoding.DefaultErrorHandling;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;

@KeyValueCodeable
@DefaultErrorHandling
public class Email {
	private String from;
	private String to;
	private String subject;
	private String message;
	private int size;
	
	private Header header = new Header(0);
	
	public List mutableArrayValueForKey(String key) {
		return new KVCMutableArrayProxy((KeyValueCoding) this,key);
	}
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
	
	/**
	 * Hat kein KVC-Support
	 */
	public static class Header {
		private int receivedTimestamp;

		public Header(int ts){
			setReceivedTimestamp(ts);
		}
		
		public int getReceivedTimestamp() {
			return receivedTimestamp;
		}

		public void setReceivedTimestamp(int receivedTimestamp) {
			this.receivedTimestamp = receivedTimestamp;
		}
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Email(String from,String to,String subj,String message){
		setFrom(from);
		setTo(to);
		setSubject(subj);
		setMessage(message);
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Override
	public String toString() {
		return "Email[" + getFrom() + ", " + getTo() + "]";
	}

	


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
