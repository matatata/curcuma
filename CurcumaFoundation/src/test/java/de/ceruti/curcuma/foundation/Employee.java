/*
This file is part of Curcuma.

Copyright (c) Matteo Ceruti 2009

Curcuma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Curcuma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Curcuma.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.ceruti.curcuma.foundation;

import java.util.Set;

import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;



public class Employee extends NSObjectImpl{
	private String name;
	private double salary;
	private Employee boss;

	


	public Employee getBoss() {
		return boss;
	}

	@PostKVONotifications
	public void setBoss(Employee boss) {
		try {
			boss = validateBoss(boss);
			this.boss = boss;
		}
		catch(ValidationException e){
			throw new IllegalArgumentException();
		}
	}

	public static class Department extends NSObjectImpl {
		private int id;

		
		public Department(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}

		@PostKVONotifications
		public void setId(int id) {
			this.id = id;
		}
	}
	
	private Department department = new Department(0);
	
	public Department getDepartment() {
		return department;
	}

	@PostKVONotifications
	public void setDepartment(Department department) {
		this.department = department;
	}

	public static Set<String> keyPathsForValuesAffectingValueForKey(String key) {
		Set<String> s = NSObjectImpl.keyPathsForValuesAffectingValueForKey(key);
		if(key.equals("description")){
			s.add("name");
			s.add("salary");
			s.add("department");
			s.add("boss");
			s.add("boss.salary");
		}
		
		
		return s;
	}
	
	public Employee() {
		this("John Doe");
	}
	
	public Employee(String string) {
		setName(string);
	}
	public String getName() {
		return name;
	}

	@PostKVONotifications
	public void setName(String name) {

		try {
			this.name = validateName(name);
		} catch (ValidationException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public double getSalary() {
		return salary;
	}

	@PostKVONotifications
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	@Override
	public String toString() {
		return "Employee:(" + getName() + ")";
	}
	
	public String getDescription() {
		StringBuffer buf = new StringBuffer();
		buf.append("Curiculum Vitae of '" + getName() + "'. His/Her current salary is " + getSalary() + ".");
		if(getBoss()!=null) {
			buf.append(" Boss: ");
			buf.append(getBoss().getName() + " Salary: " + boss.getSalary());
		}
		return buf.toString();
	}
	
	public String validateName(String name) throws ValidationException {
		
		name = name.trim();
		
		if(name==null || name.length()==0)
			throw new ValidationException("Name must not be empty");
		return name;
	}
	
	public Employee validateBoss(Employee s) throws ValidationException {
		return s;
	}
	
	public Double validateSalary(Double s) throws ValidationException {
		if(s < 0)
			throw new ValidationException("Salary must not be negative");
		return Math.abs(s);
	}
}