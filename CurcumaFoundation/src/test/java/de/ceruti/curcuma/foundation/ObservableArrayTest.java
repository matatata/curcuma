package de.ceruti.curcuma.foundation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;
import de.ceruti.curcuma.keyvalueobserving.util.RecordingObserver;
import junit.framework.TestCase;

@SuppressWarnings({"unchecked","rawtypes"})
public class ObservableArrayTest extends TestCase {

	
	public static class BeanWithObservableArrayAndConventionalCollection extends NSObjectImpl
	{
		List<Employee> employees = new ArrayList<Employee>();
		
		
		public List<Employee> getEmployees() {
			return employees;
		}

		@PostKVONotifications
		public void setEmployees(List<Employee> employees) {
			this.employees = employees;
		}
		
		private ObservableArray observableEmployees;

		public ObservableArray getObservableEmployees() {
			if(observableEmployees==null){
				observableEmployees = new ObservableArray(null,(KeyValueObserving) this,"observableEmployees",this.employees);
			}
			return observableEmployees;
		}
		
		@Override
		public List mutableArrayValueForKey(String s) {
			if ("observableEmployees".equals(s))
				return getObservableEmployees();
			
			return super.mutableArrayValueForKey(s);
		}

	}
	
	
	private void testInsertionAndRemoval(String collectionName) {
		Company c = Company.createSampleCompany();
		BeanWithObservableArrayAndConventionalCollection test = new BeanWithObservableArrayAndConventionalCollection();
		RecordingObserver observer = new RecordingObserver(collectionName);
		test.addObserver(observer, collectionName, null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld
						| KVOOption.KeyValueObservingOptionPrior);
		
		test.mutableArrayValueForKey(collectionName).addAll(c.getEmployees());
		ArrayList<Employee> controlList = new ArrayList<Employee>();
		assertEquals(2, observer.getEvts().size());
		for (int i=0;i<observer.getEvts().size();i++){
			KVOEvent e = observer.getEvts().get(i);
			
			assertEquals(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, e.getKind());
			
			if(i==0) {
				assertTrue(e.isPriorEvent());
			}
			else {
				assertTrue(e.hasNewValue());
				assertFalse(e.hasOldValue());
				assertFalse(((Collection)e.getNewValue()).isEmpty());
				controlList.addAll((Collection)e.getNewValue());
			}
		}
		assertEquals(controlList, c.getEmployees());
		observer.clear();
		
		
		test.mutableArrayValueForKey(collectionName).removeAll(c.getEmployees());
		assertEquals(2, observer.getEvts().size());
		for (int i=0;i<observer.getEvts().size();i++){
			KVOEvent e = observer.getEvts().get(i);
			
			if(i==0) {
				assertTrue(e.isPriorEvent());
			}
			else {
				assertFalse(e.hasNewValue());
				assertTrue(e.hasOldValue());
				assertFalse(((Collection)e.getOldValue()).isEmpty());
				controlList.removeAll((Collection)e.getOldValue());
				
			}
		}
		assertTrue(controlList.isEmpty());
		
		test.removeObserver(observer, collectionName);
		
		assertFalse(test.hasObservers());
		
	}
	
	public void testInsertionAndRemovalDefaultImplementation() {
		testInsertionAndRemoval("employees");
	}
	
	public void testInsertionAndRemovalExplicitObservableArray() {
		testInsertionAndRemoval("observableEmployees");
	}
	
	public void testInsertionRemovalAndUpdateOfRelationDefaultImplementation() {
		testInsertionRemovalAndUpdateOfRelation("employees",false);
	}
	
	public void testInsertionRemovalAndUpdateOfRelationExplicitObservableArray() {
		testInsertionRemovalAndUpdateOfRelation("observableEmployees",true);
	}
	
	
	
	private void testInsertionRemovalAndUpdateOfRelation(String collectionName, boolean isObservableArray) {
		Company c = Company.createSampleCompany();
		BeanWithObservableArrayAndConventionalCollection test = new BeanWithObservableArrayAndConventionalCollection();
		RecordingObserver observer = new RecordingObserver(collectionName+".name");
		test.addObserver(observer, collectionName+".name", null,
				KVOOption.KeyValueObservingOptionNew
						| KVOOption.KeyValueObservingOptionOld
						| KVOOption.KeyValueObservingOptionPrior);
		
		
		test.mutableArrayValueForKey(collectionName).addAll(c.getEmployees());
		
		ArrayList<Employee> controlList = new ArrayList<Employee>();
		assertEquals(2, observer.getEvts().size());
		for (int i=0;i<observer.getEvts().size();i++){
			KVOEvent e = observer.getEvts().get(i);
			
			assertEquals(KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION, e.getKind());
			
			if(i==0) {
				assertTrue(e.isPriorEvent());
			}
			else {
				assertTrue(e.hasNewValue());
				assertFalse(e.hasOldValue());
				assertFalse(((Collection)e.getNewValue()).isEmpty());
				controlList.addAll((Collection)e.getNewValue());
			}
		}
		assertEquals(controlList, test.getObservableEmployees().getValueForKeyPath("name"));
		observer.clear();
		
		
		test.mutableArrayValueForKey(collectionName).removeAll(c.getEmployees());
		assertEquals(2, observer.getEvts().size());
		for (int i=0;i<observer.getEvts().size();i++){
			KVOEvent e = observer.getEvts().get(i);
			
			if(i==0) {
				assertTrue(e.isPriorEvent());
			}
			else {
				assertFalse(e.hasNewValue());
				assertTrue(e.hasOldValue());
				assertFalse(((Collection)e.getOldValue()).isEmpty());
				controlList.removeAll((Collection)e.getOldValue());
				
			}
		}
		assertTrue(controlList.isEmpty());
		
		
		
		List mutableCollection = test.mutableArrayValueForKey(collectionName);
		mutableCollection.addAll(c.getEmployees());

		observer.clear();
		
		for(Object o : test.getObservableEmployees()) {
			Employee employee = (Employee)o;
			employee.setName(employee.getName()+"'");
		}
		
		
		if(isObservableArray){
			assertEquals(4, observer.getEvts().size());
			for (int i=0;i<observer.getEvts().size();i++){
				KVOEvent e = observer.getEvts().get(i);
				
				if(i%2==0) {
					assertTrue(e.isPriorEvent());
				}
				else {
					assertTrue(e.hasNewValue());
					assertTrue(e.hasOldValue());
					assertEquals(((List<String>)e.getOldValue()).get(0) + "'",((List<String>)e.getNewValue()).get(0));
				}
			}
		}
		
		test.removeObserver(observer, collectionName+".name");
		assertFalse(test.hasObservers());
	}

}
