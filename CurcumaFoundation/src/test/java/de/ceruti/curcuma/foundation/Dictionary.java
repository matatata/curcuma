package de.ceruti.curcuma.foundation;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import de.ceruti.curcuma.api.foundation.NSDictionary;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.foundation.Logging;
import de.ceruti.curcuma.foundation.NSFactory;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodingWrapper;
import de.ceruti.curcuma.keyvalueobserving.util.LoggingObserver;

public class Dictionary extends TestCase {
	
	static {
		Logging.init();
	}
	
	public void test01() {
		NSDictionary dict = NSFactory.mutableDictionary();
		dict.addObserver(new LoggingObserver("name"), "name", null, KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
		dict.addObserver(new LoggingObserver("userinfo.personal.name"), "userinfo.personal.name", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew);
		dict.addObserver(new LoggingObserver("userinfo.personal"), "userinfo.personal", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew);
			
		assertTrue(dict.isMutable());
		dict.setValueForKey("Matteo","name");
		assertEquals("Matteo", dict.get("name"));
		dict.clear();
		assertEquals(null, dict.getValueForKey("name"));
		
		
		assertEquals(null,dict.getValueForKeyPath("userinfo.personal.name"));
		
		dict.setValueForKeyPath("Matteo","userinfo.personal.name");
		
		assertEquals("Matteo", dict.getValueForKeyPath("userinfo.personal.name"));
	
		dict.clear();
		assertEquals(null, dict.getValueForKey("name"));
		assertEquals(null, dict.getValueForKeyPath("userinfo.personal.name"));
		
		
		
		Map<String,Object> pojo = new HashMap<String, Object>();
		
		KeyValueCoding wrapped = KeyValueCodingWrapper.wrapIfNecessary(pojo);
		
		wrapped.setValueForKeyPath("Matteo","userinfo.personal.name");
		assertEquals("Matteo", wrapped.getValueForKeyPath("userinfo.personal.name"));
		
		
	}
}
