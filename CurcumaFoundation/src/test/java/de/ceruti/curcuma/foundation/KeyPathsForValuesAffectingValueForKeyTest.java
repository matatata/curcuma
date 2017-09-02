package de.ceruti.curcuma.foundation;

import java.util.Set;

import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;
import junit.framework.TestCase;

public class KeyPathsForValuesAffectingValueForKeyTest extends TestCase {
	
	
	
	public static class Class1 extends NSObjectImpl {
	
		private int a;
		private int b;
		
		public int getSum() {
			return getA() + getB();
		}
		
		@PostKVONotifications
		public void setSum(int c) {
			
		}
		
		/*
		 * c wird getriggert, wenn a oder b getriggert werden
		 */
		public static Set<String> keyPathsForValuesAffectingValueForKey(String key) {
			
			Set<String> ret = NSObjectImpl.keyPathsForValuesAffectingValueForKey(key);
			
			if(key.equals("sum"))
			{
				ret.add("a");
				ret.add("b");
			}
			
			return ret;
		}


		public int getA() {
			return a;
		}


		@PostKVONotifications
		public void setA(int a) {
			this.a = a;
		}


		public int getB() {
			return b;
		}


		@PostKVONotifications
		public void setB(int b) {
			this.b = b;
		}
	}
	
	public static class Class2 extends Class1 {
		
		public int getMult() {
			return getSum()*getSum();
		}
		
		@PostKVONotifications
		public void setMult(int d) {
		}
		
		/*
		 * "mult" wird getriggert, wenn "sum" getriggert werden.
		 * Da Class2 von Class1 erbt muss im Resultat "mult" auch getriggert werden, wenn a,b oder "sum" getriggert werden.
		 */
		public static Set<String> keyPathsForValuesAffectingValueForKey(String key) {
			Set<String> ret = Class1.keyPathsForValuesAffectingValueForKey(key);
			
			if(key.equals("mult"))
			{
				ret.add("sum");
			}
			
			
			return ret;
		}
	}
	
	
	
	public void testKeyPathsForValuesAffectingValueForKey() {
		
		KVObserver cobs = new RecordingObserver("sum:sum");
		KVObserver dobs = new RecordingObserver("sum:mult");
		Class2 c = new Class2();
		c.addObserver(cobs, "sum", null, KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
		c.addObserver(dobs, "mult", null, KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
		
		c.setA(1);
		c.setB(2);
		c.setSum(0);
		assertEquals("{KVOSettingEvent from 0 to 1}\r\n" + 
				"{KVOSettingEvent from 1 to 3}\r\n" + 
				"{KVOSettingEvent from 3 to 3}\r\n", cobs.toString());
		
		c.setMult(0);
		assertEquals("{KVOSettingEvent from 0 to 1}\r\n" + 
				"{KVOSettingEvent from 1 to 9}\r\n" + 
				"{KVOSettingEvent from 9 to 9}\r\n" + 
				"{KVOSettingEvent from 9 to 9}\r\n", dobs.toString());
		
		
		c.removeObserver(cobs, "sum");
		c.removeObserver(dobs, "mult");
		


		
	}

}
