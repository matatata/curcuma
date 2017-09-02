package de.ceruti.curcuma.mail;

import java.util.ArrayList;
import java.util.Collection;

public class Mailbox {
	private String name;
	
	private Collection<Mail> mails = new ArrayList<Mail>();
	
	public Collection<Mail> getMails() {
		return mails;
	}
	public void setMails(Collection<Mail> mails) {
		this.mails = mails;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
