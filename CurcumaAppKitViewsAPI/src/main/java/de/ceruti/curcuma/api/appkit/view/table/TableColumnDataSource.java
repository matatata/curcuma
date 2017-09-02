package de.ceruti.curcuma.api.appkit.view.table;

import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;

public interface TableColumnDataSource {
	Object getObject(int row, NSTableView sender);
	void setObject(Object value, int row, NSTableView sender);
	Object validateObject(Object o, int row, NSTableView sender) throws ValidationException;
	
	/**
	 * 
	 * @param col
	 * @return can return null
	 */
	Class<?> getColumnClass();
}
