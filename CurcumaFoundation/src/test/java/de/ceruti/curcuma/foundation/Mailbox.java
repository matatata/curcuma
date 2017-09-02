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

import java.util.ArrayList;
import java.util.List;

import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;


public class Mailbox extends NSObjectImpl  {
	private List<Email> mails = new ArrayList<Email>();

	private String name;
	
	public String getName() {
		return name;
	}

	@PostKVONotifications
	public void setName(String name) {
		this.name = name;
	}

	public List<Email> getMails() {
		return mails;
	}

	public void setMails(List<Email> mails) {
		this.mails = mails;
	}
	
}
