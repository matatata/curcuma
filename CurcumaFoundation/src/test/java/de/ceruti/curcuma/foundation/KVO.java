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

import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;
import junit.framework.TestCase;


public class KVO extends TestCase {
	
	public void testSimpleKVO() {
		ArrayList<KVOEvent> recordings = new ArrayList<KVOEvent>();
		
		Model m = new Model();
		KVObserver obs = new RecordingObserver("x",recordings);
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionNone);
		m.setX(new X());
		
		assertEquals(1,recordings.size());
		KVOEvent e = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e.getKind());
		assertFalse(e.isPriorEvent());
		assertFalse(e.hasOldValue());
		assertFalse(e.hasNewValue());
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
		//
		
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionOld);
	
		X oldX,newX;
		oldX = m.getX();
		newX=new X();
		
		m.setX(newX);
		
		assertEquals(1,recordings.size());
		e = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e.getKind());
		assertFalse(e.isPriorEvent());
		assertTrue(e.hasOldValue());
		assertEquals(oldX, e.getOldValue());
		assertFalse(e.hasNewValue());
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
		//
		
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionNew);
		
		oldX = m.getX();
		newX=new X();
		
		m.setX(newX);
		
		assertEquals(1,recordings.size());
		e = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e.getKind());
		assertFalse(e.isPriorEvent());
		assertTrue(e.hasNewValue());
		assertEquals(newX, e.getNewValue());
		assertFalse(e.hasOldValue());
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
		//
		
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew);
		
		oldX = m.getX();
		newX=new X();
		//a
		m.setX(newX);
		assertEquals(1,recordings.size());
		e = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e.getKind());
		assertFalse(e.isPriorEvent());
		assertTrue(e.hasNewValue());
		assertEquals(newX, e.getNewValue());
		assertTrue(e.hasOldValue());
		assertEquals(oldX, e.getOldValue());
		
		//b
		oldX=m.getX();
		m.setX(newX); //this is not new!
		assertEquals(1,recordings.size());
		e = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e.getKind());
		assertFalse(e.isPriorEvent());
		assertTrue(e.hasNewValue());
		assertEquals(newX, e.getNewValue());
		assertTrue(e.hasOldValue());
		assertEquals(oldX, e.getOldValue()); //new and old are equal
		assertEquals(oldX, newX); //old and new are equal
		
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
		
	}
	
	public void testPriorEvents() {
		ArrayList<KVOEvent> recordings = new ArrayList<KVOEvent>();
		
		Model m = new Model();
		KVObserver obs = new RecordingObserver("x",recordings);
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionPrior);
		m.setX(new X());
		
		assertEquals(2,recordings.size());
		KVOEvent e0 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e0.getKind());
		assertTrue(e0.isPriorEvent());
		assertFalse(e0.hasOldValue());
		assertFalse(e0.hasNewValue());
		KVOEvent e1 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e1.getKind());
		assertFalse(e1.isPriorEvent());
		assertFalse(e1.hasOldValue());
		assertFalse(e1.hasNewValue());
		
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
		//
		
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionPrior);
	
		X oldX,newX;
		oldX = m.getX();
		newX=new X();
		
		m.setX(newX);
		
		assertEquals(2,recordings.size());
		e0 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e0.getKind());
		assertTrue(e0.isPriorEvent());
		assertTrue(e0.hasOldValue());
		assertEquals(oldX, e0.getOldValue());
		assertFalse(e0.hasNewValue());
		e1 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e1.getKind());
		assertFalse(e1.isPriorEvent());
		assertTrue(e1.hasOldValue());
		assertEquals(oldX, e1.getOldValue());
		assertFalse(e1.hasNewValue());
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
		//
		
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionPrior);
		
		oldX = m.getX();
		newX=new X();
		
		m.setX(newX);
		
		assertEquals(2,recordings.size());
		e0 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e0.getKind());
		assertTrue(e0.isPriorEvent());
		assertFalse(e0.hasOldValue());
		assertFalse(e0.hasNewValue());
		e1 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e1.getKind());
		assertFalse(e1.isPriorEvent());
		assertTrue(e1.hasNewValue());
		assertEquals(newX, e1.getNewValue());
		assertFalse(e1.hasOldValue());
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
		//
		
		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionPrior);
		
		oldX = m.getX();
		newX=new X();
		//a
		m.setX(newX);
		assertEquals(2,recordings.size());
		e0 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e0.getKind());
		assertTrue(e0.isPriorEvent());
		assertFalse(e0.hasNewValue());
		assertTrue(e0.hasOldValue());
		assertEquals(oldX, e0.getOldValue());
		e1 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e1.getKind());
		assertFalse(e1.isPriorEvent());
		assertTrue(e1.hasNewValue());
		assertEquals(newX, e1.getNewValue());
		assertTrue(e1.hasOldValue());
		assertEquals(oldX, e1.getOldValue());
		
		//b
		oldX=m.getX();
		m.setX(newX); //this is not new!
		assertEquals(2,recordings.size());
		e0 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e0.getKind());
		assertTrue(e0.isPriorEvent());
		assertFalse(e0.hasNewValue());
		assertTrue(e0.hasOldValue());
		assertEquals(oldX, e0.getOldValue());
		e1 = recordings.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e1.getKind());
		assertFalse(e1.isPriorEvent());
		assertTrue(e1.hasNewValue());
		assertEquals(newX, e1.getNewValue());
		assertTrue(e1.hasOldValue());
		assertEquals(oldX, e1.getOldValue()); //new and old are equal
		assertEquals(oldX, newX); //old and new are equal
		
		
		m.removeObserver(obs, "x");
		assertFalse(m.hasObservers());
		
//		// OldNew Compare
//		
//		m.addObserver(obs, "x", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionPrior);
//		
//		oldX = m.getX();
//		newX=new X();
//		//a
//		m.setX(newX);
//		assertEquals(2,recordings.size());
//		e0 = recordings.remove(0);
//		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e0.getKind());
//		assertTrue(e0.isPriorEvent());
//		assertFalse(e0.hasNewValue());
//		assertTrue(e0.hasOldValue());
//		assertEquals(oldX, e0.getOldValue());
//		e1 = recordings.remove(0);
//		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e1.getKind());
//		assertFalse(e1.isPriorEvent());
//		assertTrue(e1.hasNewValue());
//		assertEquals(newX, e1.getNewValue());
//		assertTrue(e1.hasOldValue());
//		assertEquals(oldX, e1.getOldValue());
//		assertFalse(oldX.equals(newX));
//		
//		//b
//		oldX=m.getX();
//		m.setX(newX); //this is not new! So expect only the prior event
//		assertEquals(1,recordings.size());
//		e0 = recordings.remove(0);
//		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, e0.getKind());
//		assertTrue(e0.isPriorEvent());
//		assertFalse(e0.hasNewValue());
//		assertTrue(e0.hasOldValue());
//		assertEquals(oldX, e0.getOldValue());
//		m.removeObserver(obs, "x");
//		assertFalse(m.hasObservers());
	}
	
	
	public void testForwards() {
		ArrayList<KVOEvent> recordingsXYZ = new ArrayList<KVOEvent>();
		ArrayList<KVOEvent> recordingsXY = new ArrayList<KVOEvent>();
		
		Model m = new Model();
		X x = new X();
		
		KVObserver xyzObs = new RecordingObserver("x.y.z",recordingsXYZ);
		KVObserver xyObs = new RecordingObserver("x.y",recordingsXY);
		
		m.addObserver(xyzObs, "x.y.z", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew);
		m.addObserver(xyObs, "x.y", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew);
		m.setX(x);
		assertEquals(1,recordingsXYZ.size());
		KVOEvent eXYZ = recordingsXYZ.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, eXYZ.getKind());
		assertNotSame(eXYZ.getOldValue(), eXYZ.getNewValue());
		assertTrue(eXYZ.getOldValue() instanceof Z);
		assertTrue(eXYZ.getNewValue() instanceof Z);
		
		KVOEvent eXY = recordingsXY.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, eXY.getKind());
		assertNotSame(eXY.getOldValue(), eXY.getNewValue());
		assertTrue(eXY.getOldValue() instanceof Y);
		assertTrue(eXY.getNewValue() instanceof Y);
		
		Y oldY = x.getY();
		Y y = new Y();
		x.setY(y);
		
		eXYZ = recordingsXYZ.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, eXYZ.getKind());
		assertNotSame(eXYZ.getOldValue(), eXYZ.getNewValue());
		assertTrue(eXYZ.getOldValue() instanceof Z);
		assertTrue(eXYZ.getNewValue() instanceof Z);
		
		eXY = recordingsXY.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, eXY.getKind());
		assertEquals(y, eXY.getNewValue());
		assertEquals(oldY, eXY.getOldValue());
		
		Z oldZ = x.getY().getZ();

		Z z = new Z();
		z.setName("Zeta");
		y.setZ(z);
		eXYZ = recordingsXYZ.remove(0);
		assertEquals(KVOEvent.KEY_VALUE_CHANGE_SETTING, eXYZ.getKind());
		assertEquals(z,eXYZ.getNewValue());
		assertEquals(oldZ, eXYZ.getOldValue());
		
		assertEquals(0,recordingsXY.size());
	}
}
