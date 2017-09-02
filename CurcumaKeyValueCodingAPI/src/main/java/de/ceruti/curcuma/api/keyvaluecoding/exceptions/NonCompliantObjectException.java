package de.ceruti.curcuma.api.keyvaluecoding.exceptions;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;


public class NonCompliantObjectException extends KeyValueCodingException {
	private KeyValueCoding owner;
	private Object illegalElement;
	private String keypath;
	
	public NonCompliantObjectException(
			KeyValueCoding owner,
			Object illegalElement, String keypath) {
		super("NonCompliantObject in object " + owner + " at " + keypath + ": " + illegalElement);
		
		this.owner = owner;
		this.illegalElement = illegalElement;
		this.keypath = keypath;
	}

	public KeyValueCoding getOwner() {
		return owner;
	}

	public Object getIllegalElement() {
		return illegalElement;
	}

	public String getKeyPath() {
		return keypath;
	}

}
