package de.ceruti.curcuma.keyvalueobserving;

import static de.ceruti.curcuma.api.keyvalueobserving.KVOOption.KeyValueObservingOptionNew;
import static de.ceruti.curcuma.api.keyvalueobserving.KVOOption.KeyValueObservingOptionNone;
import static de.ceruti.curcuma.api.keyvalueobserving.KVOOption.KeyValueObservingOptionOld;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.keyvaluecoding.DefaultErrorHandling;
import de.ceruti.curcuma.keyvaluecoding.KVCMutableArrayProxy;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodeable;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;

public class KeyValueObservingImplTest  {

	@KeyValueObservable
	@KeyValueCodeable
	@DefaultErrorHandling
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
		
		public List mutableArrayValueForKey(String key) {
			return new KVCMutableArrayProxy((KeyValueCoding) this,key);
		}
		

	}

	@Test
	public void testPropertyChangeInRelationIsNotified() {
		Bean bean = new Bean();
		RecordingObserver recordingObserver = new RecordingObserver();
		bean.addObserver(recordingObserver, "next.name", "ctx", KeyValueObservingOptionNew | KeyValueObservingOptionOld);

		bean.setName("Name");
		bean.setNext(new Bean());
		recordingObserver.clear(); // clear prior events
		
		bean.getNext().setName("Tom"); 
		
		assertEquals(1, recordingObserver.getEvts().size());
		KVOEvent event = recordingObserver.getEvts().get(0);
		assertNull( event.getOldValue());
		assertEquals("Tom", event.getNewValue());
	}
	
	@Test
	public void testPropertyChangeInRelationIsNotifiedNoneOption() {
		Bean bean = new Bean();
		RecordingObserver recordingObserver = new RecordingObserver();
		bean.addObserver(recordingObserver, "next.name", "ctx", KeyValueObservingOptionNone);

		bean.setName("Name");
		bean.setNext(new Bean());
		recordingObserver.clear(); // clear prior events
		
		bean.getNext().setName("Tom"); 
		
		assertEquals(1, recordingObserver.getEvts().size());
		KVOEvent event = recordingObserver.getEvts().get(0);
		assertFalse( event.hasOldValue());
		assertFalse(event.hasNewValue());
	}

	
	
}
