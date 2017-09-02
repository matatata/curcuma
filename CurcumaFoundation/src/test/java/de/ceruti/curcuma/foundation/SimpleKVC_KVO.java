package de.ceruti.curcuma.foundation;

import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.keyvalueobserving.util.LoggingObserver;
import junit.framework.TestCase;

public class SimpleKVC_KVO extends TestCase {
	
	
	public void test_getters_and_setters() {
		Employee employee = new Employee();
		employee.addObserver(new LoggingObserver("desc"), "description", null, KVOOption.KeyValueObservingOptionNew);
		
		
		employee.setName("Matteo Ceruti");
		assertEquals(employee.getName(), employee.getValueForKey("name"));
		employee.setValueForKey("John Doe", "name");
		assertEquals("John Doe", employee.getName());
		
		employee.setSalary(40000.0);
		assertEquals(employee.getSalary(), employee.getValueForKey("salary"));
		employee.setValueForKey(99000.0, "salary");
		assertEquals(99000.0, employee.getSalary());
		
		Company company = new Company();
		company.addObserver(new LoggingObserver("ceo.description"), "ceo.description", null, KVOOption.KeyValueObservingOptionNew);
		
		Employee ceo = new Employee();
		ceo.setName("Steve Jobs");
		ceo.setSalary(1000000.0);
		company.getEmployees().add(employee);
		company.setCeo(ceo);
		
		assertEquals("Steve Jobs", company.getValueForKeyPath("ceo.name"));
		assertEquals(1000000.0, company.getValueForKeyPath("ceo.salary"));
	}
}
