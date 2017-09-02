package de.ceruti.curcuma.keyvaluecoding;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.NonCompliantAccessorFoundException;

public class KeyValueCodingTest {
	
	@KeyValueCodeable
	@DefaultErrorHandling
	public static class Bean{
		private Bean next;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public void setNext(Bean next) {
			this.next = next;
		}
		
		public Bean getNext() {
			return next;
		}
		
		public List mutableArrayValueForKey(String key) {
			return new KVCMutableArrayProxy((KeyValueCoding) this,key);
		}
		
	}
	
	@Test
	public void testGetAndSetValueForKeypath() {
		Bean bean = new Bean();
		bean.setValueForKey("Matteo", "name");
		assertEquals("Matteo", bean.getName());
		assertEquals("Matteo", bean.getValueForKey("name"));
		bean.setValueForKey(new Bean(), "next");
		bean.setValueForKeyPath("Mike", "next.name");
		assertEquals("Mike", bean.getNext().getName());
		assertEquals("Mike", bean.getValueForKeyPath("next.name"));
	}
	
	@Test(expected=NonCompliantAccessorFoundException.class)
	public void testUndefinedKey() throws Exception {
		Bean bean=new Bean();
		bean.setValueForKey(666, "undefined");
	}

}
