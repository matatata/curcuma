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

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.keyvalueobserving.util.LoggingObserver;
import junit.framework.TestCase;


public class KVCTest extends TestCase
{

	public void test1() {
		
		
		Mailbox mbox = new Mailbox();
		mbox.addObserver(new LoggingObserver("mails"), "mails", null, KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionNone);
		mbox.addObserver(new LoggingObserver("name"),"name",null,KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew);
		mbox.addObserver(new LoggingObserver("mails.subject"),"mails.subject",null,KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew);
		
		List to = mbox.mutableArrayValueForKey("mails");
		
		
		to.add(new Email("curcuma@ceruti.de","matteo@ceruti.de","Hi there!", "Just wanted to let you know that I'm back in the U.S."));
		to.add(new Email("matteo@ceruti.de","curcuma@ceruti.de","Re:Hi there!", "Welcome back! CU tomorrow." ));
				
		List<String> obj = (List<String>) mbox.getValueForKeyPath("mails.from");
		ArrayList<String> actual = new ArrayList<String>();
		for(Email m : mbox.getMails()){
			actual.add(m.getFrom());
		}
		assertEquals(obj, actual);
		

		
		to.add(new Email("curcuma@ceruti.de","matteo@ceruti.de","Hunger!", "How about lunch at 5pm"));
		
		assertEquals(mbox.getMails().size(), 3);
		
		((KeyValueCoding)to).setValueForKey("DEL", null , "subject");
		
		
		for(Email m : mbox.getMails()){
			assertEquals("DEL",m.getSubject());
		}
		
		
		mbox.setValueForKeyPath(new Long(System.currentTimeMillis()), "mails.header.receivedTimestamp");
	
	
		mbox.setName("INBOX");
	}
	
}
