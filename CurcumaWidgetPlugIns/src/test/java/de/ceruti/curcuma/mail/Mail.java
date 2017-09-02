package de.ceruti.curcuma.mail;

import java.util.Date;

public class Mail {
	private String betreff;
	private String body;
	private String rcptTo;
	private Date date;
	private boolean flagged;
	
	public Mail(){
	}

	public String getBetreff() {
		return betreff;
	}

	public void setBetreff(String betreff) {
		this.betreff = betreff;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getRcptTo() {
		return rcptTo;
	}

	public void setRcptTo(String rcptTo) {
		this.rcptTo = rcptTo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean getFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}
	

}
