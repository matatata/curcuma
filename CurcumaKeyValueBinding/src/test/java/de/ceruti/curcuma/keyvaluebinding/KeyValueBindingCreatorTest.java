package de.ceruti.curcuma.keyvaluebinding;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.keyvaluecoding.DefaultErrorHandling;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;
import de.ceruti.curcuma.keyvalueobserving.KeyValueObservable;

public class KeyValueBindingCreatorTest {

	@KeyValueObservable
	@KeyValueCodeable
	@DefaultErrorHandling
	@KeyValueBindingCreator
	public static class Bean {
		private String name;
		private Bean next;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Bean getNext() {
			return next;
		}

		public void setNext(Bean next) {
			this.next = next;
		}
		
		
		public Bean() {
			exposeBinding("name",String.class);
		}
		
		public List mutableArrayValueForKey(String key) {
			return new KVCMutableArrayProxy((KeyValueCoding) this,key);
		}

	}
	
	@Test
	public void testBinding() {
		Bean bean1 = new Bean();
		Bean bean2 = new Bean();
		//bind bean2's name to bean1's name
		bean2.bind("name", bean1, "name", new DefaultBindingOptions());
		
		bean1.setName("Tom");
		assertEquals("Tom",bean2.getName());
		
		
	}

}
