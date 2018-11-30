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
		
		public Bean(String name) {
			this.name=name;
		}

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
		
		@Override
		public String toString() {
			return name;
		}
		

	}

	@Test
	public void testPropertyChangeInRelationIsNotified() {
		Bean bean = new Bean("bean");
		RecordingObserver recordingObserver = new RecordingObserver();
		bean.addObserver(recordingObserver, "next.name", "ctx", KeyValueObservingOptionNew | KeyValueObservingOptionOld);

		bean.setName("Name");
		bean.setNext(new Bean("next"));
		recordingObserver.clear(); // clear prior events
		
		bean.getNext().setName("Tom"); 
		
		assertEquals(1, recordingObserver.getEvts().size());
		KVOEvent event = recordingObserver.getEvts().get(0);
		assertEquals("next", event.getOldValue());
		assertEquals("Tom", event.getNewValue());
	}
	
	@Test
	public void testPropertyChangeIn2RelationIsNotified() {
		Bean bean = new Bean("bean");
		RecordingObserver recordingObserver = new RecordingObserver();
		bean.addObserver(recordingObserver, "next.next.name", "ctx", KeyValueObservingOptionNew | KeyValueObservingOptionOld);

		
		Bean nextBean = new Bean("next");
		nextBean.setNext(new Bean("next.next"));
		bean.setNext(nextBean);
		
		assertEquals(1, recordingObserver.getEvts().size());
		KVOEvent event = recordingObserver.getEvts().get(0);
		assertNull(event.getOldValue());
		assertEquals("next.next", event.getNewValue());
		
		recordingObserver.clear(); // clear prior events
		bean.getNext().getNext().setName("Tom"); 
		
		assertEquals(1, recordingObserver.getEvts().size());
		event = recordingObserver.getEvts().get(0);
		assertEquals("next.next", event.getOldValue());
		assertEquals("Tom", event.getNewValue());
		
		recordingObserver.clear();
		
		bean.getNext().setNext(null);
		
		assertEquals(1, recordingObserver.getEvts().size());
		event = recordingObserver.getEvts().get(0);
		assertEquals("Tom", event.getOldValue());
		assertNull( event.getNewValue());
		
	}
	
	@Test
	public void testPropertyChangeInRelationIsNotifiedNoneOption() {
		Bean bean = new Bean("bean");
		RecordingObserver recordingObserver = new RecordingObserver();
		bean.addObserver(recordingObserver, "next.name", "ctx", KeyValueObservingOptionNone);

		bean.setName("Name");
		bean.setNext(new Bean("next"));
		recordingObserver.clear(); // clear prior events
		
		bean.getNext().setName("Tom"); 
		
		assertEquals(1, recordingObserver.getEvts().size());
		KVOEvent event = recordingObserver.getEvts().get(0);
		assertFalse( event.hasOldValue());
		assertFalse(event.hasNewValue());
	}

	
	
}
