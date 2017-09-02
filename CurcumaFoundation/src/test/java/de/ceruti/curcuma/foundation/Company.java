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

import java.util.ArrayList;
import java.util.List;

import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;

public class Company extends NSObjectImpl {
	private List<Employee> employees = new ArrayList<Employee>();
	private Employee ceo;
	
	public List<Employee> getEmployees() {
		return employees;
	}

	@PostKVONotifications
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public Employee getCeo() {
		return ceo;
	}
	
	@PostKVONotifications
	public void setCeo(Employee ceo) {
		this.ceo = ceo;
	}
	
	
	public static Company createSampleCompany() {
		Company c = new Company();
		Employee e = new Employee();
		e.setName("Steve Jobs");
		c.setCeo(e);
		e = new Employee();
		e.setName("Matteo Ceruti");
		c.getEmployees().add(e);
		e = new Employee();
		e.setName("Steve Wozniak");
		c.getEmployees().add(e);
		return c;
	}
	
	public static Company createSampleCompany2() {
		Company c = new Company();
		Employee e = new Employee();
		e.setName("Bill Gates");
		c.setCeo(e);
		e = new Employee();
		e.setName("Steve Balmer");
		c.getEmployees().add(e);
		e = new Employee();
		e.setName("Developer Developer");
		c.getEmployees().add(e);
		return c;
	}
}