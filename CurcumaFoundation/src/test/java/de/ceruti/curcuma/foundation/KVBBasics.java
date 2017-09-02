package de.ceruti.curcuma.foundation;

import junit.framework.TestCase;

public class KVBBasics extends TestCase {

	
	public static class KVBEmployee extends Employee {
		public KVBEmployee(){
			exposeBinding("salary",Double.class);
		}
	}
	
	public void test0(){
		KVBEmployee emplMaster = new KVBEmployee();
		KVBEmployee emplMirror = new KVBEmployee();

		emplMaster.setSalary(100.0);
		emplMirror.setSalary(0.0);
		
		emplMirror.bind("salary",emplMaster,"salary", null);  
		assertEquals(100.0, emplMaster.getSalary());
		assertEquals(emplMaster.getSalary(), emplMirror.getSalary());
		
		emplMaster.setSalary(1000.0);
		assertEquals(1000.0, emplMaster.getSalary());
		assertEquals(emplMaster.getSalary(), emplMirror.getSalary());
		
		
	}
	
}
