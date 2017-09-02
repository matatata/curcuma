package de.ceruti.curcuma.appkit;


import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.appkit.controllers.NSArrayControllerImpl;
import de.ceruti.curcuma.appkit.controllers.NSObjectControllerImpl;
import de.ceruti.curcuma.core.Factory;
import de.ceruti.curcuma.foundation.Company;
import de.ceruti.curcuma.foundation.Employee;
import de.ceruti.curcuma.keyvalueobserving.util.LoggingObserver;
import junit.framework.TestCase;

public class NSObjectControllerImplTest extends TestCase {

	
	public void test01(){
		Company company = Company.createSampleCompany();
		Company company2 = Company.createSampleCompany2();
		
		NSObjectControllerImpl controller = new NSObjectControllerImpl();
		controller.setContent(company);
		KVObserver obs = new LoggingObserver("employees.name");
		controller.addObserver(obs,"selection.employees.name",null,KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
		controller.setContent(company2);
		controller.removeObserver(obs,"selection.employees.name");
		assertFalse(controller.hasObservers());
	}
	
	 public void test03(){
	    Company company = Company.createSampleCompany();
	    Company company2 = Company.createSampleCompany2();
	    
	    NSArrayControllerImpl controller = new NSArrayControllerImpl();
	    controller.setContentArray(company.getEmployees());
	    
	    KVObserver obs = new LoggingObserver("selection.name");
	    controller.addObserver(obs,"selection.name",null,KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
	    
	    controller.setContent(company2.getEmployees());
	    controller.removeObserver(obs,"selection.name");
	    assertFalse(controller.hasObservers());
	  }
	 
   public void test04(){
     Company company = Company.createSampleCompany();
     Company company2 = Company.createSampleCompany2();
     
     NSArrayControllerImpl controller = new NSArrayControllerImpl();
     controller.setContentArray(company.getEmployees());
     
     KVObserver obs = new LoggingObserver("arrangedObjects.name");
     controller.addObserver(obs,"arrangedObjects.name",null,KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
     
     company.getEmployees().get(0).getDepartment().setId(222);
     controller.mutableArrayValueForKey("arrangedObjects").add(new Employee("Thomas Cook"));
     controller.getArrangedObjects().add(new Employee("Mr. Spock"));
     controller.addObject(new Employee("James T. Kirk"));
     
     
     controller.setContent(company2.getEmployees());
     controller.removeObserver(obs,"arrangedObjects.name");
     assertFalse(controller.hasObservers());
   }
   
   public void test05(){
     Company company = Company.createSampleCompany();
     Company company2 = Company.createSampleCompany2();
     
     NSArrayControllerImpl controller = new NSArrayControllerImpl();
     controller.setContentArray(company.getEmployees());
     
     assertTrue(controller.getSelectedObjects().isEmpty());
     
     KVObserver obs = new LoggingObserver("arrangedObjects.name");
     controller.addObserver(obs,"arrangedObjects.name",null,KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
     KVObserver obs2 = new LoggingObserver("selectionIndexes");
     controller.addObserver(obs2,"selectionIndexes",null,KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
     KVObserver obs3 = new LoggingObserver("selection.name");
     controller.addObserver(obs3,"selection.name",null,KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
  
     
     controller.addObjects(company2.getEmployees());
     controller.addSelectedObjects(company.getEmployees());
     assertEquals(controller.getSelectedObjects(), company.getEmployees());
     
     controller.setSelectionIndexes(Factory.indexSet(2));
     
     controller.removeObserver(obs,"arrangedObjects.name");
     controller.removeObserver(obs2, "selectionIndexes");
     controller.removeObserver(obs3, "selection.name");
     assertFalse(controller.hasObservers());
     
   }
   
   public void test06(){
     Company company = Company.createSampleCompany();
     NSArrayControllerImpl controller = new NSArrayControllerImpl();
     controller.setObjectClass(Employee.class);
     controller.setContentArray(company.getEmployees());
     
     KVObserver obs = new LoggingObserver("arrangedObjects.name");
     controller.addObserver(obs,"arrangedObjects.name",null,KVOOption.KeyValueObservingOptionNew | KVOOption.KeyValueObservingOptionOld);
  
     controller.add();
     controller.remove();
     
     
     controller.removeObserver(obs,"arrangedObjects.name");
     assertFalse(controller.hasObservers());
     
   }
   
}
