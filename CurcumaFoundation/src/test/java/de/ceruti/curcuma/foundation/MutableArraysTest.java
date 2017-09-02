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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;
import junit.framework.TestCase;

public class MutableArraysTest extends TestCase {

	public void test1() throws MalformedURLException {

		ImageURLsModel testObj = new ImageURLsModel();
		RecordingObserver recordingObserver = new RecordingObserver();
		testObj.addObserver(recordingObserver, "imageURLs", null,
				KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);

		List mutableArray = testObj.mutableArrayValueForKeyPath("imageURLs");
		String args[] = new String[] { "http://www.ceruti.org/", "http://www.apple.com" };

		URL[] us = new URL[args.length];
		for (int i = 0; i < args.length; i++) {
			us[i] = new URL(args[i]);
		}
		mutableArray.addAll(Arrays.asList(us));

		for (int i = 0; i < args.length; i++) {
			assertEquals(new URL(args[i]), mutableArray.get(i));
		}

		mutableArray.set(1, new URL("ftp://ftp.host.com"));

		mutableArray.clear();

		assertTrue(testObj.imageURLs.isEmpty());
		
		assertEquals("[type=ARRAYELEM_INSERTION, indexes=([0, 1[) newValues=[http://www.ceruti.org/]]\r\n" + 
				"[type=ARRAYELEM_INSERTION, indexes=([1, 2[) newValues=[http://www.apple.com]]\r\n" + 
				"[type=ARRAYELEM_REPLACEMENT, indexes=([1, 2[) oldValues=[http://www.apple.com] newValues=[ftp://ftp.host.com]]\r\n" + 
				"[type=ARRAYELEM_REMOVAL, indexes=([0, 1[) oldValues=[http://www.ceruti.org/]]\r\n" + 
				"[type=ARRAYELEM_REMOVAL, indexes=([0, 1[) oldValues=[ftp://ftp.host.com]]\r\n" , recordingObserver.toString());
		
		
		
	}
}
