package de.ceruti.curcuma.foundation;

import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;
import junit.framework.TestCase;

public class ObserverRemovalTest extends TestCase {

	public void testRemoveObserver() throws Exception{
		X x1 = new X();
		Y y1 = new Y();
		Z z1 = new Z();
		X x2 = new X();
		Y y2 = new Y();
		Z z2 = new Z();

		x1.setY(y1);
		y1.setZ(z1);
		z1.setName("Z1");

		x2.setY(y2);
		y2.setZ(z2);
		z2.setName("Z2");


		RecordingObserver xyzname = new RecordingObserver("y.z.name");
		RecordingObserver desc = new RecordingObserver("desc");

		x1.addObserver(xyzname, "y.z.name", null, KVOOption.KeyValueObservingOptionOldNew);
		x1.addObserver(desc, "desc", null, KVOOption.KeyValueObservingOptionOldNew);

		x1.setY(y2);
		
		assertEquals("{KVOSettingEvent from Z1 to Z2}\r\n", xyzname.toString());
		assertEquals("{KVOSettingEvent from Y[Z[Z1]] to Y[Z[Z2]]}\r\n", desc.toString());


		x1.removeObserver(desc, "desc");
		x1.removeObserver(xyzname, "y.z.name");
		assertFalse(x1.hasObservers());
		assertFalse(y1.hasObservers());
		assertFalse(z1.hasObservers());
		assertFalse(x2.hasObservers());
		assertFalse(y2.hasObservers());
		assertFalse(z2.hasObservers());


		xyzname.clear();
		desc.clear();
		
		x1.addObserver(desc, "desc", null, KVOOption.KeyValueObservingOptionOldNew);

		x1.setY(y1);
		
		assertTrue(xyzname.getEvts().isEmpty());
		assertEquals("{KVOSettingEvent from Y[Z[Z2]] to Y[Z[Z1]]}\r\n", desc.toString());

		x1.removeObserver(desc, "desc");

		assertFalse(x1.hasObservers());
		assertFalse(y1.hasObservers());
		assertFalse(z1.hasObservers());
		assertFalse(x2.hasObservers());
		assertFalse(y2.hasObservers());
		assertFalse(z2.hasObservers());


	}

}
