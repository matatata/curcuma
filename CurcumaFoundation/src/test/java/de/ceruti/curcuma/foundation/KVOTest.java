package de.ceruti.curcuma.foundation;

import java.util.ArrayList;

import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;
import junit.framework.TestCase;

public class KVOTest extends TestCase {
	
	ArrayList<KVOEvent> recordings = new ArrayList<KVOEvent>();
	
	
	Model m = new Model();
	
	public KVOTest() {
	
		m.getX().getListOfStrings().add("Tom");
		m.getX().getListOfStrings().add("Frank");
		m.getX().getListOfStrings().add("Peter");
		m.getX().getY().getListOfTs().add(new T(2));
		m.getX().getY().getListOfTs().add(new T(3));
		m.getX().getY().getListOfTs().add(new T(5));
		
		
		m.addObserver(new RecordingObserver("x",recordings), "x", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("x.y",recordings), "x.y", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("x.y.z",recordings), "x.y.z", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("x.y.z.name",recordings), "x.y.z.name", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("x.listOfStrings",recordings), "x.listOfStrings", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("x.y.listOfTs",recordings), "x.y.listOfTs", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("x.y.listOfTs.i",recordings), "x.y.listOfTs.i", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("x.y.listOfTs.i No 2",recordings), "x.y.listOfTs.i", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("employee",recordings), "employee", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
		m.addObserver(new RecordingObserver("employee.name",recordings), "employee.name", null, KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew /*| KVOOption.KeyValueObservingOptionPrior*/);
	
	
	}
	
	private X creatAnX(){
		X x = new X();
		x.getListOfStrings().add("James");
		x.getListOfStrings().add("Norah");
		x.getY().getZ().setName("John");
		return x;
	}
	
	
	public void test01() {
		
		recordings.clear();
		
		X x = creatAnX();
		
		m.setX(x); //expecting all Observers to Fire
		
		
		assertEquals("{KVOSettingEvent from X[Y[Z[Untitled]]] to X[Y[Z[John]]]}\r\n" + 
				"{KVOSettingEvent from [Tom, Frank, Peter] to [James, Norah]}\r\n" + 
				"{KVOSettingEvent from Untitled to John}\r\n" + 
				"{KVOSettingEvent from Z[Untitled] to Z[John]}\r\n" + 
				"{KVOSettingEvent from Y[Z[Untitled]] to Y[Z[John]]}\r\n" + 
				"{KVOSettingEvent from [T[2], T[3], T[5]] to []}\r\n" + 
				"{KVOSettingEvent from [2, 3, 5] to []}\r\n" + 
				"{KVOSettingEvent from [2, 3, 5] to []}\r\n", RecordingObserver.recordingToString(recordings));
		
		recordings.clear();
	}
	
	public void test02() {
		m.setValueForKeyPath("Matteo", "x.y.z.name");
		assertEquals("Matteo",m.getX().getY().getZ().getName());
	}
	
	public void test03() {
		
		// mutableArray-Changes
		
		m.mutableArrayValueForKeyPath("x.y.listOfTs").add(new T(111));
		
		assertEquals("[type=ARRAYELEM_INSERTION, indexes=([3, 4[) newValues=[T[111]]]\r\n" + 
				"[type=ARRAYELEM_INSERTION, indexes=([3, 4[) newValues=[111]]\r\n" + 
				"[type=ARRAYELEM_INSERTION, indexes=([3, 4[) newValues=[111]]\r\n", RecordingObserver.recordingToString(recordings));
		
	}
	
	
	
	
	

}
