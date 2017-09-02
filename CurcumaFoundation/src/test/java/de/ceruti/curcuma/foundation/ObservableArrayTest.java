package de.ceruti.curcuma.foundation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.foundation.Logging;
import de.ceruti.curcuma.foundation.ObservableArray;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;
import de.ceruti.curcuma.keyvalueobserving.util.LoggingObserver;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;

public class ObservableArrayTest extends TestCase {

	static {
		Logging.init();
	}
	
	public static class Test extends NSObjectImpl {
		List<Employee> employees = new ArrayList<Employee>();
		
		/*
		 * observableEmployees wird getriggert, wenn employees getriggert werden
		 */
		public static Set<String> keyPathsForValuesAffectingValueForKey(String key) {
			
			Set<String> ret = NSObjectImpl.keyPathsForValuesAffectingValueForKey(key);
			
			if(key.equals("observableEmployees"))
			{
				ret.add("employees");
			}
			
			return ret;
		}
		
		public List<Employee> getEmployees() {
			return employees;
		}

		@PostKVONotifications
		public void setEmployees(List<Employee> employees) {
			this.employees = employees;
		}

		private ObservableArray observableEmployees = new ObservableArray(null,Test.this,"observableEmployees",this.employees);

		public ObservableArray getObservableEmployees() {
			return observableEmployees;
		}
		
		@Override
		public List mutableArrayValueForKey(String s) {
			if (s.equals("observableEmployees"))
				return getObservableEmployees();
			
			return super.mutableArrayValueForKey(s);
		}

	}
	
	/**
	 * 
	 */
	public void test0() {
		Company c = Company.createSampleCompany();
		Test test = new Test();
		ArrayList<KVOEvent> evtList = new ArrayList<KVOEvent>();
		RecordingObserver o1 = new RecordingObserver("employees",evtList);
		test.addObserver(o1, "employees", null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld
						| KVOOption.KeyValueObservingOptionPrior);
		
		test.mutableArrayValueForKey("employees").addAll(c.getEmployees());
		ArrayList<Employee> tmp = new ArrayList<Employee>();
		for (int i=0;i<evtList.size();i++){
			KVOEvent e = evtList.get(i);
			
			if(i%2==0) {
				//ungerade:
				assertTrue(e.isPriorEvent());
			}
			else {
				Employee empl = c.getEmployees().get(i/2);
				Employee empl2 = (Employee) test.getObservableEmployees().get(i/2);
				assertEquals(empl,empl2);
				assertTrue(e.hasNewValue());
				assertFalse(e.hasOldValue());
				assertFalse(((Collection)e.getNewValue()).isEmpty());
				for(Object o : (Collection)e.getNewValue()) {
					assertTrue(c.getEmployees().contains(o));
				}
				tmp.addAll((Collection)e.getNewValue());
				
			}
		}
		assertEquals(tmp, c.getEmployees());
		evtList.clear();
		
		
		test.mutableArrayValueForKey("employees").removeAll(c.getEmployees());
		for (int i=0;i<evtList.size();i++){
			KVOEvent e = evtList.get(i);
			
			if(i%2==0) {
				//ungerade:
				assertTrue(e.isPriorEvent());
			}
			else {
				Employee empl = c.getEmployees().get(i/2);
				assertFalse(e.hasNewValue());
				assertTrue(e.hasOldValue());
				assertFalse(((Collection)e.getOldValue()).isEmpty());
				for(Object o : (Collection)e.getOldValue()) {
					assertTrue(c.getEmployees().contains(o));
				}
				tmp.removeAll((Collection)e.getOldValue());
				
			}
		}
		assertTrue(tmp.isEmpty());
		
		test.removeObserver(o1, "employees");
		
		assertFalse(test.hasObservers());
		
	}
	
	
	/**
	 * 
	 */
	public void test1() {
		Company c = Company.createSampleCompany();
		Test test = new Test();
		ArrayList<KVOEvent> evtList = new ArrayList<KVOEvent>();
		RecordingObserver o1 = new RecordingObserver("observableEmployees",evtList);
		test.addObserver(o1, "observableEmployees", null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld
						| KVOOption.KeyValueObservingOptionPrior);
		
		test.mutableArrayValueForKey("observableEmployees").addAll(c.getEmployees());
		ArrayList<Employee> tmp = new ArrayList<Employee>();
		for (int i=0;i<evtList.size();i++){
			KVOEvent e = evtList.get(i);
			
			if(i%2==0) {
				//ungerade:
				assertTrue(e.isPriorEvent());
			}
			else {
				Employee empl = c.getEmployees().get(i/2);
				Employee empl2 = (Employee) test.getObservableEmployees().get(i/2);
				assertEquals(empl,empl2);
				assertTrue(e.hasNewValue());
				assertFalse(e.hasOldValue());
				assertFalse(((Collection)e.getNewValue()).isEmpty());
				for(Object o : (Collection)e.getNewValue()) {
					assertTrue(c.getEmployees().contains(o));
				}
				tmp.addAll((Collection)e.getNewValue());
				
			}
		}
		assertEquals(tmp, c.getEmployees());
		evtList.clear();
		
		
		test.mutableArrayValueForKey("observableEmployees").removeAll(c.getEmployees());
		for (int i=0;i<evtList.size();i++){
			KVOEvent e = evtList.get(i);
			
			if(i%2==0) {
				//ungerade:
				assertTrue(e.isPriorEvent());
			}
			else {
				Employee empl = c.getEmployees().get(i/2);
				assertFalse(e.hasNewValue());
				assertTrue(e.hasOldValue());
				assertFalse(((Collection)e.getOldValue()).isEmpty());
				for(Object o : (Collection)e.getOldValue()) {
					assertTrue(c.getEmployees().contains(o));
				}
				tmp.removeAll((Collection)e.getOldValue());
				
			}
		}
		assertTrue(tmp.isEmpty());
		
		test.removeObserver(o1, "observableEmployees");
		
		assertFalse(test.hasObservers());
		
	}
	
	/**
	 * 
	 */
	public void test2() {
		Company c = Company.createSampleCompany();
		Test test = new Test();
		ArrayList<KVOEvent> evtList = new ArrayList<KVOEvent>();
		ArrayList<KVOEvent> evtList2 = new ArrayList<KVOEvent>();
		RecordingObserver o1 = new RecordingObserver("observableEmployees",evtList);
		test.addObserver(o1, "observableEmployees", null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld
						| KVOOption.KeyValueObservingOptionPrior);
		RecordingObserver o2 = new RecordingObserver("observableEmployees.name",evtList2);
		test.addObserver(o2, "observableEmployees.name", null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld
						| KVOOption.KeyValueObservingOptionPrior);
		
		
		//depkey
		test.mutableArrayValueForKey("employees").addAll(c.getEmployees());
		for (int i=0;i<evtList2.size();i++){
			//TODO
		}
		test.mutableArrayValueForKey("employees").removeAll(c.getEmployees());
		
		evtList.clear();
		evtList2.clear();
		
		test.mutableArrayValueForKey("observableEmployees").addAll(c.getEmployees());
		ArrayList<Employee> tmp = new ArrayList<Employee>();
		for (int i=0;i<evtList.size();i++){
			KVOEvent e = evtList.get(i);
			
			if(i%2==0) {
				//ungerade:
				assertTrue(e.isPriorEvent());
			}
			else {
				Employee empl = c.getEmployees().get(i/2);
				Employee empl2 = (Employee) test.getObservableEmployees().get(i/2);
				assertEquals(empl,empl2);
				assertTrue(e.hasNewValue());
				assertFalse(e.hasOldValue());
				assertFalse(((Collection)e.getNewValue()).isEmpty());
				for(Object o : (Collection)e.getNewValue()) {
					assertTrue(c.getEmployees().contains(o));
				}
				tmp.addAll((Collection)e.getNewValue());
				
			}
		}
		assertEquals(tmp, c.getEmployees());
		evtList.clear();
		
		
		for(Object o : test.getObservableEmployees()) {
			Employee employee = (Employee)o;
			employee.setName(employee.getName() + " Jr.");
		}
		
		for (int i=0;i<evtList2.size();i++){
			KVOEvent e = evtList2.get(i);
			
			if(i%2==0) {
				//ungerade:
				assertTrue(e.isPriorEvent());
				continue;
			}
			
		}
		
		test.mutableArrayValueForKey("observableEmployees").removeAll(c.getEmployees());
		for (int i=0;i<evtList.size();i++){
			KVOEvent e = evtList.get(i);
			
			if(i%2==0) {
				//ungerade:
				assertTrue(e.isPriorEvent());
			}
			else {
				Employee empl = c.getEmployees().get(i/2);
				assertFalse(e.hasNewValue());
				assertTrue(e.hasOldValue());
				assertFalse(((Collection)e.getOldValue()).isEmpty());
				for(Object o : (Collection)e.getOldValue()) {
					assertTrue(c.getEmployees().contains(o));
				}
				tmp.removeAll((Collection)e.getOldValue());
				
			}
		}
		assertTrue(tmp.isEmpty());
		
		LoggingObserver o3 = new LoggingObserver("employees");
		
		test.addObserver(o3, "employees", null,
				KVOOption.KeyValueObservingOptionOld
						| KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionPrior);
		
		test.printInfo(System.out);
		
		test.removeObserver(o2, "observableEmployees.name");
		test.mutableArrayValueForKey("employees").add(new Employee("googo"));
	
		test.removeObserver(o1, "observableEmployees");
		test.removeObserver(o3, "employees");
		assertFalse(test.hasObservers());
		test.printInfo(System.out);
			
		
		
		
	}

	public void test3() {
		Employee e = new Employee("John Doe");
		LoggingObserver obs3 = new LoggingObserver("description");
		LoggingObserver obs1 = new LoggingObserver("department");
		LoggingObserver obs2 = new LoggingObserver("department.id");
		e.addObserver(obs1, "department", null, KVOOption.KeyValueObservingOptionNew);
		e.addObserver(obs2, "department.id", null, KVOOption.KeyValueObservingOptionNew);
		e.addObserver(obs3, "description", null, KVOOption.KeyValueObservingOptionNew);
		
		
		e.setDepartment(new Employee.Department(2));


		e.removeObserver(obs1, "department");
		e.removeObserver(obs3, "description");
		e.removeObserver(obs2, "department.id");
		
		assertFalse(e.hasObservers());
		
	}
	
	public void test1221() {
		Company c = Company.createSampleCompany();
		List<Employee> l  = c.getEmployees();
		ObservableArray s = new ObservableArray(c,c,"dummy",l);
		
		LoggingObserver obs = new LoggingObserver("name");
		
		
//		c.mutableArrayValueForKey("observableEmployees").addAll(l);
		
		s.addObserver(obs, "name", null, KVOOption.KeyValueObservingOptionNew
											 | KVOOption.KeyValueObservingOptionOld 
//											 | KVOOption.KeyValueObservingOptionPrior 
											);
	
		for(Object r : s) {
			Employee e = (Employee)r;
			e.setName(e.getName()+ " Jr.");
		}
		
		s.add(new Employee("Tim Sculley"));
		
		s.clear();
		
		s.removeObserver(obs, "name");
		
		////////////////////////
		
		c = Company.createSampleCompany();
		
		Test controller = new Test();
		controller.addObserver(new LoggingObserver("observableEmployees"), "observableEmployees", null, KVOOption.KeyValueObservingOptionNew
				 | KVOOption.KeyValueObservingOptionOld);
		
		obs = new LoggingObserver("observableEmployees.name");
		controller.addObserver(obs,
				"observableEmployees.name", null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld);
		
//		controller.setEmployees(c.getEmployees());
		
		controller.mutableArrayValueForKey("observableEmployees").addAll(c.getEmployees());
		
		
		controller.mutableArrayValueForKey("observableEmployees").add(new Employee("Tim Sculley1"));
//		controller.getObservableEmployees().add(new Employee("Tim Sculley2"));
//		controller.getEmployees().add(new Employee("Tim Sculley3"));
//		
//		
		for(Object r : controller.getObservableEmployees()) {
			Employee e = (Employee)r;
			e.setName(e.getName()+ " Jr.");
		}
		
		controller.getObservableEmployees().remove(2);
		controller.getObservableEmployees().clear();
		
		controller.getObservableEmployees().add(new Employee("Tim Sculley4"));
		

		controller.removeObserver(obs, "observableEmployees.name");
		
		
		obs = new LoggingObserver("observableEmployees.department.id");
		controller.addObserver(obs,
				"observableEmployees.department.id", null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld);
		
		((Employee)controller.getObservableEmployees().get(0)).getDepartment().setId(999);
		
		
		controller.removeObserver(obs, "observableEmployees.department.id");
		System.out.println(controller.getObservableEmployees());
	}
}
